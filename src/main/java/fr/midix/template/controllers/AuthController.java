package fr.midix.template.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import fr.midix.template.entities.User;
import fr.midix.template.resources.UserResource;
import fr.midix.template.resources.creation.UserCreationResource;
import fr.midix.template.security.TokenProvider;
import fr.midix.template.services.EmailService;
import fr.midix.template.services.UserService;
import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

  private final TokenProvider tokenProvider;

  private final AuthenticationManager authenticationManager;
  private final PasswordEncoder passwordEncoder;

  private final UserService userService;
  private final EmailService emailService;

  @PostMapping("/signup")
  public ResponseEntity<UserResource> signup(@RequestBody UserCreationResource resource) {
    resource.setEmail(resource.getEmail().toLowerCase().trim());
    resource.setPassword(resource.getPassword().trim());

    User user = this.userService.create(resource);

    this.emailService.sendRegistrationEmail(user);

    UserResource result = UserResource.from(user);
    result.setToken(this.tokenProvider.createToken(user.getId()));

    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping("/signin")
  public ResponseEntity<UserResource> login(@RequestBody UserResource resource) {
    resource.setEmail(resource.getEmail().toLowerCase().trim());
    resource.setPassword(resource.getPassword().trim());

    User user = this.userService.findByEmail(resource.getEmail());
    if (!this.passwordEncoder.matches(resource.getPassword(), user.getPassword())) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "USER_NOT_FOUND");
    }

    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getId(),
        resource.getPassword(), List.of());

    try {
      this.authenticationManager.authenticate(authenticationToken);

      UserResource result = UserResource.from(user);
      result.setToken(this.tokenProvider.createToken(user.getId()));

      return new ResponseEntity<>(result, HttpStatus.OK);
    } catch (AuthenticationException e) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

  }
}
