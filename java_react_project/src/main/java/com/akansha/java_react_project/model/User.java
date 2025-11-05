package com.akansha.java_react_project.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    public enum Role {
        ADMIN("0"),
        STAFF("1"),
        USER("2");

        private final String code;
        Role(String code) { this.code = code; }
        public String getCode() { return code; }
    }

    public enum UserStatus {
        ACTIVE("0"), INACTIVE("1");

        private final String code;
        UserStatus(String code) { this.code = code; }
        public String getCode() { return code; }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String cipherPassword;

    @Column
    private Date passwordChangedAt;

    @Column
    private String currentSessionId;

    // ✅ Role is now stored as String ("0", "1", "2")
    @Column(name = "role")
    private String role;

    @Enumerated(EnumType.STRING)
    @Column
    private UserStatus userStatus = UserStatus.ACTIVE;

    @Column
    private Boolean isDeleted = false;

    @Column
    private Date deletedAt;

    // ✅ Relationships
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id")
    private List<RefreshToken> refreshTokens = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "update_master_id")
    private UpdateMaster updateMaster;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private UserDetail userDetail;

    // ------------------- Getters & Setters -------------------
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getCipherPassword() { return cipherPassword; }
    public void setCipherPassword(String cipherPassword) { this.cipherPassword = cipherPassword; }

    public Date getPasswordChangedAt() { return passwordChangedAt; }
    public void setPasswordChangedAt(Date passwordChangedAt) { this.passwordChangedAt = passwordChangedAt; }

    public String getCurrentSessionId() { return currentSessionId; }
    public void setCurrentSessionId(String currentSessionId) { this.currentSessionId = currentSessionId; }

    // ✅ Correct String-based getter/setter for role
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public UserStatus getUserStatus() { return userStatus; }
    public void setUserStatus(UserStatus userStatus) { this.userStatus = userStatus; }

    public Boolean getIsDeleted() { return isDeleted; }
    public void setIsDeleted(Boolean isDeleted) { this.isDeleted = isDeleted; }

    public Date getDeletedAt() { return deletedAt; }
    public void setDeletedAt(Date deletedAt) { this.deletedAt = deletedAt; }

    public List<RefreshToken> getRefreshTokens() { return refreshTokens; }
    public void setRefreshTokens(List<RefreshToken> refreshTokens) { this.refreshTokens = refreshTokens; }

    public UpdateMaster getUpdateMaster() { return updateMaster; }
    public void setUpdateMaster(UpdateMaster updateMaster) { this.updateMaster = updateMaster; }

    public UserDetail getUserDetail() { return userDetail; }
    public void setUserDetail(UserDetail userDetail) { this.userDetail = userDetail; }
}
