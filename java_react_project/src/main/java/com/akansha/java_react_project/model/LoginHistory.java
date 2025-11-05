package com.akansha.java_react_project.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "login_history")
public class LoginHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime loginAt;
    private String userIP;
    private String userBrowser;
    private String deviceType;
    private String operatingSystem;
    private String loginStatus;
    private String sessionId;

    // Getters & Setters omitted for brevity
}
