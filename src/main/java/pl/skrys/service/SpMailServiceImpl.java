package pl.skrys.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


//skrysmailspring@gmail.com
//springmail1
@Service
public class SpMailServiceImpl implements SpMailService{

    @Autowired
    private JavaMailSender emailSender;

    public void sendSimpleMessage(String to, String subject, String text) {
        //...
        SimpleMailMessage message = new SimpleMailMessage();
        //message.setFrom("noreply@SkrysSpol.pl");
        message.setFrom("skrysmailspring@zohomail.eu");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
        //...
    }
}
