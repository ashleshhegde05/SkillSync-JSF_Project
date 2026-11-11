
package com.skillsync.controller;

import com.skillsync.entity.Skill;
import com.skillsync.entity.User;
import com.skillsync.repository.SkillRepository;
import com.skillsync.repository.UserRepository;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class SkillController {

    private final SkillRepository skillRepository;
    private final UserRepository userRepository;

    public SkillController(
            SkillRepository skillRepository,
            UserRepository userRepository) {
        this.skillRepository = skillRepository;
        this.userRepository = userRepository;
    }

    private User getLoggedInUser(Authentication authentication) {
        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() ->
                        new IllegalStateException("Logged-in user not found"));
    }

    // Display the logged-in student's skills
    @GetMapping("/skills")
    public String showSkills(
            Authentication authentication,
            Model model) {

        User user = getLoggedInUser(authentication);

        List<Skill> skills =
                skillRepository.findByUserOrderBySkillNameAsc(user);

        model.addAttribute("skills", skills);
        model.addAttribute("skillCount", skills.size());
        model.addAttribute("fullName", user.getFullName());
        model.addAttribute("email", user.getEmail());

        return "skills";
    }

    // Add a new skill
    @PostMapping("/skills/add")
    public String addSkill(
            @RequestParam("skillName") String skillName,
            @RequestParam("proficiency") String proficiency,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        String normalizedName = skillName.trim();

        if (normalizedName.isBlank() || normalizedName.length() > 100) {
            redirectAttributes.addFlashAttribute(
                    "error", "Enter a skill name between 1 and 100 characters.");
            return "redirect:/skills";
        }

        List<String> allowedLevels =
                List.of("Beginner", "Intermediate", "Advanced");

        if (!allowedLevels.contains(proficiency)) {
            redirectAttributes.addFlashAttribute(
                    "error", "Please select a valid proficiency level.");
            return "redirect:/skills";
        }

        User user = getLoggedInUser(authentication);

        Skill skill = new Skill();
        skill.setSkillName(normalizedName);
        skill.setProficiency(proficiency);
        skill.setUser(user);

        skillRepository.save(skill);

        redirectAttributes.addFlashAttribute(
                "success", "Skill added successfully!");

        return "redirect:/skills";
    }

    // Delete a skill owned by the logged-in student
    @PostMapping("/skills/delete/{id}")
    public String deleteSkill(
            @PathVariable Long id,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        User user = getLoggedInUser(authentication);

        skillRepository.findByIdAndUser(id, user).ifPresentOrElse(
                skillRepository::delete,
                () -> redirectAttributes.addFlashAttribute(
                        "error", "Skill not found in your profile.")
        );

        if (!redirectAttributes.getFlashAttributes().containsKey("error")) {
            redirectAttributes.addFlashAttribute(
                    "success", "Skill deleted successfully.");
        }

        return "redirect:/skills";
    }
}
