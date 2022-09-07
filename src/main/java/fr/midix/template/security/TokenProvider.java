package fr.midix.template.security;

import java.util.Base64;
import java.util.Date;
import java.util.UUID;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class TokenProvider {

  private final String secretKey;

  private final long validityInMilliseconds;

  private final UserDetailsService userService;

  public TokenProvider(UserDetailsService userService) {
    this.secretKey = Base64.getEncoder().encodeToString("echo-orthos%S3cr3t".getBytes());
    this.validityInMilliseconds = 2592000;
    this.userService = userService;
  }

  public String createToken(String username) {
    return this.createToken(username, Long.valueOf(7 * 24 * 60 * 60 * 1000L));
  }

  public String createToken(String username, boolean withValidity) {
    return this.createToken(username, withValidity ? 1000 * this.validityInMilliseconds : null);
  }

  public String createToken(String username, Long tokenValidityInMilliseconds) {
    Date now = new Date();
    return Jwts.builder().setId(UUID.randomUUID().toString()).setSubject(username).setIssuedAt(now)
        .signWith(SignatureAlgorithm.HS512, this.secretKey)
        .setExpiration(tokenValidityInMilliseconds == null ? null
            : new Date(now.getTime() + tokenValidityInMilliseconds))
        .compact();
  }

  public Authentication getAuthentication(String token) {
    String username = Jwts.parser().setSigningKey(this.secretKey).parseClaimsJws(token).getBody().getSubject();
    UserDetails userDetails = this.userService.loadUserByUsername(username);

    return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
  }
}
