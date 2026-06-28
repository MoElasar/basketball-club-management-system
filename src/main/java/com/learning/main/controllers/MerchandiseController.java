package com.learning.main.controllers;

import com.learning.main.enm.MerchandiseCategory;
import com.learning.main.model.Merchandise;
import com.learning.main.model.User;
import com.learning.main.repository.services.MerchandiseService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/merchandise")
public class MerchandiseController {
    private final MerchandiseService merchandiseService;

    public MerchandiseController(MerchandiseService merchandiseService) {
        this.merchandiseService = merchandiseService;
    }

    private boolean isAuthenticated(HttpSession session) {
        return session.getAttribute("user") != null;
    }

    @GetMapping
    public String listMerchandise(Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/login";
        }
        
        User currentUser = (User) session.getAttribute("user");
        model.addAttribute("merchandiseList", merchandiseService.getAllMerchandise());
        model.addAttribute("pageTitle", "Merchandise Management");
        model.addAttribute("currentUser", currentUser);
        return "admin/merchandise/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/login";
        }
        
        User currentUser = (User) session.getAttribute("user");
        model.addAttribute("merchandise", new Merchandise());
        model.addAttribute("categories", MerchandiseCategory.values());
        model.addAttribute("pageTitle", "Add Merchandise");
        model.addAttribute("currentUser", currentUser);
        return "admin/merchandise/add";
    }

    @PostMapping("/save")
    public String saveMerchandise(@ModelAttribute Merchandise merchandise, 
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        if (!isAuthenticated(session)) {
            return "redirect:/login";
        }
        
        try {
            merchandiseService.saveMerchandise(merchandise);
            redirectAttributes.addFlashAttribute("successMessage", "Merchandise saved successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error saving merchandise: " + e.getMessage());
        }
        return "redirect:/admin/merchandise";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, 
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        if (!isAuthenticated(session)) {
            return "redirect:/login";
        }
        
        User currentUser = (User) session.getAttribute("user");
        return merchandiseService.getMerchandiseById(id)
                .map(merch -> {
                    model.addAttribute("merchandise", merch);
                    model.addAttribute("categories", MerchandiseCategory.values());
                    model.addAttribute("pageTitle", "Edit Merchandise");
                    model.addAttribute("currentUser", currentUser);
                    return "admin/merchandise/edit";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("errorMessage", "Merchandise not found with ID: " + id);
                    return "redirect:/admin/merchandise";
                });
    }

    @PostMapping("/update/{id}")
    public String updateMerchandise(@PathVariable Long id, 
                                  @ModelAttribute Merchandise merchandise,
                                  HttpSession session,
                                  RedirectAttributes redirectAttributes) {
        if (!isAuthenticated(session)) {
            return "redirect:/login";
        }
        
        try {
            merchandise.setMerchandiseId(id);
            merchandiseService.saveMerchandise(merchandise);
            redirectAttributes.addFlashAttribute("successMessage", "Merchandise updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating merchandise: " + e.getMessage());
        }
        return "redirect:/admin/merchandise";
    }

    @GetMapping("/delete/{id}")
    public String deleteMerchandise(@PathVariable Long id, 
                                  HttpSession session,
                                  RedirectAttributes redirectAttributes) {
        if (!isAuthenticated(session)) {
            return "redirect:/login";
        }
        
        try {
            merchandiseService.deleteMerchandise(id);
            redirectAttributes.addFlashAttribute("successMessage", "Merchandise deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting merchandise: " + e.getMessage());
        }
        return "redirect:/admin/merchandise";
    }
}