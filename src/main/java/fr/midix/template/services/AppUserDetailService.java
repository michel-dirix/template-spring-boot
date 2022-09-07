package fr.midix.template.services;

import java.util.Collections;
import java.util.Optional;

import org.apache.http.util.TextUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import fr.midix.template.entities.User;
import fr.midix.template.repositories.UserRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AppUserDetailService implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public final UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    if (TextUtils.isBlank(username)) {
      throw new UsernameNotFoundException("User '" + username + "' not found");
    }

    username = username.trim().toLowerCase();
    Optional<User> optUser = this.userRepository.findById(username);
    if (!optUser.isPresent()) {
      throw new UsernameNotFoundException("User '" + username + "' not found");
    }
    User user = optUser.get();

    return org.springframework.security.core.userdetails.User.withUsername(username).password(user.getPassword())
        .authorities(Collections.emptyList()).accountExpired(false).accountLocked(false)
        .credentialsExpired(false).disabled(false).build();
  }
}
