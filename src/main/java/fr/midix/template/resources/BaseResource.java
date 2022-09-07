package fr.midix.template.resources;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class BaseResource {
  @EqualsAndHashCode.Include
  protected String id;

  protected String createdAt;

  protected String lastModifiedAt;
}
