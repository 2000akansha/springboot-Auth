package com.akansha.java_react_project.repository;

import com.akansha.java_react_project.model.UserDetail;
import com.akansha.java_react_project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserDetailRepository extends JpaRepository<UserDetail, Long> {
    Optional<UserDetail> findByUser(User user);
}
