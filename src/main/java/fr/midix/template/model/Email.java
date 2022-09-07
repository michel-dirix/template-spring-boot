package fr.midix.template.model;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class Email {
  String to;
  String subject;
  String content;
  private Map<String, Object> model = new HashMap<>();

}
