package com.akansha.java_react_project.repository;

import com.akansha.java_react_project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // JPA automatically implements these methods
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
}
