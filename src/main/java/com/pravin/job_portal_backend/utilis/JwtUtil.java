package com.pravin.job_portal_backend.utilis;

import java.util.Date;
import java.util.HashMap;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtUtil {
  private final String secretKey;
  private final long expirationMs;

  public JwtUtil(
      @Value("${app.jwt.secret:${JWT_SECRET:TaK+HaV^uvCHEFsEVfypW#7g9^k*Z8$V}}") String secretKey,
      @Value("${app.jwt.expiration-ms:${JWT_EXPIRATION_MS:3600000}}") long expirationMs) {
    this.secretKey = secretKey;
    this.expirationMs = expirationMs;
  }

  private SecretKey getSigningKey() {
    return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
  }

  public String extractUsername(String token) {
    Claims claims = extractAllClaims(token);
    return claims.getSubject();
  }

  public Date extractExpiration(String token) {
    return extractAllClaims(token).getExpiration();
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(getSigningKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  private Boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  public String generateToken(String username, Long userId,
      java.util.Collection<? extends org.springframework.security.core.GrantedAuthority> authorities) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("role", authorities.stream().map(Object::toString).toList());
    claims.put("id", userId); // Add userId to JWT claims
    return createToken(claims, username);
  }

  private String createToken(Map<String, Object> claims, String subject) {
    return Jwts.builder()
        .setClaims(claims)
        .setSubject(subject)
        .setHeaderParam("typ", "JWT")
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
        .signWith(getSigningKey())
        .compact();
  }

  public Boolean validateToken(String token) {
    try {
      return !isTokenExpired(token);
    } catch (JwtException | IllegalArgumentException ex) {
      log.debug("JWT validation failed: {}", ex.getMessage());
      return false;
    }
  }

  // Extract roles from JWT (as a list of strings)
  public java.util.List<String> extractRoles(String token) {
    Claims claims = extractAllClaims(token);
    Object rolesObj = claims.get("role");
    if (rolesObj instanceof java.util.List<?> rawList) {
      java.util.List<String> roles = new java.util.ArrayList<>();
      for (Object o : rawList) {
        if (o != null) roles.add(o.toString());
      }
      return roles;
    }
    return java.util.Collections.emptyList();
  }
}
