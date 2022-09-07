package fr.midix.template.services;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import fr.midix.template.entities.User;
import fr.midix.template.repositories.UserRepository;
import fr.midix.template.resources.creation.UserCreationResource;
import fr.midix.template.utils.Utils;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;

  public User checkAuthentication(Authentication authentication) {
    return this.findById(((UserDetails) authentication.getPrincipal()).getUsername());
  }

  public User findById(String id) {
    return this.userRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "USER_NOT_FOUND"));
  }

  public User findByEmail(String email) {
    return this.userRepository.findByEmail(email);
  }

  @Transactional
  public User create(UserCreationResource resource) {
    if (StringUtils.isBlank(resource.getEmail())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "EMAIL_IS_REQUIRED");
    }

    if (StringUtils.isBlank(resource.getPassword())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "PASSWORD_IS_REQUIRED");
    }

    resource.setEmail(resource.getEmail().toLowerCase().trim());
    resource.setPassword(resource.getPassword().trim());

    if (this.userRepository.existsByEmailIgnoreCase(resource.getEmail())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "EMAIL_ALREADY_IN_USE");
    }

    User user = new User();
    user.setEmail(resource.getEmail());
    user.setPassword(this.passwordEncoder.encode(resource.getPassword()));
    user.setFirstname(resource.getFirstname());
    user.setLastname(resource.getLastname());
    user.setCreatedAt(Utils.getUTCDateNow());
    user.setEnabled(true);

    return this.userRepository.save(user);
  }
}
