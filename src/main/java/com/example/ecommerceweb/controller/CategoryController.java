package com.example.ecommerceweb.controller;

import com.example.ecommerceweb.model.Category;
import com.example.ecommerceweb.model.Product;
import com.example.ecommerceweb.repository.ReservationRepository;
import com.example.ecommerceweb.service.CategoryService;
import com.example.ecommerceweb.service.ProductService;
import com.example.ecommerceweb.service.ReservationService;
import com.example.ecommerceweb.service.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping(value = "/admin")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @Autowired
    ProductService productService;

    @Autowired
    ReservationService reservationService;

    @Autowired
    ReservationRepository reservationRepository;

    @RequestMapping(value = "/categories")
    public String viewAllCategory(Model model){
        List<Category> categoryList = categoryService.getAllCategory();
        model.addAttribute("categories", categoryList);
        return "/category/categories";
    }

    @GetMapping("/categories/add")
    public String addCategory(Model model){
        model.addAttribute("category" , new Category());
        model.addAttribute("pageTitle" , "Add new category");
        return "/category/categoryAdd";
    }

    @PostMapping("/categories/add")
    public String saveCategory(@RequestParam(value = "id", required = false) Long id, @ModelAttribute("category") Category category, RedirectAttributes ra ){
        categoryService.addCategory(category);
        System.out.println(id);
        if(id != 0){
            ra.addFlashAttribute("msg", "Updated successfully");
        }else {
            ra.addFlashAttribute("msg", "Added successfully");
        }
        return "redirect:/admin/categories";
    }

    @GetMapping("/categories/delete/{id}")
    public String deleteCategoryById(@PathVariable(value = "id") int id, RedirectAttributes ra){
        categoryService.deleteCategoryById(id);
        ra.addFlashAttribute("msg", "Deleted successfully");
        return "redirect:/admin/categories";
    }

    @GetMapping("/categories/update/{id}")
    public String updateCategoryById(@PathVariable(value = "id") int id, Model model){
        try {
            Category category = categoryService.getCategoryById(id);
            model.addAttribute("category", category);
            model.addAttribute("pageTitle" , "Update category (Id: " + id + ")" );
            return "/category/categoryAdd";
        } catch (UserNotFoundException e) {
            e.printStackTrace();
            return "redirect:/admin/categories";
        }
    }

    @GetMapping("/categories/detail/{id}")
    public String detailCategory(@PathVariable("id") int id, Model model){
        try {
            Category category = categoryService.getCategoryById(id);
            List<Product> productList = productService.getAllProduct();
            model.addAttribute("category", category);
            model.addAttribute("products", productList);
        } catch (UserNotFoundException e) {
            e.printStackTrace();
            return "redirect:/admin/categories";
        }
        return "/category/categoryDetail";
    }

}
