
package com.skillsync.controller;

import com.skillsync.entity.User;
import com.skillsync.repository.UserRepository;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LearningController {

    private final UserRepository userRepository;

    public LearningController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/learning")
    public String learning(Authentication authentication, Model model) {

        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        model.addAttribute("fullName", user.getFullName());
        model.addAttribute("email", user.getEmail());

        return "learning";
    }
}
