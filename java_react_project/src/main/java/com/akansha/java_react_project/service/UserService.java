package com.akansha.java_react_project.service;

import com.akansha.java_react_project.model.User;
import com.akansha.java_react_project.model.UserDetail;
import com.akansha.java_react_project.repository.UserDetailRepository;
import com.akansha.java_react_project.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserDetailRepository userDetailRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Value("${jwt.access-token-secret}")
    private String accessTokenSecret;

    @Value("${jwt.refresh-token-secret}")
    private String refreshTokenSecret;

    @Value("${jwt.access-token-expires}")
    private String accessTokenExpires;

    @Value("${jwt.refresh-token-expires}")
    private String refreshTokenExpires;

    public UserService(UserRepository userRepository, UserDetailRepository userDetailRepository) {
        this.userRepository = userRepository;
        this.userDetailRepository = userDetailRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    // ✅ Signup method
    @Transactional
    public User signup(String username, String password, String role,
                       String fullName, String email, String phoneNumber, String address) throws Exception {

        System.out.println("Signup called with username: " + username + ", role: " + role);

        // Check duplicate username
        Optional<User> existingUser = userRepository.findByUsername(username);
        if (existingUser.isPresent()) {
            System.out.println("Duplicate username detected: " + username);
            throw new Exception("Username already exists. Please use another one.");
        }

        // Password validation
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>]).{8,20}$";
        if (!Pattern.matches(passwordRegex, password)) {
            System.out.println("Password validation failed for username: " + username);
            throw new Exception("Password must contain uppercase, lowercase, number, special character, and be 8-20 chars long.");
        }
        System.out.println("Password validated successfully");

        // Hash password
        String hashedPassword = passwordEncoder.encode(password);
        System.out.println("Password hashed");

        // Validate and set role
        role = role.toUpperCase().trim();
        if (!role.equals("ADMIN") && !role.equals("STAFF") && !role.equals("USER")) {
            throw new Exception("Invalid role value: " + role + ". Allowed: ADMIN, STAFF, USER");
        }

        // ✅ Create User
        User user = new User();
        user.setUsername(username);
        user.setCipherPassword(hashedPassword);
        user.setRole(role); // ✅ store as string
        user.setUserStatus(User.UserStatus.ACTIVE);
        user.setIsDeleted(false);

        user = userRepository.save(user);
        System.out.println("User saved with ID: " + user.getId());

        // ✅ Create UserDetail
   // ✅ Create UserDetail
UserDetail userDetail = new UserDetail();
userDetail.setUser(user);
userDetail.setFullName(fullName);
userDetail.setEmail(email);
userDetail.setPhoneNumber(phoneNumber);
userDetail.setAddress(address);
userDetail.setUserCreatedBy(user);
userDetailRepository.save(userDetail);

// ✅ Link back to User object
user.setUserDetail(userDetail);   // <--- THIS LINE FIXES IT
userRepository.save(user);        // update the relation

System.out.println("UserDetail linked with User ID: " + user.getId());
return user;

    }

    // ✅ Login method
    public String login(String username, String password, String role) throws Exception {
        System.out.println("Login called with username: " + username + ", role: " + role);

        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            throw new Exception("Invalid username or password");
        }

        User user = userOpt.get();

        // Role validation
        if (!user.getRole().equalsIgnoreCase(role)) {
            System.out.println("Role mismatch. User role: " + user.getRole() + ", Request role: " + role);
            throw new Exception("Invalid role");
        }

        // Password check
        if (!passwordEncoder.matches(password, user.getCipherPassword())) {
            throw new Exception("Invalid username or password");
        }

        // Generate Session ID
        String sessionIdCurrent = UUID.randomUUID().toString();
        user.setCurrentSessionId(sessionIdCurrent);
        userRepository.save(user);
        System.out.println("Session ID updated for user: " + username + " -> " + sessionIdCurrent);

        // Token creation
        Date now = new Date();
        Date accessExp = new Date(now.getTime() + parseDurationToMillis(accessTokenExpires));
        Date refreshExp = new Date(now.getTime() + parseDurationToMillis(refreshTokenExpires));

        String accessToken = Jwts.builder()
                .setSubject(user.getId().toString())
                .claim("role", user.getRole())
                .claim("sessionIdCurrent", sessionIdCurrent)
                .setIssuedAt(now)
                .setExpiration(accessExp)
                .signWith(SignatureAlgorithm.HS256, accessTokenSecret)
                .compact();

        System.out.println("Access token generated successfully for " + username);
        return accessToken;
    }

    // ✅ Helper to convert "6h" → milliseconds
    private long parseDurationToMillis(String duration) {
        if (duration.endsWith("h")) {
            long hours = Long.parseLong(duration.replace("h", ""));
            return hours * 60 * 60 * 1000;
        }
        if (duration.endsWith("m")) {
            long minutes = Long.parseLong(duration.replace("m", ""));
            return minutes * 60 * 1000;
        }
        if (duration.endsWith("s")) {
            long seconds = Long.parseLong(duration.replace("s", ""));
            return seconds * 1000;
        }
        return 0;
    }
}
