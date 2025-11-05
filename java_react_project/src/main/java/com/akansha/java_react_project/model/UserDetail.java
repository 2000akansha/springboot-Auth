package com.akansha.java_react_project.model;

import jakarta.persistence.*;

@Entity
@Table(name = "user_details")
public class UserDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    private String fullName;
    private String address;
    private String email;
    private String phoneNumber;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User userCreatedBy;

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public User getUserCreatedBy() { return userCreatedBy; }
    public void setUserCreatedBy(User userCreatedBy) { this.userCreatedBy = userCreatedBy; }
}
