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
  // This secret signs and verifies JWTs. If the token was not signed with this
  // same secret, JJWT will reject it while parsing.
  private final String secretKey;

  // Token lifetime in milliseconds. Default is 3600000 ms = 1 hour.
  private final long expirationMs;

  public JwtUtil(
      @Value("${app.jwt.secret:${JWT_SECRET:TaK+HaV^uvCHEFsEVfypW#7g9^k*Z8$V}}") String secretKey,
      @Value("${app.jwt.expiration-ms:${JWT_EXPIRATION_MS:3600000}}") long expirationMs) {
    // @Value comes from Spring. It reads app.jwt.* from application.properties,
    // then environment variables, then the hard-coded default fallback.
    this.secretKey = secretKey;
    this.expirationMs = expirationMs;
  }

  private SecretKey getSigningKey() {
    // Keys.hmacShaKeyFor comes from the JJWT library. It converts our secret
    // text into a SecretKey suitable for HMAC signing.
    return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
  }

  public String extractUsername(String token) {
    // In JWT language, "subject" is the main identity. We store user email here.
    Claims claims = extractAllClaims(token);
    return claims.getSubject();
  }

  public Date extractExpiration(String token) {
    // Expiration is the "exp" claim. After this time, validateToken returns false.
    return extractAllClaims(token).getExpiration();
  }

  private Claims extractAllClaims(String token) {
    // Jwts.parserBuilder() is from the JJWT library.
    // parseClaimsJws verifies the signature first, then returns the token body.
    // If signature, format, or expiration is invalid, it throws JwtException.
    return Jwts.parserBuilder()
        .setSigningKey(getSigningKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  private Boolean isTokenExpired(String token) {
    // A token is expired when its exp time is before the current server time.
    return extractExpiration(token).before(new Date());
  }

  public String generateToken(String username, Long userId,
      java.util.Collection<? extends org.springframework.security.core.GrantedAuthority> authorities) {
    // Claims are custom data stored inside the JWT payload.
    // Spring Security authorities already include the ROLE_ prefix, for example:
    // ROLE_USER, ROLE_ADMIN, ROLE_RECRUITER.
    Map<String, Object> claims = new HashMap<>();
    claims.put("role", authorities.stream().map(Object::toString).toList());
    claims.put("id", userId);
    return createToken(claims, username);
  }

  private String createToken(Map<String, Object> claims, String subject) {
    // JWT structure is: header.payload.signature.
    // Header: token type/signing algorithm metadata.
    // Payload: subject, roles, id, issued time, expiration.
    // Signature: proves payload was created by the backend and was not changed.
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
      // Parsing already verifies the signature. This method also checks expiry.
      return !isTokenExpired(token);
    } catch (JwtException | IllegalArgumentException ex) {
      // JwtException covers invalid signature, malformed token, expired token,
      // unsupported token, and similar JJWT parsing failures.
      log.debug("JWT validation failed: {}", ex.getMessage());
      return false;
    }
  }

  public java.util.List<String> extractRoles(String token) {
    // The JWT "role" claim is stored as a JSON array, so JJWT gives it back as
    // a List. These strings later become SimpleGrantedAuthority objects.
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
