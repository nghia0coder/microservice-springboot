package com.ecommercial.frontend.controller;

import com.ecommercial.frontend.models.request.LoginRequest;
import com.ecommercial.frontend.models.request.RegisterRequest;
import com.ecommercial.frontend.models.response.APIResponse;
import com.ecommercial.frontend.models.response.AuthenticationResponse;
import com.ecommercial.frontend.service.AuthenticationService;
import com.ecommercial.frontend.service.CustomerService;
import com.ecommercial.frontend.ultilities.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {

    AuthenticationService authenticationService;
    CustomerService customerService;
    @GetMapping("/login")
    public String loginForm(Model model)
    {
        LoginRequest loginRequest = new LoginRequest();
        model.addAttribute("loginRequest",loginRequest);
        return "login";
    }

    @PostMapping("/loginConfirm")
    public String login(LoginRequest loginRequest, RedirectAttributes redirectAttributes, Model model, HttpSession session)
    {
        APIResponse apiResponse  = authenticationService.login(loginRequest);
        if(apiResponse.isIsSuccess() && apiResponse != null)
        {
            String jsonString = apiResponse.getResult().toString();
            Object result = authenticationService.deserializeObject(jsonString);
            AuthenticationResponse authenticationResponse = (AuthenticationResponse) result;

            session.setAttribute("loggedInUser", JwtUtil.getSubFromToken(((AuthenticationResponse) result).getToken()));
            session.setAttribute("role", JwtUtil.getScopeFromToken(((AuthenticationResponse) result).getToken()));
            session.setAttribute("customerId", JwtUtil.getIdFromToken(((AuthenticationResponse) result).getToken()));



            redirectAttributes.addFlashAttribute("message", "Login successfully!");
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");

            return "redirect:/home/index";
        }
        else
        {
            redirectAttributes.addFlashAttribute("message", "Login failed! Invalid Password or Username");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            return "redirect:/login";
        }
    }

    @PostMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        if (session != null) {
            session.invalidate();
        }

        redirectAttributes.addFlashAttribute("message", "You have been logged out successfully.");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");

        // Redirect to the login page or home page
        return "redirect:/home/index";
    }




    @GetMapping("/register")
    public String registerForm(Model model)
    {
        RegisterRequest request = new RegisterRequest();
        model.addAttribute("register", request);
        return "register";
    }

    @PostMapping("/registerConfirm")
    public String register(Model model, @Valid @ModelAttribute("register") RegisterRequest request, BindingResult bindingResult, RedirectAttributes redirectAttributes)
    {
        if (bindingResult.hasErrors()) {
            model.addAttribute("register",request);
            return "register";
        }
        APIResponse apiResponse = customerService.register(request);
        if(apiResponse.isIsSuccess() && apiResponse != null)
        {
            redirectAttributes.addFlashAttribute("message", "Register successfully! Please Login");
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");
            return "redirect:/login";
        }

        int jsonStartIndex = apiResponse.getMessage().indexOf("{");

        int jsonEndIndex = apiResponse.getMessage().lastIndexOf("}") + 1;


        String jsonResponse = apiResponse.getMessage().substring(jsonStartIndex, jsonEndIndex);


        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            String message = rootNode.get("message").asText();

            redirectAttributes.addFlashAttribute("message", message);
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return "redirect:/register";
    }
}
