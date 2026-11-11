
package com.skillsync.controller;

import com.skillsync.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Locale;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // Open the registration page
    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";
    }

    // Process the registration form
    @PostMapping("/register")
    public String registerUser(
            @RequestParam("fullName") String fullName,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (fullName.isBlank() || email.isBlank() || password.isBlank()) {
            model.addAttribute("error", "Please fill in all fields.");
            return "register";
        }

        // Normalize email
        String normalizedEmail = email.trim().toLowerCase(Locale.ROOT);

        // Allow only NMAMIT email addresses
        if (!normalizedEmail.matches("^[^@\\s]+@nmamit\\.in$")) {
            model.addAttribute(
                    "error",
                    "Only NMAMIT college email addresses (@nmamit.in) are allowed."
            );
            return "register";
        }

        if (password.length() < 8) {
            model.addAttribute(
                    "error", "Password must contain at least 8 characters.");
            return "register";
        }

        try {
            userService.registerUser(fullName, normalizedEmail, password);

            redirectAttributes.addFlashAttribute(
                    "success", "Account created successfully! Please log in.");

            return "redirect:/login";

        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }

    // Open the login page
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }
}
