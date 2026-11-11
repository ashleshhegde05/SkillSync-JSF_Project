
package com.skillsync.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "resumes")
public class Resume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    private String fullName;
    private String email;
    private String phone;
    
private String professionalTitle;
private String location;

@Column(length = 2000)
private String careerObjective;

@Column(length = 3000)
private String educationDetails;

@Column(length = 3000)
private String internshipExperience;

@Column(length = 3000)
private String certificationsAchievements;

@Column(length = 2000)
private String additionalInformation;

    private String linkedIn;
    private String github;

    private String college;
    private String degree;
    private String branch;
    private String graduationYear;
    private String cgpa;

    @Column(length = 2000)
    private String skills;

    @Column(length = 3000)
    private String projects;

    @Column(length = 3000)
    private String experience;

    public Resume() {
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLinkedIn() {
        return linkedIn;
    }

    public void setLinkedIn(String linkedIn) {
        this.linkedIn = linkedIn;
    }

    public String getGithub() {
        return github;
    }

    public void setGithub(String github) {
        this.github = github;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getGraduationYear() {
        return graduationYear;
    }

    public void setGraduationYear(String graduationYear) {
        this.graduationYear = graduationYear;
    }

    public String getCgpa() {
        return cgpa;
    }

    public void setCgpa(String cgpa) {
        this.cgpa = cgpa;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getProjects() {
        return projects;
    }

    public void setProjects(String projects) {
        this.projects = projects;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }
    
// Professional Title
public String getProfessionalTitle() {
    return professionalTitle;
}

public void setProfessionalTitle(String professionalTitle) {
    this.professionalTitle = professionalTitle;
}

// Location
public String getLocation() {
    return location;
}

public void setLocation(String location) {
    this.location = location;
}

// Career Objective
public String getCareerObjective() {
    return careerObjective;
}

public void setCareerObjective(String careerObjective) {
    this.careerObjective = careerObjective;
}

// Education Details
public String getEducationDetails() {
    return educationDetails;
}

public void setEducationDetails(String educationDetails) {
    this.educationDetails = educationDetails;
}

// Internship Experience
public String getInternshipExperience() {
    return internshipExperience;
}

public void setInternshipExperience(String internshipExperience) {
    this.internshipExperience = internshipExperience;
}

// Certifications and Achievements
public String getCertificationsAchievements() {
    return certificationsAchievements;
}

public void setCertificationsAchievements(String certificationsAchievements) {
    this.certificationsAchievements = certificationsAchievements;
}

// Additional Information
public String getAdditionalInformation() {
    return additionalInformation;
}

public void setAdditionalInformation(String additionalInformation) {
    this.additionalInformation = additionalInformation;
}

}

