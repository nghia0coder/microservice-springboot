package com.ecommercial.frontend.controller;

import com.ecommercial.frontend.models.response.APIResponse;
import com.ecommercial.frontend.models.CategoryDTO;

import com.ecommercial.frontend.service.CategoryService;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.util.List;

@Slf4j
@Controller
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryController {
    CategoryService categoryService;


    @GetMapping("/categoryIndex")
    public String homeIndex(Model model)
    {
        APIResponse responseDTO = categoryService.getAllCategory();
        if(responseDTO.isIsSuccess() && responseDTO != null)
        {
            String jsonString = responseDTO.getResult().toString();
            Object result = categoryService.deserializeObject(jsonString);
            List<CategoryDTO> categoryDTOs = (List<CategoryDTO>) result;
            model.addAttribute("categories",categoryDTOs);
            return "categoryIndex";
        }
        else
        {
            log.warn("Failed");
        }
        return "error";
    }

    @GetMapping("/category/create")
    public String categoryCreateForm(Model model)
    {
        CategoryDTO categoryDTO = new CategoryDTO();
        model.addAttribute("category",categoryDTO);
        return "categoryCreate";
    }
    @PostMapping("/createCategory")
    public String createcategory(@Valid @ModelAttribute("category") CategoryDTO categoryDTO,BindingResult result, RedirectAttributes redirectAttributes, Model model)
    {
        if (result.hasErrors()) {
            log.warn("Error");
            model.addAttribute("category",categoryDTO);
            return "categoryCreate";
        }

        APIResponse apiResponse = categoryService.createCategory(categoryDTO);

        if(apiResponse.isIsSuccess() && apiResponse != null)
        {
            redirectAttributes.addFlashAttribute("message", "Category created successfully!");
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        }
        else
        {
            redirectAttributes.addFlashAttribute("message", "Category creation failed!");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
        }
        return "redirect:/categoryIndex";
    }
    @GetMapping("/categoryUpdate/{categoryId}")
    public String categoryUpdateFrom(@PathVariable("categoryId") Integer categoryId, Model model, RedirectAttributes redirectAttributes)
    {
        APIResponse apiResponse = categoryService.getCategoryById(categoryId);
        if(apiResponse.isIsSuccess() && apiResponse != null)
        {
            String jsonString = apiResponse.getResult().toString();
            Object result = categoryService.deserializeObject(jsonString);
            CategoryDTO categoryDTO = (CategoryDTO) result ;
            model.addAttribute("category", categoryDTO);
        }
        return "categoryUpdate";
    }
    @PostMapping("/updateCategory/{categoryId}")
    public String categoryUpdate(@PathVariable("categoryId") Integer categoryId,
                                Model model,@Valid @ModelAttribute("category") CategoryDTO categoryDTO,
                                BindingResult result ,
                                RedirectAttributes redirectAttributes)
    {
        if (result.hasErrors()) {

            model.addAttribute("category",categoryDTO);
            return "categoryUpdate";  // return to the form with error messages
        }
        APIResponse apiResponse = categoryService.updateCategory(categoryId,categoryDTO);
        if(apiResponse.isIsSuccess() && apiResponse != null)
        {
            redirectAttributes.addFlashAttribute("message", "Category updated successfully!");
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        }
        else
        {
            redirectAttributes.addFlashAttribute("message", "Category updated failed!");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
        }
        return "redirect:/categoryIndex";
    }
    @GetMapping("/categoryDelete/{categoryId}")
    public String categoryDeleteForm(@PathVariable("categoryId") Integer categoryId, Model model, RedirectAttributes redirectAttributes)
    {
        APIResponse apiResponse = categoryService.getCategoryById(categoryId);
        if(apiResponse.isIsSuccess() && apiResponse != null)
        {
            String jsonString = apiResponse.getResult().toString();
            Object result = categoryService.deserializeObject(jsonString);
            CategoryDTO categoryDTO = (CategoryDTO) result ;
            model.addAttribute("category", categoryDTO);
        }
        return "categoryDelete";
    }
    @PostMapping("/deleteCategory/{categoryId}")
    public String categoryDelete(@PathVariable("categoryId") Integer categoryId, RedirectAttributes redirectAttributes)
    {
        APIResponse apiResponse = categoryService.deleteCategory(categoryId);
        if(apiResponse.isIsSuccess() && apiResponse != null)
        {
            redirectAttributes.addFlashAttribute("message", "Category Deleted successfully!");
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        }
        else
        {
            redirectAttributes.addFlashAttribute("message", "Category Deleted failed!");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
        }
        return "redirect:/categoryIndex";
    }
}
