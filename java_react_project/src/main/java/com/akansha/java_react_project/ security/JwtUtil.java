// package com.akansha.java_react_project.security;

// import io.jsonwebtoken.*;
// import org.springframework.stereotype.Component;
// import java.util.Date;
// import org.springframework.beans.factory.annotation.Value; // ✅ ADD THIS IMPORT
// @Component
// public class JwtUtil {

//   @Value("${jwt.access-token-secret}")
//     private String ACCESS_TOKEN_SECRET;
//         private final long ACCESS_TOKEN_EXPIRES = 6 * 60 * 60 * 1000; // 6 hours

//     public String generateToken(Long userId, String role, String sessionId) {
//         return Jwts.builder()
//                 .claim("id", userId)
//                 .claim("role", role)
//                 .claim("sessionIdCurrent", sessionId)
//                 .setIssuedAt(new Date())
//                 .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRES))
// .signWith(SignatureAlgorithm.HS256, ACCESS_TOKEN_SECRET.getBytes())
//                 .compact();
//     }

//     public Claims validateToken(String token) throws JwtException {
//         return Jwts.parser()
//                 .setSigningKey(ACCESS_TOKEN_SECRET)
//                 .parseClaimsJws(token)
//                 .getBody();
//     }
// }
package com.akansha.java_react_project.security;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.access-token-secret}")
    private String ACCESS_TOKEN_SECRET;

    private final long ACCESS_TOKEN_EXPIRES = 6 * 60 * 60 * 1000; // 6 hours

    public String generateToken(Long userId, String role, String sessionId) {
        return Jwts.builder()
                .claim("id", userId)
                .claim("role", role)
                .claim("sessionIdCurrent", sessionId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRES))
                // 🔥 FIX: convert to bytes to avoid base64 decoding
                .signWith(SignatureAlgorithm.HS256, ACCESS_TOKEN_SECRET.getBytes())
                .compact();
    }

    public Claims validateToken(String token) throws JwtException {
        return Jwts.parser()
                // 🔥 FIX: again, use getBytes() for validation
                .setSigningKey(ACCESS_TOKEN_SECRET.getBytes())
                .parseClaimsJws(token)
                .getBody();
    }
}
