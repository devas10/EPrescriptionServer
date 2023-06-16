package com.Devas.BackendPrescription.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;

@Configuration
public class JavaMail {
  private static String email;
  private static String password;

  @Value("${devas.email.emailId}")
  void setEmail(String email){
    this.email = email;
  }
  @Value("${devas.email.password}")
  void setPassword(String password){
    this.password = password;
  }

  @Bean
  JavaMailSender getJavaMailSender(){
    JavaMailSenderImpl MailServer = new JavaMailSenderImpl();
    MailServer.setHost("smtp.gmail.com");   // Gmail host the same as the email of the host
    MailServer.setPort(587);
    MailServer.setUsername(email);
    MailServer.setPassword(password);
    Properties props = MailServer.getJavaMailProperties();
    props.put("mail.transport.protocol", "smtp");
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.debug", "true");
    return  MailServer;
  }
  @Bean
  Session getSession(){
    Properties props = new Properties();
    props.put("mail.smtp.auth", true);
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.host", "smtp.gmail.com");
    props.put("mail.smtp.port", "587");
    props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
    Session session = Session.getInstance(props, new Authenticator() {
      @Override
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(email, password);
      }
    });
    return session;
  }


}
