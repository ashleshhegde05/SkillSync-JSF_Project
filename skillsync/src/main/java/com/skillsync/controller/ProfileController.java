
package com.skillsync.controller;

import com.skillsync.entity.User;
import com.skillsync.repository.UserRepository;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProfileController {

    private final UserRepository userRepository;

    public ProfileController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Display the logged-in student's profile
    @GetMapping("/profile")
    public String showProfile(Authentication authentication, Model model) {

        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() ->
                        new IllegalStateException("Logged-in user not found"));

        model.addAttribute("fullName", user.getFullName());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("phoneNumber", user.getPhoneNumber());
        model.addAttribute("college", user.getCollege());
        model.addAttribute("degree", user.getDegree());
        model.addAttribute("branch", user.getBranch());

        return "profile";
    }

    // Save updated profile details
    @PostMapping("/profile/update")
    public String updateProfile(
            Authentication authentication,
            @RequestParam String fullName,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) String college,
            @RequestParam(required = false) String degree,
            @RequestParam(required = false) String branch,
            RedirectAttributes redirectAttributes) {

        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() ->
                        new IllegalStateException("Logged-in user not found"));

        if (fullName == null || fullName.trim().isEmpty()
                || fullName.trim().length() > 100) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage", "Enter a valid name (maximum 100 characters).");
            return "redirect:/profile";
        }

        if (phoneNumber != null && !phoneNumber.isBlank()
                && !phoneNumber.trim().matches("[0-9+() -]{7,15}")) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage", "Enter a valid phone number.");
            return "redirect:/profile";
        }

        if (college != null && college.length() > 150
                || degree != null && degree.length() > 100
                || branch != null && branch.length() > 100) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage", "One or more profile fields are too long.");
            return "redirect:/profile";
        }

        user.setFullName(fullName.trim());
        user.setPhoneNumber(clean(phoneNumber));
        user.setCollege(clean(college));
        user.setDegree(clean(degree));
        user.setBranch(clean(branch));

        userRepository.save(user);

        redirectAttributes.addFlashAttribute(
                "successMessage", "Your profile has been updated successfully!");

        return "redirect:/profile";
    }

    private String clean(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}
