package com.ecommercial.frontend.controller;

import com.ecommercial.frontend.models.*;
import com.ecommercial.frontend.models.response.APIResponse;
import com.ecommercial.frontend.service.AuthenticationService;
import com.ecommercial.frontend.service.OrderService;
import com.ecommercial.frontend.service.ProductService;
import jakarta.servlet.http.HttpSession;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@Slf4j
@Controller
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartController {

    ProductService productService;
    OrderService orderService;
    AuthenticationService authenticationService;

    @GetMapping("/cart/add/{productId}")
    public String addToCart(@PathVariable("productId") Integer productId, HttpSession session, RedirectAttributes redirectAttributes) {

        if(!authenticationService.isLogin(session))
        {
            redirectAttributes.addFlashAttribute("message", "Please Login To Add To Cart");
            redirectAttributes.addFlashAttribute("alertClass", "alert-warning");
            return "redirect:/login";
        }

        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
            session.setAttribute("cart", cart);
        }

        APIResponse apiResponse = productService.getProductById(productId);
        if(apiResponse.isIsSuccess() && apiResponse != null)
        {
            String jsonString = apiResponse.getResult().toString();
            Object result = productService.deserializeObject(jsonString);
            ProductDTO product = (ProductDTO) result;



            // Kiểm tra xem sản phẩm đã có trong giỏ hàng chưa
            boolean itemExists = false;
            for (CartItem item : cart) {
                if (item.getProductId().equals(product.getId())) {
                    if (item.getQuantity() + 1 > product.getAvailableQuantity()) {
                        redirectAttributes.addFlashAttribute("message", "Add To Cart Fail! Not enough stock.");
                        redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
                        return "redirect:/home/index";
                    }

                    item.setQuantity(item.getQuantity() + 1);
                    itemExists = true;
                    break;
                }
            }


            if (!itemExists) {
                if (1 > product.getAvailableQuantity()) {
                    redirectAttributes.addFlashAttribute("message", "Add To Cart Fail! Not enough stock.");
                    redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
                    return "redirect:/home/index";
                }
                cart.add(new CartItem(product.getId(), product.getName(), product.getPrice(), 1));
            }


            redirectAttributes.addFlashAttribute("message", "Add To Cart Successfully!");
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        }
        else
        {
            redirectAttributes.addFlashAttribute("message", "Add To Cart Fail!");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
        }
        return "redirect:/home/index";
    }


    @GetMapping("/cartIndex")
    public String viewCart(Model model, HttpSession session) {

        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");

        if (cart == null) {
            cart = new ArrayList<>();
            session.setAttribute("cart", cart);
            model.addAttribute("cart", cart);
            return "cartIndex";
        }

        BigDecimal total = cart.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        int itemCount = cart.stream()
                .mapToInt(CartItem::getQuantity)
                .sum();


        model.addAttribute("cart", cart);
        model.addAttribute("total", total);
        model.addAttribute("itemCount", itemCount);
        return "cartIndex";
    }

    @GetMapping("/checkout")
    public String checkOut(Model model,HttpSession session)
    {
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");


        BigDecimal total = cart.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<PaymentMethod> paymentMethods = Arrays.asList(PaymentMethod.values());


        model.addAttribute("cart", cart);
        model.addAttribute("total", total);
        model.addAttribute("paymentMethods", paymentMethods);

        return "checkout";
    }

    @GetMapping("/placeorder")
    public String placeOrder(Model model, HttpSession httpSession, RedirectAttributes redirectAttributes,
                             @RequestParam("paymentMethod") PaymentMethod paymentMethod)
    {

        APIResponse response = orderService.placeOrder(httpSession, paymentMethod);

        if(response.isIsSuccess() && response != null)
        {
            redirectAttributes.addFlashAttribute("message", "Place Order successfully!");
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");
            httpSession.removeAttribute("cart");
            return "redirect:/home/index";
        }
        else
        {
            redirectAttributes.addFlashAttribute("message", "Place Order failed!");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
        }
        return "redirect:/productIndex";
    }


    @GetMapping("/removeProduct")
    public String removeProduct(Model model,HttpSession session, RedirectAttributes redirectAttributes, Integer productId)
    {
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");

        if (cart != null) {
            // Iterate over the cart items
            Iterator<CartItem> iterator = cart.iterator();
            while (iterator.hasNext()) {
                CartItem item = iterator.next();
                // Check if the current item’s productId matches the one to remove
                if (item.getProductId().equals(productId)) {
                    // Remove the item from the cart
                    iterator.remove();
                    break; // Exit the loop after removing the item
                }
            }

            // Update the session attribute with the modified cart
            session.setAttribute("cart", cart);

            // Add a message to the redirect attributes (optional)
            redirectAttributes.addFlashAttribute("message", "Product removed from cart");
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        } else {
            // Add an error message if the cart was not found
            redirectAttributes.addFlashAttribute("error", "Cart is empty or not found");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
        }
        return "redirect:/cartIndex";

    }


    @GetMapping("/updateQuantity")
    public String updateQuantity(HttpSession session, @RequestParam Integer productId, @RequestParam Integer change, RedirectAttributes redirectAttributes) {
        // Retrieve the cart from the session
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        APIResponse apiResponse = productService.getProductById(productId);
        String jsonString = apiResponse.getResult().toString();
        Object result = productService.deserializeObject(jsonString);
        ProductDTO product = (ProductDTO) result;


        if (cart != null) {
            Iterator<CartItem> iterator = cart.iterator();
            while (iterator.hasNext()) {
                CartItem item = iterator.next();
                if (item.getProductId().equals(productId)) {

                    int newQuantity = item.getQuantity() + change;

                    if(newQuantity > product.getAvailableQuantity())
                    {
                        redirectAttributes.addFlashAttribute("message", "Add To Cart Fail! Not enough stock.");
                        redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
                        return "redirect:/cartIndex";
                    }
                    if (newQuantity <= 0) {
                        // Remove the item if the new quantity is zero or less
                        iterator.remove();
                    } else {
                        // Otherwise, update the quantity
                        item.setQuantity(newQuantity);
                    }
                    break; // Exit the loop after updating/removing the item
                }
            }

            // Update the session attribute with the modified cart
            session.setAttribute("cart", cart);

            // Add a message to the redirect attributes (optional)
            redirectAttributes.addFlashAttribute("message", "Cart updated");
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        } else {
            // Add an error message if the cart was not found
            redirectAttributes.addFlashAttribute("error", "Cart is empty or not found");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
        }

        // Redirect to a suitable page (e.g., the cart page or a confirmation page)
        return "redirect:/cartIndex"; // Adjust the redirect URL as needed
    }

}
