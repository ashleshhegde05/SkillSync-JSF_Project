
package com.skillsync.repository;

import com.skillsync.entity.Skill;
import com.skillsync.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SkillRepository extends JpaRepository<Skill, Long> {

    List<Skill> findByUserOrderBySkillNameAsc(User user);

    Optional<Skill> findByIdAndUser(Long id, User user);
}
