
package com.skillsync.controller;

import com.skillsync.entity.Skill;
import com.skillsync.entity.User;
import com.skillsync.repository.SkillRepository;
import com.skillsync.repository.UserRepository;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Controller
public class CareerController {

    private final UserRepository userRepository;
    private final SkillRepository skillRepository;

    public CareerController(
            UserRepository userRepository,
            SkillRepository skillRepository) {
        this.userRepository = userRepository;
        this.skillRepository = skillRepository;
    }

    @GetMapping("/career")
    public String showCareers(
            Authentication authentication,
            Model model) {

        User user = userRepository.findByEmail(
                authentication.getName()
        ).orElseThrow();

        List<Skill> studentSkills =
                skillRepository.findByUserOrderBySkillNameAsc(user);

        List<String> skillNames = studentSkills.stream()
                .map(skill -> skill.getSkillName()
                        .toLowerCase(Locale.ROOT))
                .toList();

        List<Map<String, Object>> careers = new ArrayList<>();

        // EXISTING CAREERS

        addCareer(careers, "Java Developer",
                "Build backend applications and enterprise software.",
                List.of("java", "sql", "spring boot"), skillNames);

        addCareer(careers, "Web Developer",
                "Create websites and interactive web applications.",
                List.of("html", "css", "javascript"), skillNames);

        addCareer(careers, "Data Analyst",
                "Analyze data and communicate useful insights.",
                List.of("python", "sql", "excel"), skillNames);

        addCareer(careers, "Cloud Engineer",
                "Work with cloud infrastructure and deployment.",
                List.of("aws", "azure", "linux"), skillNames);


        // NEW CSE CAREERS

        addCareer(careers, "Python Developer",
                "Develop applications, automation tools and backend systems using Python.",
                List.of("python", "django", "flask", "sql", "git"), skillNames);

        addCareer(careers, "Full Stack Developer",
                "Develop complete web applications including frontend and backend.",
                List.of("html", "css", "javascript", "react", "node.js", "mongodb"), skillNames);

        addCareer(careers, "Android Developer",
                "Create mobile applications for Android devices.",
                List.of("java", "kotlin", "android studio", "xml"), skillNames);

        addCareer(careers, "AI/ML Engineer",
                "Build intelligent systems using artificial intelligence and machine learning.",
                List.of("python", "machine learning", "numpy", "pandas", "tensorflow"), skillNames);

        addCareer(careers, "Cybersecurity Analyst",
                "Protect computer systems, networks and applications from security threats.",
                List.of("networking", "linux", "python", "ethical hacking"), skillNames);

        addCareer(careers, "DevOps Engineer",
                "Automate software development, testing and deployment processes.",
                List.of("linux", "git", "docker", "jenkins", "aws"), skillNames);

        addCareer(careers, "Database Administrator",
                "Manage, maintain and secure organizational databases.",
                List.of("sql", "mysql", "postgresql", "database management"), skillNames);

        addCareer(careers, "Software Engineer",
                "Design, develop, test and maintain software applications.",
                List.of("java", "python", "dsa", "oops", "git"), skillNames);

        addCareer(careers, "UI/UX Developer",
                "Design attractive and user-friendly website and application interfaces.",
                List.of("html", "css", "javascript", "figma"), skillNames);

        addCareer(careers, "Data Scientist",
                "Use programming, statistics and machine learning to understand complex data.",
                List.of("python", "statistics", "pandas", "numpy", "machine learning"), skillNames);

        addCareer(careers, "Backend Developer",
                "Develop server-side logic, APIs and database-driven applications.",
                List.of("java", "spring boot", "rest api", "sql", "git"), skillNames);

        addCareer(careers, "Blockchain Developer",
                "Develop decentralized applications and blockchain-based solutions.",
                List.of("solidity", "javascript", "ethereum", "web3"), skillNames);


        // SORT CAREERS BY MATCH PERCENTAGE

        careers.sort((a, b) ->
                Integer.compare(
                        (int) b.get("matchPercentage"),
                        (int) a.get("matchPercentage")
                )
        );

        model.addAttribute("fullName", user.getFullName());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("careers", careers);
        model.addAttribute("skillCount", studentSkills.size());

        return "career";
    }

    private void addCareer(
            List<Map<String, Object>> careers,
            String title,
            String description,
            List<String> requiredSkills,
            List<String> studentSkills) {

        List<String> matched = requiredSkills.stream()
                .filter(required -> studentSkills.stream()
                        .anyMatch(student -> student.contains(required)))
                .toList();

        List<String> missing = requiredSkills.stream()
                .filter(required -> !studentSkills.stream()
                        .anyMatch(student -> student.contains(required)))
                .toList();

        int percentage = Math.round(
                matched.size() * 100.0f / requiredSkills.size()
        );

        Map<String, Object> career = new LinkedHashMap<>();
        career.put("title", title);
        career.put("description", description);
        career.put("matchedSkills", matched);
        career.put("missingSkills", missing);
        career.put("matchPercentage", percentage);

        careers.add(career);
    }
}
