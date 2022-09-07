package fr.midix.template.services;

import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import fr.midix.template.config.AppConfig;
import fr.midix.template.entities.User;
import fr.midix.template.model.Email;

import de.neuland.pug4j.PugConfiguration;
import de.neuland.pug4j.template.FileTemplateLoader;
import de.neuland.pug4j.template.PugTemplate;
import de.neuland.pug4j.template.TemplateLoader;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

  private final JavaMailSender javaMailSender;
  private final AppConfig appConfig;

  @Async
  public void sendRegistrationEmail(User user) {
    Email mail = new Email();
    mail.setTo(user.getEmail());
    mail.setSubject("Votre inscription");
    mail.getModel().put("user", user);
    this.sendEmail(mail, "registration-template");
  }

  private void sendEmail(Email mail, String template) {
    MimeMessage mimeMessage = javaMailSender.createMimeMessage();
    try {

      MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

      mimeMessageHelper.setSubject(mail.getSubject());
      mimeMessageHelper.setFrom("mail@mail.com");
      mimeMessageHelper.setTo(mail.getTo());
      mail.getModel().put("logo",
          "https://ik.imagekit.io/sdtpmi4qu4/echo-orthos_z_qxLg6zV.png?ik-sdk-version=javascript-1.4.3&updatedAt=1658836352394");
      mail.setContent(getContentFromTemplate(template, mail.getModel()));
      mimeMessageHelper.setText(mail.getContent(), true);

      javaMailSender.send(mimeMessageHelper.getMimeMessage());
    } catch (MessagingException e) {
      e.printStackTrace();
    }
  }

  private String getContentFromTemplate(String templateName, Map<String, Object> model) {
    String content = "";

    try {
      ClassLoader classLoader = getClass().getClassLoader();

      PugConfiguration config = new PugConfiguration();
      TemplateLoader loader = new FileTemplateLoader(classLoader.getResource("templates/").getPath());
      config.setTemplateLoader(loader);

      PugTemplate template = config.getTemplate(
          templateName + ".pug");
      content = config.renderTemplate(template, model);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return content;
  }
}
