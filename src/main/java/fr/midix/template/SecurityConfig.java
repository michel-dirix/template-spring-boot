package fr.midix.template;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import fr.midix.template.security.JWTConfigurer;
import fr.midix.template.security.TokenProvider;
import fr.midix.template.services.AppUserDetailService;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

  private final TokenProvider tokenProvider;

  private final AppUserDetailService userDetailsService;

  private final PasswordEncoder passwordEncoder;

  AuthenticationManager authenticationManager;

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
      throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    AuthenticationManagerBuilder authenticationManagerBuilder = http
        .getSharedObject(AuthenticationManagerBuilder.class);
    authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    authenticationManager = authenticationManagerBuilder.build();
    http.csrf().disable().cors().and().authorizeRequests().antMatchers("/**").permitAll().and()
        .apply(new JWTConfigurer(this.tokenProvider));
    http.authenticationManager(authenticationManager);
    return http.build();
  }
}
