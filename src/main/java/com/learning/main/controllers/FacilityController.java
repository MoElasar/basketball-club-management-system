package com.learning.main.controllers;

import com.learning.main.enm.FacilityType;
import com.learning.main.model.Facility;
import com.learning.main.model.User;
import com.learning.main.repository.services.FacilityService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/admin/facilities")
public class FacilityController {

    @Autowired
    private FacilityService facilityService;

    @GetMapping
    public String manageFacilities(Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return "redirect:/login";
        }

        List<Facility> facilities = facilityService.getAllFacilities();
        model.addAttribute("pageTitle", "Facility Management");
        model.addAttribute("facilities", facilities);
        model.addAttribute("newFacility", new Facility());
        model.addAttribute("facilityTypes", Arrays.asList(FacilityType.values()));
        model.addAttribute("currentUser", currentUser);
        return "admin/facilities";
    }

    @PostMapping("/add")
    public String addFacility(@ModelAttribute("newFacility") Facility facility, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return "redirect:/login";
        }
        facilityService.saveFacility(facility);
        return "redirect:/admin/facilities";
    }

    @GetMapping("/edit/{id}")
    public String showEditFacilityForm(@PathVariable Long id, Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return "redirect:/login";
        }

        Facility facility = facilityService.getFacilityById(id)
                .orElseThrow(() -> new RuntimeException("Facility not found"));
        model.addAttribute("pageTitle", "Edit Facility");
        model.addAttribute("facility", facility);
        model.addAttribute("facilityTypes", Arrays.asList(FacilityType.values()));
        model.addAttribute("currentUser", currentUser);
        return "admin/edit-facility";
    }

    @PostMapping("/update/{id}")
    public String updateFacility(@PathVariable Long id, @ModelAttribute("facility") Facility facilityDetails, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return "redirect:/login";
        }

        Facility facility = facilityService.getFacilityById(id)
                .orElseThrow(() -> new RuntimeException("Facility not found"));

        facility.setFacilityName(facilityDetails.getFacilityName());
        facility.setLocation(facilityDetails.getLocation());
        facility.setType(facilityDetails.getType());
        facility.setCapacity(facilityDetails.getCapacity());
        facility.setDescription(facilityDetails.getDescription());

        facilityService.saveFacility(facility);
        return "redirect:/admin/facilities";
    }

    @GetMapping("/delete/{id}")
    public String deleteFacility(@PathVariable Long id, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return "redirect:/login";
        }
        facilityService.deleteFacility(id);
        return "redirect:/admin/facilities";
    }
}