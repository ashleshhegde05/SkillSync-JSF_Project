
package com.skillsync.controller;

import com.skillsync.entity.Resume;
import com.skillsync.entity.User;
import com.skillsync.repository.ResumeRepository;
import com.skillsync.repository.UserRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

@Controller
public class ResumeController {

    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;

    public ResumeController(ResumeRepository resumeRepository,
                            UserRepository userRepository) {
        this.resumeRepository = resumeRepository;
        this.userRepository = userRepository;
    }

    private User getLoggedInUser(Authentication authentication) {
        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // Open Resume Builder
    @GetMapping("/resume")
    public String showResumeBuilder(Authentication authentication, Model model) {

        User user = getLoggedInUser(authentication);

        Resume resume = resumeRepository.findByUser(user)
                .orElse(new Resume());

        resume.setFullName(user.getFullName());
        resume.setEmail(user.getEmail());

        model.addAttribute("resume", resume);
        model.addAttribute("fullName", user.getFullName());

        return "resume-builder";
    }

    // Save Resume
    @PostMapping("/resume/save")
    public String saveResume(@ModelAttribute Resume resume,
                             Authentication authentication) {

        User user = getLoggedInUser(authentication);

        Resume existingResume = resumeRepository.findByUser(user)
                .orElse(new Resume());

        existingResume.setUser(user);
        existingResume.setFullName(resume.getFullName());
        existingResume.setEmail(resume.getEmail());
        existingResume.setPhone(resume.getPhone());
        existingResume.setLinkedIn(resume.getLinkedIn());
        existingResume.setGithub(resume.getGithub());

        existingResume.setCollege(resume.getCollege());
        existingResume.setDegree(resume.getDegree());
        existingResume.setBranch(resume.getBranch());
        existingResume.setGraduationYear(resume.getGraduationYear());
        existingResume.setCgpa(resume.getCgpa());

        existingResume.setSkills(resume.getSkills());
        existingResume.setProjects(resume.getProjects());
        existingResume.setExperience(resume.getExperience());

        
existingResume.setProfessionalTitle(resume.getProfessionalTitle());
existingResume.setLocation(resume.getLocation());

existingResume.setCareerObjective(resume.getCareerObjective());
existingResume.setEducationDetails(resume.getEducationDetails());

existingResume.setInternshipExperience(resume.getInternshipExperience());

existingResume.setCertificationsAchievements(
        resume.getCertificationsAchievements());

existingResume.setAdditionalInformation(
        resume.getAdditionalInformation());


        resumeRepository.save(existingResume);

        return "redirect:/resume/preview";
    }
    
    // Preview Resume
    @GetMapping("/resume/preview")
    public String previewResume(Authentication authentication, Model model) {

        User user = getLoggedInUser(authentication);

        Resume resume = resumeRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Resume not found"));

        model.addAttribute("resume", resume);
        model.addAttribute("fullName", user.getFullName());

        return "resume-preview";
    }

}
