package fr.midix.template.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.midix.template.entities.User;
import fr.midix.template.resources.UserResource;
import fr.midix.template.services.UserService;
import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @GetMapping("/me")
  public ResponseEntity<UserResource> getConnectedUser(Authentication authentication) {
    User user = this.userService.checkAuthentication(authentication);
    return new ResponseEntity<>(UserResource.from(user), HttpStatus.OK);
  }
}
