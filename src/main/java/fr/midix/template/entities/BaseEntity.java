package fr.midix.template.entities;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public abstract class BaseEntity {

  @Id
  @EqualsAndHashCode.Include
  protected String id;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  protected Date createdAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  protected Date lastModifiedAt;
}
