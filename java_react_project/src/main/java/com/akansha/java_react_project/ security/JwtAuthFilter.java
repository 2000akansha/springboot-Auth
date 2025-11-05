// package com.akansha.java_react_project.security;

// import com.akansha.java_react_project.model.User;
// import com.akansha.java_react_project.repository.UserRepository;
// import io.jsonwebtoken.Claims;
// import io.jsonwebtoken.ExpiredJwtException;
// import jakarta.servlet.FilterChain;
// import jakarta.servlet.ServletException;
// import jakarta.servlet.http.Cookie;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Component;
// import org.springframework.web.filter.OncePerRequestFilter;

// import java.io.IOException;

// @Component
// public class JwtAuthFilter extends OncePerRequestFilter {

//     @Autowired
//     private UserRepository userRepository;

//     private static final String ACCESS_SECRET = "ACCESS_SECRET_EXAMPLE";

//     @Override
//     protected void doFilterInternal(HttpServletRequest request,
//                                     HttpServletResponse response,
//                                     FilterChain filterChain)
//             throws ServletException, IOException {

//         String path = request.getRequestURI();

//         // Skip JWT check for public endpoints
//         if (path.startsWith("/api/auth/")) {
//             filterChain.doFilter(request, response);
//             return;
//         }

//         try {
//             String token = null;

//             // ✅ Read token from cookies
//             if (request.getCookies() != null) {
//                 for (Cookie cookie : request.getCookies()) {
//                     if ("accessToken".equals(cookie.getName())) {
//                         token = cookie.getValue();
//                         break;
//                     }
//                 }
//             }

//             if (token == null) {
//                 response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                 response.getWriter().write("Authorization token required");
//                 return;
//             }

//             // ✅ Parse the JWT token
//             Claims claims = io.jsonwebtoken.Jwts.parser()
//                     .setSigningKey(ACCESS_SECRET)
//                     .parseClaimsJws(token)
//                     .getBody();

//             Long userId = Long.parseLong(claims.getSubject());
//             String sessionIdCurrent = claims.get("sessionIdCurrent", String.class);

//             // ✅ Fetch user from DB
//             User user = userRepository.findById(userId)
//                     .orElseThrow(() -> new RuntimeException("User not found"));

//             // ✅ Check if password changed after token issued
//             if (user.getPasswordChangedAt() != null) {
//                 long passwordChangedAt = user.getPasswordChangedAt().toInstant().getEpochSecond(); // 🔥 FIXED HERE
//                 long tokenIssuedAt = claims.getIssuedAt().toInstant().getEpochSecond();

//                 if (tokenIssuedAt < passwordChangedAt) {
//                     response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                     response.getWriter().write("Session expired. Please log in again.");
//                     return;
//                 }
//             }

//             // ✅ Check single session validity
//             if (!sessionIdCurrent.equals(user.getCurrentSessionId())) {
//                 response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                 response.getWriter().write("Logged out due to login elsewhere");
//                 return;
//             }

//             // ✅ Attach user info to request for downstream usage
//             request.setAttribute("userId", user.getId());
//             request.setAttribute("userRole", user.getRole());
//             request.setAttribute("username", user.getUsername());

//             filterChain.doFilter(request, response);

//         } catch (ExpiredJwtException e) {
//             response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//             response.getWriter().write("Access token expired");
//         } catch (Exception e) {
//             response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//             response.getWriter().write("Unauthorized: " + e.getMessage());
//         }
//     }
// }
package com.akansha.java_react_project.security;

import com.akansha.java_react_project.model.User;
import com.akansha.java_react_project.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private UserRepository userRepository;

    @Value("${jwt.access-token-secret}")
    private String ACCESS_SECRET; // ✅ from application.properties

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        if (path.startsWith("/api/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = null;

            if (request.getCookies() != null) {
                for (Cookie cookie : request.getCookies()) {
                    if ("accessToken".equals(cookie.getName())) {
                        token = cookie.getValue();
                        break;
                    }
                }
            }

            if (token == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Authorization token required");
                return;
            }

            // ✅ FIXED: using getBytes()
            Claims claims = io.jsonwebtoken.Jwts.parser()
                    .setSigningKey(ACCESS_SECRET.getBytes())
                    .parseClaimsJws(token)
                    .getBody();

            Long userId = Long.parseLong(claims.get("id").toString());
            String sessionIdCurrent = claims.get("sessionIdCurrent", String.class);

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (user.getPasswordChangedAt() != null) {
                long passwordChangedAt = user.getPasswordChangedAt().toInstant().getEpochSecond();
                long tokenIssuedAt = claims.getIssuedAt().toInstant().getEpochSecond();

                if (tokenIssuedAt < passwordChangedAt) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("Session expired. Please log in again.");
                    return;
                }
            }

            if (!sessionIdCurrent.equals(user.getCurrentSessionId())) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Logged out due to login elsewhere");
                return;
            }

            request.setAttribute("userId", user.getId());
            request.setAttribute("userRole", user.getRole());
            request.setAttribute("username", user.getUsername());

            filterChain.doFilter(request, response);

        } catch (ExpiredJwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Access token expired");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized: " + e.getMessage());
        }
    }
}
