package com.ecommercial.frontend.controller;

import com.ecommercial.frontend.models.response.APIResponse;
import com.ecommercial.frontend.models.ProductDTO;
import com.ecommercial.frontend.service.ProductService;
import jakarta.servlet.http.HttpSession;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Slf4j
@Controller
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HomeController {
    ProductService productService;


    @GetMapping("/home/index")
    public String homeIndex(Model model)
    {
        APIResponse responseDTO = productService.getAllProduct();
        log.warn("Im in");
        if(responseDTO.isIsSuccess() && responseDTO != null)
        {
            String jsonString = responseDTO.getResult().toString();
            Object result = productService.deserializeObject(jsonString);
            List<ProductDTO> products = (List<ProductDTO>) result;
            model.addAttribute("products",products);
            return "homeIndex";
        }
        else
        {
            log.warn("Failed");
        }
        return "error";
    }

    @ControllerAdvice
    public class GlobalControllerAdvice {

        @ModelAttribute
        public void addUserToModel(HttpSession session, Model model) {
            String username = (String) session.getAttribute("loggedInUser");
            model.addAttribute("username", username);
        }
    }


}
