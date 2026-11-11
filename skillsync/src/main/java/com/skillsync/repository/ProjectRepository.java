
package com.skillsync.repository;

import com.skillsync.entity.Project;
import com.skillsync.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    // Get only the logged-in student's projects
    List<Project> findByUserOrderByCreatedAtDesc(User user);

    // Find a project only if it belongs to the given student
    Optional<Project> findByIdAndUser(Long id, User user);
}
