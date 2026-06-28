// src/main/java/com/learning/main/controllers/StaffsController.java
package com.learning.main.controllers;

import com.learning.main.enm.StaffRole;
import com.learning.main.model.Staff;
import com.learning.main.model.StaffContract;
import com.learning.main.model.Team;
import com.learning.main.model.User;
import com.learning.main.repository.services.StaffContractService;
import com.learning.main.repository.services.StaffService;
import com.learning.main.repository.services.TeamService;
import com.learning.main.repository.services.UserService;
import com.learning.main.service.FileStorageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/staff")
public class StaffsController {

	@Autowired
	private StaffService staffService;
	@Autowired
	private TeamService teamService;
	@Autowired
	private UserService userService;
	@Autowired
	private FileStorageService fileStorageService;

 
	
	
	@Autowired
    private StaffContractService staffContractService;
    
   

    private boolean isAuthenticated(HttpSession session) {
        return session.getAttribute("user") != null;
    }

    @GetMapping("/{staffId}")
    public String viewStaffContracts(@PathVariable Long staffId, Model model, HttpSession session) {
//        if (!isAuthenticated(session)) {
//            return "redirect:/login";
//        }
//        
        Optional<Staff> staff = staffService.getStaffById(staffId);
        if (staff.isEmpty()) {
            return "redirect:/admin/staff?error=staffNotFound";
        }
        
        List<StaffContract> contracts = staffContractService.findStaffContractsByStaff(staff.get());
        
        model.addAttribute("pageTitle", "Staff Contracts - " + staff.get().getFirstName() + " " + staff.get().getLastName());
        model.addAttribute("contracts", contracts);
        model.addAttribute("staff", staff.get());
        model.addAttribute("newContract", new StaffContract());
        return "admin/staff-contracts";
    }

    @PostMapping("/contracts/add/{staffId}")
    public String addStaffContract(@PathVariable Long staffId, 
                                 @ModelAttribute("newContract") StaffContract contract,
                                 Model model, HttpSession session) {
//        if (!isAuthenticated(session)) {
//            return "redirect:/login";
//        }
//        
        Optional<Staff> staff = staffService.getStaffById(staffId);
        if (staff.isEmpty()) {
            return "redirect:/admin/staff?error=staffNotFound";
        }
        
        contract.setStaff(staff.get());
        staffContractService.saveStaffContract(contract);
        
        return "redirect:/admin/staff/" + staffId + "?success=add";
    }

    @GetMapping("/contracts/edit/{contractId}")
    public String showEditContractForm(@PathVariable Long contractId, 
                                     Model model, HttpSession session) {
//        if (!isAuthenticated(session)) {
//            return "redirect:/login";
//        }
//        
        Optional<StaffContract> contract = staffContractService.getStaffContractById(contractId);
//        if (contract.isEmpty()) {
//            return "redirect:/admin/staff?error=contractNotFound";
//        }
        
        model.addAttribute("pageTitle", "Edit Contract");
        model.addAttribute("contract", contract.get());
        return "admin/edit-contract";
    }

    @PostMapping("/update/{contractId}")
    public String updateStaffContract(@PathVariable Long contractId,
                                    @ModelAttribute("contract") StaffContract contractDetails,
                                    Model model, HttpSession session) {
//        if (!isAuthenticated(session)) {
//            return "redirect:/login";
//        }
//        
        StaffContract existingContract = staffContractService.getStaffContractById(contractId)
                .orElseThrow(() -> new RuntimeException("Contract not found"));
        
        existingContract.setStartDate(contractDetails.getStartDate());
        existingContract.setEndDate(contractDetails.getEndDate());
        existingContract.setSalary(contractDetails.getSalary());
        existingContract.setBenefits(contractDetails.getBenefits());
        
        staffContractService.saveStaffContract(existingContract);
        
        return "redirect:/admin/staff/" + existingContract.getStaff().getStaffId() + "?success=update";
    }

    @GetMapping("/delete/{contractId}")
    public String deleteStaffContract(@PathVariable Long contractId, HttpSession session) {
//        if (!isAuthenticated(session)) {
//            return "redirect:/login";
//        }
//        
        Optional<StaffContract> contract = staffContractService.getStaffContractById(contractId);
        if (contract.isEmpty()) {
            return "redirect:/admin/staff?error=contractNotFound";
        }
        
        Long staffId = contract.get().getStaff().getStaffId();
        staffContractService.deleteStaffContract(contractId);
        
        return "redirect:/admin/staff/contracts/" + staffId + "?success=delete";
    }
	
	
	
	
	

	@GetMapping
	public String manageStaff(Model model, HttpSession session) {
		if (!isAuthenticated(session)) {
			return "redirect:/login";
		}
		User currentUser = (User) session.getAttribute("user");
		List<Staff> staffMembers = staffService.getAllStaff();
		List<Team> teams = teamService.getAllTeams();
		List<User> users = userService.getAllUsers();

		model.addAttribute("pageTitle", "Staff Management");
		model.addAttribute("staffMembers", staffMembers);
		model.addAttribute("newStaff", new Staff());
		model.addAttribute("teams", teams);
		model.addAttribute("staffRoles", StaffRole.values());
		model.addAttribute("users", users);
		model.addAttribute("currentUser", currentUser);
		return "admin/staff"; // This will be the main staff listing page (e.g., admin/staff.html)
	}

	@PostMapping("/add")
	public String addStaff(@ModelAttribute("newStaff") Staff staff, @RequestParam("teamId") Long teamId,
			@RequestParam(value = "userId", required = false) Long userId,
			@RequestParam(value = "profileImageFile", required = false) MultipartFile profileImageFile,
			HttpSession session, Model model) {

		if (!isAuthenticated(session)) {
			return "redirect:/login";
		}

		// Handle file upload
		if (profileImageFile != null && !profileImageFile.isEmpty()) {
			try {
				String fileName = fileStorageService.storeFile(profileImageFile);
				staff.setProfileImage("/api/files/" + fileName); // Store the URL path
			} catch (Exception e) {
				model.addAttribute("error", "Failed to upload profile image: " + e.getMessage());
				return showStaffFormWithError(model, session, staff);
			}
		}

		Optional<Team> teamOptional = teamService.getTeamById(teamId);
		if (teamOptional.isEmpty()) {
			model.addAttribute("error", "Team not found.");
			// Re-populate model for error display
			return showStaffFormWithError(model, session, staff);
		}
		staff.setTeam(teamOptional.get());

		if (userId != null) {
			Optional<User> userOptional = userService.getUserById(userId);
			userOptional.ifPresent(staff::setUser);
		} else {
			staff.setUser(null); // Allow un-associating user
		}

		// Set hire date if not already set
		if (staff.getHireDate() == null) {
			staff.setHireDate(LocalDate.now());
		}

		staffService.saveStaff(staff);
		return "redirect:/admin/staff?success=add";
	}

	@GetMapping("/edit/{id}")
	public String showEditStaffForm(@PathVariable Long id, Model model, HttpSession session) {
		if (!isAuthenticated(session)) {
			return "redirect:/login";
		}
		User currentUser = (User) session.getAttribute("user");
		Optional<Staff> staff = staffService.getStaffById(id);
		if (staff.isEmpty()) {
			return "redirect:/admin/staff?error=notFound";
		}
		model.addAttribute("pageTitle", "Edit Staff");
		model.addAttribute("staff", staff.get());
		model.addAttribute("teams", teamService.getAllTeams());
		model.addAttribute("staffRoles", StaffRole.values());
		model.addAttribute("users", userService.getAllUsers());
		model.addAttribute("currentUser", currentUser);
		return "admin/edit-staff"; // Form for editing an existing staff (e.g., admin/edit-staff.html)
	}

	@PostMapping("/update/{id}")
	public String updateStaff(@PathVariable Long id, @ModelAttribute("staff") Staff staffDetails,
			@RequestParam("teamId") Long teamId, @RequestParam(value = "userId", required = false) Long userId,
			@RequestParam(value = "profileImageFile", required = false) MultipartFile profileImageFile,
			@RequestParam(value = "removeImage", required = false) boolean removeImage, HttpSession session,
			Model model) {

		if (!isAuthenticated(session)) {
			return "redirect:/login";
		}

		Staff existingStaff = staffService.getStaffById(id)
				.orElseThrow(() -> new RuntimeException("Staff not found for this id :: " + id));

		// Handle image removal if checkbox is checked
		if (removeImage && existingStaff.getProfileImage() != null) {
			String oldFileName = existingStaff.getProfileImage().replace("/api/files/", "");
			fileStorageService.deleteFile(oldFileName);
			existingStaff.setProfileImage(null);
		}

		// Handle new file upload
		if (profileImageFile != null && !profileImageFile.isEmpty()) {
			try {
				// Delete old image if exists
				if (existingStaff.getProfileImage() != null) {
					String oldFileName = existingStaff.getProfileImage().replace("/api/files/", "");
					fileStorageService.deleteFile(oldFileName);
				}

				// Store new image
				String fileName = fileStorageService.storeFile(profileImageFile);
				existingStaff.setProfileImage("/api/files/" + fileName);
			} catch (Exception e) {
				model.addAttribute("error", "Failed to upload profile image: " + e.getMessage());
				return showEditFormWithError(model, session, existingStaff);
			}
		}

		Optional<Team> teamOptional = teamService.getTeamById(teamId);
		if (teamOptional.isEmpty()) {
			model.addAttribute("error", "Team not found.");
			return showEditFormWithError(model, session, existingStaff);
		}
		existingStaff.setTeam(teamOptional.get());

		if (userId != null) {
			Optional<User> userOptional = userService.getUserById(userId);
			existingStaff.setUser(userOptional.orElse(null)); // Set null if user not found
		} else {
			existingStaff.setUser(null); // Allow un-associating user
		}

		// Update staff details
		existingStaff.setFirstName(staffDetails.getFirstName());
		existingStaff.setLastName(staffDetails.getLastName());
		existingStaff.setRole(staffDetails.getRole());
		existingStaff.setHireDate(staffDetails.getHireDate());
		existingStaff.setSpecialization(staffDetails.getSpecialization());
		// Profile image is handled separately above

		staffService.saveStaff(existingStaff);
		return "redirect:/admin/staff?success=update";
	}

	@GetMapping("/delete/{id}")
	public String deleteStaff(@PathVariable Long id, HttpSession session) {
		if (!isAuthenticated(session)) {
			return "redirect:/login";
		}
		// Optionally, delete the profile image file before deleting the staff record
		Optional<Staff> staffOptional = staffService.getStaffById(id);
		if (staffOptional.isPresent()) {
			Staff staff = staffOptional.get();
			if (staff.getProfileImage() != null) {
				String fileName = staff.getProfileImage().replace("/api/files/", "");
				fileStorageService.deleteFile(fileName);
			}
		}
		staffService.deleteStaff(id);
		return "redirect:/admin/staff?success=delete";
	}

	private String showStaffFormWithError(Model model, HttpSession session, Staff staff) {
		User currentUser = (User) session.getAttribute("user");
		model.addAttribute("staffMembers", staffService.getAllStaff());
		model.addAttribute("teams", teamService.getAllTeams());
		model.addAttribute("staffRoles", StaffRole.values());
		model.addAttribute("users", userService.getAllUsers());
		model.addAttribute("newStaff", staff);
		model.addAttribute("pageTitle", "Staff Management");
		model.addAttribute("currentUser", currentUser);
		return "admin/staff";
	}

	private String showEditFormWithError(Model model, HttpSession session, Staff staff) {
		User currentUser = (User) session.getAttribute("user");
		model.addAttribute("staff", staff);
		model.addAttribute("teams", teamService.getAllTeams());
		model.addAttribute("staffRoles", StaffRole.values());
		model.addAttribute("users", userService.getAllUsers());
		model.addAttribute("pageTitle", "Edit Staff");
		model.addAttribute("currentUser", currentUser);
		return "admin/edit-staff";
	}
}