// package com.akansha.java_react_project.controller;

// import com.akansha.java_react_project.model.User;
// import com.akansha.java_react_project.model.UserDetail;
// import com.akansha.java_react_project.service.UserService;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import java.util.HashMap;
// import java.util.Map;

// @RestController
// @RequestMapping("/api/auth")
// public class AuthController {

//     private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
//     private final UserService userService;

//     public AuthController(UserService userService) {
//         this.userService = userService;
//     }

//     // ------------------- SIGNUP -------------------
//     @PostMapping("/signup")
//     public ResponseEntity<?> signup(@RequestBody SignupRequest request) {
//         logger.info("Signup request received for username: {}", request.getUsername());

//         try {
//             // Validate request
//             if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
//                 logger.error("Signup failed: Username is required");
//                 return buildErrorResponse("Username is required", HttpStatus.BAD_REQUEST);
//             }

//             if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
//                 logger.error("Signup failed: Password is required");
//                 return buildErrorResponse("Password is required", HttpStatus.BAD_REQUEST);
//             }

//             if (request.getRole() == null || request.getRole().trim().isEmpty()) {
//                 logger.error("Signup failed: Role is required");
//                 return buildErrorResponse("Role is required", HttpStatus.BAD_REQUEST);
//             }

//             User user = userService.signup(
//                     request.getUsername(),
//                     request.getPassword(),
//                     request.getRole(),
//                     request.getFullName(),
//                     request.getEmail(),
//                     request.getPhoneNumber(),
//                     request.getAddress()
//             );

//             UserDetail userDetail = user.getUserDetail();

//             Map<String, Object> response = new HashMap<>();
//             response.put("userId", user.getId());
//             response.put("fullName", userDetail.getFullName());
//             response.put("username", user.getUsername());
//             response.put("role", user.getRole().name());
//             response.put("email", userDetail.getEmail());
//             response.put("phoneNumber", userDetail.getPhoneNumber());
//             response.put("address", userDetail.getAddress());

//             logger.info("User successfully registered: {}", user.getUsername());
//             return ResponseEntity.status(HttpStatus.CREATED).body(response);

//         } catch (IllegalArgumentException e) {
//             logger.error("Signup validation error: {}", e.getMessage(), e);
//             return buildErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);

//         } catch (IllegalStateException e) {
//             logger.error("Signup state error (user might already exist): {}", e.getMessage(), e);
//             return buildErrorResponse(e.getMessage(), HttpStatus.CONFLICT);

//         } catch (Exception e) {
//             logger.error("Unexpected error during signup for username: {}",
//                         request.getUsername(), e);
//             return buildErrorResponse("An unexpected error occurred: " + e.getMessage(),
//                                      HttpStatus.INTERNAL_SERVER_ERROR);
//         }
//     }

//     // ------------------- LOGIN -------------------
//     @PostMapping("/login")
//     public ResponseEntity<?> login(@RequestBody LoginRequest request) {
//         logger.info("Login request received for username: {}", request.getUsername());

//         try {
//             // Validate request
//             if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
//                 logger.error("Login failed: Username is required");
//                 return buildErrorResponse("Username is required", HttpStatus.BAD_REQUEST);
//             }

//             if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
//                 logger.error("Login failed: Password is required");
//                 return buildErrorResponse("Password is required", HttpStatus.BAD_REQUEST);
//             }

//             if (request.getRole() == null || request.getRole().trim().isEmpty()) {
//                 logger.error("Login failed: Role is required");
//                 return buildErrorResponse("Role is required", HttpStatus.BAD_REQUEST);
//             }

//             String accessToken = userService.login(
//                     request.getUsername(),
//                     request.getPassword(),
//                     request.getRole()
//             );

//             Map<String, Object> response = new HashMap<>();
//             response.put("accessToken", accessToken);
//             response.put("username", request.getUsername());
//             response.put("role", request.getRole().toUpperCase());
//             response.put("message", "Logged in successfully");

//             logger.info("User successfully logged in: {}", request.getUsername());
//             return ResponseEntity.ok(response);

//         } catch (IllegalArgumentException e) {
//             logger.error("Login validation error for username {}: {}",
//                         request.getUsername(), e.getMessage(), e);
//             return buildErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);

//         } catch (SecurityException e) {
//             logger.error("Login authentication failed for username {}: {}",
//                         request.getUsername(), e.getMessage());
//             return buildErrorResponse(e.getMessage(), HttpStatus.UNAUTHORIZED);

//         } catch (Exception e) {
//             logger.error("Unexpected error during login for username: {}",
//                         request.getUsername(), e);
//             return buildErrorResponse("An unexpected error occurred: " + e.getMessage(),
//                                      HttpStatus.INTERNAL_SERVER_ERROR);
//         }
//     }

//     // ------------------- HELPER METHOD -------------------
//     private ResponseEntity<?> buildErrorResponse(String message, HttpStatus status) {
//         Map<String, String> error = new HashMap<>();
//         error.put("status", "error");
//         error.put("message", message);
//         return ResponseEntity.status(status).body(error);
//     }

//     // ------------------- REQUEST CLASSES -------------------
//     public static class SignupRequest {
//         private String fullName;
//         private String username;
//         private String password;
//         private String role;
//         private String email;
//         private String phoneNumber;
//         private String address;

//         public String getFullName() { return fullName; }
//         public void setFullName(String fullName) { this.fullName = fullName; }
//         public String getUsername() { return username; }
//         public void setUsername(String username) { this.username = username; }
//         public String getPassword() { return password; }
//         public void setPassword(String password) { this.password = password; }
//         public String getRole() { return role; }
//         public void setRole(String role) { this.role = role; }
//         public String getEmail() { return email; }
//         public void setEmail(String email) { this.email = email; }
//         public String getPhoneNumber() { return phoneNumber; }
//         public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
//         public String getAddress() { return address; }
//         public void setAddress(String address) { this.address = address; }
//     }

//     public static class LoginRequest {
//         private String username;
//         private String password;
//         private String role;

//         public String getUsername() { return username; }
//         public void setUsername(String username) { this.username = username; }
//         public String getPassword() { return password; }
//         public void setPassword(String password) { this.password = password; }
//         public String getRole() { return role; }
//         public void setRole(String role) { this.role = role; }
//     }
// }



package com.akansha.java_react_project.controller;

import com.akansha.java_react_project.model.User;
import com.akansha.java_react_project.model.UserDetail;
import com.akansha.java_react_project.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(
    originPatterns = "*",
    allowedHeaders = "*",
    exposedHeaders = "*",
    methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS},
    allowCredentials = "true",
    maxAge = 3600
)
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // ------------------- SIGNUP -------------------
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest request) {
        logger.info("Signup request received for username: {}", request.getUsername());

        try {
            // Validate request
            if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
                logger.error("Signup failed: Username is required");
                return buildErrorResponse("Username is required", HttpStatus.BAD_REQUEST);
            }

            if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
                logger.error("Signup failed: Password is required");
                return buildErrorResponse("Password is required", HttpStatus.BAD_REQUEST);
            }

            if (request.getRole() == null || request.getRole().trim().isEmpty()) {
                logger.error("Signup failed: Role is required");
                return buildErrorResponse("Role is required", HttpStatus.BAD_REQUEST);
            }

            User user = userService.signup(
                    request.getUsername(),
                    request.getPassword(),
                    request.getRole(),
                    request.getFullName(),
                    request.getEmail(),
                    request.getPhoneNumber(),
                    request.getAddress()
            );

            UserDetail userDetail = user.getUserDetail();

            Map<String, Object> response = new HashMap<>();
            response.put("userId", user.getId());
            response.put("fullName", userDetail.getFullName());
            response.put("username", user.getUsername());
response.put("role", user.getRole()); // ✅ FIXED            response.put("email", userDetail.getEmail());
            response.put("phoneNumber", userDetail.getPhoneNumber());
            response.put("address", userDetail.getAddress());

            logger.info("User successfully registered: {}", user.getUsername());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IllegalArgumentException e) {
            logger.error("Signup validation error: {}", e.getMessage(), e);
            return buildErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);

        } catch (IllegalStateException e) {
            logger.error("Signup state error (user might already exist): {}", e.getMessage(), e);
            return buildErrorResponse(e.getMessage(), HttpStatus.CONFLICT);

        } catch (Exception e) {
            logger.error("Unexpected error during signup for username: {}",
                        request.getUsername(), e);
            return buildErrorResponse("An unexpected error occurred: " + e.getMessage(),
                                     HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ------------------- LOGIN -------------------
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        logger.info("Login request received for username: {}", request.getUsername());

        try {
            // Validate request
            if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
                logger.error("Login failed: Username is required");
                return buildErrorResponse("Username is required", HttpStatus.BAD_REQUEST);
            }

            if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
                logger.error("Login failed: Password is required");
                return buildErrorResponse("Password is required", HttpStatus.BAD_REQUEST);
            }

            if (request.getRole() == null || request.getRole().trim().isEmpty()) {
                logger.error("Login failed: Role is required");
                return buildErrorResponse("Role is required", HttpStatus.BAD_REQUEST);
            }

            String accessToken = userService.login(
                    request.getUsername(),
                    request.getPassword(),
                    request.getRole()
            );

            Map<String, Object> response = new HashMap<>();
            response.put("accessToken", accessToken);
            response.put("username", request.getUsername());
            response.put("role", request.getRole().toUpperCase());
            response.put("message", "Logged in successfully");

            logger.info("User successfully logged in: {}", request.getUsername());
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            logger.error("Login validation error for username {}: {}",
                        request.getUsername(), e.getMessage(), e);
            return buildErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);

        } catch (SecurityException e) {
            logger.error("Login authentication failed for username {}: {}",
                        request.getUsername(), e.getMessage());
            return buildErrorResponse(e.getMessage(), HttpStatus.UNAUTHORIZED);

        } catch (Exception e) {
            logger.error("Unexpected error during login for username: {}",
                        request.getUsername(), e);
            return buildErrorResponse("An unexpected error occurred: " + e.getMessage(),
                                     HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ------------------- HELPER METHOD -------------------
    private ResponseEntity<?> buildErrorResponse(String message, HttpStatus status) {
        Map<String, String> error = new HashMap<>();
        error.put("status", "error");
        error.put("message", message);
        return ResponseEntity.status(status).body(error);
    }

    // ------------------- REQUEST CLASSES -------------------
    public static class SignupRequest {
        private String fullName;
        private String username;
        private String password;
        private String role;
        private String email;
        private String phoneNumber;
        private String address;

        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPhoneNumber() { return phoneNumber; }
        public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }
    }

    public static class LoginRequest {
        private String username;
        private String password;
        private String role;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
    }
}