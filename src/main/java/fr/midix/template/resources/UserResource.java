package fr.midix.template.resources;

import com.fasterxml.jackson.annotation.JsonInclude;

import fr.midix.template.entities.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResource extends BaseResource {

  private String email;

  private String password;

  private String firstname;

  private String lastname;

  private String token;

  public static UserResource from(User user) {
    UserResource resource = new UserResource();
    resource.setId(user.getId());
    resource.setEmail(user.getEmail());
    resource.setFirstname(user.getFirstname());
    resource.setLastname(user.getLastname());
    return resource;
  }
}
