package fr.midix.template.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import fr.midix.template.entities.User;
import fr.midix.template.repositories.UserRepository;

@Component
public class PasswordAuthenticationProvider implements AuthenticationProvider {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String email = authentication.getPrincipal().toString();
    String password = authentication.getCredentials().toString();

    User user = this.userRepository.findByEmail(email);
    boolean passwordsMatches = this.passwordEncoder.matches(password, user.getPassword());

    if (!passwordsMatches) {
      return null;
    }

    return new UsernamePasswordAuthenticationToken(email, password,
        AuthorityUtils.createAuthorityList());
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return authentication.equals(UsernamePasswordAuthenticationToken.class);
  }
}
