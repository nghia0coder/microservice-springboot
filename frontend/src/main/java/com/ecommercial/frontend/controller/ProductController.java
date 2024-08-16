package com.ecommercial.frontend.controller;

import com.ecommercial.frontend.models.response.APIResponse;
import com.ecommercial.frontend.models.CategoryDTO;
import com.ecommercial.frontend.models.ProductDTO;
import com.ecommercial.frontend.service.CategoryService;
import com.ecommercial.frontend.service.ProductService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductController {
    ProductService productService;
    CategoryService categoryService;

    @GetMapping("/productIndex")
    public String homeIndex(Model model)
    {
        APIResponse responseDTO = productService.getAllProduct();
        if(responseDTO.isIsSuccess() && responseDTO != null)
        {
            String jsonString = responseDTO.getResult().toString();
            Object result = productService.deserializeObject(jsonString);
            List<ProductDTO> products = (List<ProductDTO>) result;
            model.addAttribute("products",products);
            return "productIndex";
        }
        else
        {
            log.warn("Failed");
        }
        return "error";
    }

    @GetMapping("/product/create")
    public String productCreateForm(Model model)
    {
        ProductDTO productDTO = new ProductDTO();
        APIResponse apiResponse = categoryService.getAllCategory();

        String jsonString = apiResponse.getResult().toString();
        Object result = categoryService.deserializeObject(jsonString);
        List<CategoryDTO> categoryDTOs = (List<CategoryDTO>) result;
        model.addAttribute("categories", categoryDTOs);
        model.addAttribute("product",productDTO);
        return "productCreate";
    }
    @PostMapping("/createProduct")
    public String createProduct(@Valid @ModelAttribute("product") ProductDTO productDTO, BindingResult result,
                                RedirectAttributes redirectAttributes, Model model)
    {
        if (result.hasErrors()) {
            log.warn("Error");
            model.addAttribute("product",productDTO);
            return "productCreate";
        }
        APIResponse apiResponse = productService.createProduct(productDTO);

        if(apiResponse.isIsSuccess() && apiResponse != null)
        {
            redirectAttributes.addFlashAttribute("message", "Product created successfully!");
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        }
        else
        {
            redirectAttributes.addFlashAttribute("message", "Product creation failed!");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
        }
        return "redirect:/productIndex";
    }
    @GetMapping("/productUpdate/{productId}")
    public String productUpdateFrom(@PathVariable("productId") Integer productId, Model model, RedirectAttributes redirectAttributes)
    {
        APIResponse apiResponse = productService.getProductById(productId);
        if(apiResponse.isIsSuccess() && apiResponse != null)
        {
            String jsonString = apiResponse.getResult().toString();
            Object result = productService.deserializeObject(jsonString);
            ProductDTO productDTO = (ProductDTO) result ;
            /////////////////////
            APIResponse allCategory = categoryService.getAllCategory();
            String cateogryString = allCategory.getResult().toString();
            Object categoryResult = categoryService.deserializeObject(cateogryString);
            List<CategoryDTO> categoryDTOs = (List<CategoryDTO>) categoryResult;
            model.addAttribute("categories", categoryDTOs);
            /////////////////////


            model.addAttribute("product", productDTO);
        }
        else
        {
            redirectAttributes.addFlashAttribute("message", "Something wrong");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            ProductDTO productDTO = new ProductDTO();
            model.addAttribute("product", productDTO);
        }
        return "productUpdate";
    }
    @PostMapping("/updateProduct/{productId}")
    public String productUpdate(@PathVariable("productId") Integer productId, Model model,@Valid @ModelAttribute("product") ProductDTO productDTO,BindingResult result ,RedirectAttributes redirectAttributes)
    {
        if (result.hasErrors()) {

            model.addAttribute("product",productDTO);
            return "productUpdate";  // return to the form with error messages
        }
        APIResponse apiResponse = productService.updateProduct(productId,productDTO);
        if(apiResponse.isIsSuccess() && apiResponse != null)
        {
            redirectAttributes.addFlashAttribute("message", "Product updated successfully!");
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        }
        else
        {
            redirectAttributes.addFlashAttribute("message", "Product updated failed!");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
        }
        return "redirect:/productIndex";
    }
    @GetMapping("/productDelete/{productId}")
    public String productDeleteForm(@PathVariable("productId") Integer productId, Model model, RedirectAttributes redirectAttributes)
    {
        APIResponse apiResponse = productService.getProductById(productId);
        if(apiResponse.isIsSuccess() && apiResponse != null)
        {
            String jsonString = apiResponse.getResult().toString();
            Object result = productService.deserializeObject(jsonString);
            ProductDTO productDTO = (ProductDTO) result ;
            model.addAttribute("product", productDTO);
        }
        return "productDelete";
    }
    @PostMapping("/deleteProduct/{productId}")
    public String productDelete(@PathVariable("productId") Integer productId, RedirectAttributes redirectAttributes)
    {
        APIResponse apiResponse = productService.deleteProduct(productId);
        if(apiResponse.isIsSuccess() && apiResponse != null)
        {
            redirectAttributes.addFlashAttribute("message", "Product Deleted successfully!");
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        }
        else
        {
            redirectAttributes.addFlashAttribute("message", "Product Deleted failed!");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
        }
        return "redirect:/productIndex";
    }
}
