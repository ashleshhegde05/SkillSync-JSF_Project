
package com.skillsync.repository;

import com.skillsync.entity.Resume;
import com.skillsync.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResumeRepository extends JpaRepository<Resume, Long> {

    Optional<Resume> findByUser(User user);

}
