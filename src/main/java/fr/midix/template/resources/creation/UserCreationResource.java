package fr.midix.template.resources.creation;

import lombok.Data;

@Data
public class UserCreationResource {

  private String email;

  private String password;

  private String firstname;

  private String lastname;
}
