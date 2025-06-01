package org.example.utils;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.example.dtos.AuthenticatedUserDto;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

public class JwtUtil {
    private static final Key key = Keys.hmacShaKeyFor(System.getenv("SECRET_KEY").getBytes(StandardCharsets.UTF_8));
    public static String generateToken(Integer id, String username, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .claim("userId", id)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() +3600_000))
                .signWith(key, SignatureAlgorithm.HS256).compact();
    }
    public static AuthenticatedUserDto decodeToken(String token){
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        return new AuthenticatedUserDto(claims.get("userId", Integer.class), claims.getSubject(), claims.get("role", String.class));
    }
}
