
package com.skillsync.controller;

import com.skillsync.entity.User;
import com.skillsync.repository.UserRepository;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final UserRepository userRepository;

    public DashboardController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/dashboard")
    public String showDashboard(
            Authentication authentication,
            Model model) {

        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() ->
                        new IllegalStateException("Logged-in user not found"));

        model.addAttribute("fullName", user.getFullName());
        model.addAttribute("email", user.getEmail());

        return "dashboard";
    }
}
