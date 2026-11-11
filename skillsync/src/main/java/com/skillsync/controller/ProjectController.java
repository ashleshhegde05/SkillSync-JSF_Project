
package com.skillsync.controller;

import com.skillsync.entity.Project;
import com.skillsync.entity.User;
import com.skillsync.repository.ProjectRepository;
import com.skillsync.repository.UserRepository;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class ProjectController {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public ProjectController(
            ProjectRepository projectRepository,
            UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    // Display the logged-in student's projects
    @GetMapping("/projects")
    public String showProjects(
            Authentication authentication,
            Model model) {

        User user = getLoggedInUser(authentication);

        List<Project> projects =
                projectRepository.findByUserOrderByCreatedAtDesc(user);

        model.addAttribute("projects", projects);
        model.addAttribute("projectCount", projects.size());
        model.addAttribute("fullName", user.getFullName());
        model.addAttribute("email", user.getEmail());

        return "projects";
    }

    // Add a new project
    @PostMapping("/projects/add")
    public String addProject(
            Authentication authentication,
            @RequestParam String projectName,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String technologies,
            @RequestParam(required = false) String projectLink,
            RedirectAttributes redirectAttributes) {

        User user = getLoggedInUser(authentication);

        if (projectName == null || projectName.isBlank()
                || projectName.trim().length() > 150) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Enter a project name (maximum 150 characters).");
            return "redirect:/projects";
        }

        if (description != null && description.length() > 1000
                || technologies != null && technologies.length() > 300
                || projectLink != null && projectLink.length() > 500) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "One or more project fields exceed the allowed length.");
            return "redirect:/projects";
        }

        if (projectLink != null && !projectLink.isBlank()
                && !projectLink.trim().matches("^https?://.+")) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Project links must start with http:// or https://.");
            return "redirect:/projects";
        }

        Project project = new Project();
        project.setProjectName(projectName.trim());
        project.setDescription(clean(description));
        project.setTechnologies(clean(technologies));
        project.setProjectLink(clean(projectLink));
        project.setUser(user);

        projectRepository.save(project);

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Project added successfully!");

        return "redirect:/projects";
    }

    // Delete a project owned by the logged-in student
    @PostMapping("/projects/delete/{id}")
    public String deleteProject(
            @PathVariable Long id,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        User user = getLoggedInUser(authentication);

        Project project = projectRepository
                .findByIdAndUser(id, user)
                .orElse(null);

        if (project == null) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Project not found or you do not have permission to delete it.");
            return "redirect:/projects";
        }

        projectRepository.delete(project);

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Project deleted successfully!");

        return "redirect:/projects";
    }

    private User getLoggedInUser(Authentication authentication) {
        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() ->
                        new IllegalStateException("Logged-in user not found"));
    }

    private String clean(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}
