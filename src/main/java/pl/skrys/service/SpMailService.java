package pl.skrys.service;

import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

public interface SpMailService {
    public void sendSimpleMessage(String to, String subject, String text);
}
