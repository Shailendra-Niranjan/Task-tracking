package smp.group.task.tracking.ServiceImpl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import smp.group.task.tracking.Service.EmailService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendSimpleEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        message.setFrom(fromEmail);

        mailSender.send(message);
    }

    public void sendResetPassword(String to, String subject, String text , String name) throws IOException, MessagingException {
        String content = loadTemplate("src/main/java/smp/group/task/tracking/EmailTemplate/New-Reset-password.html" , name , text);
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper =  new MimeMessageHelper(mimeMessage , true);
        helper.setFrom(fromEmail);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setSentDate(new Date());
        helper.setText(content ,true);
        mailSender.send(mimeMessage);

    }

    @Override
    public void userCreationMail(String to, String subject, String text, String name) throws IOException, MessagingException {
        String content = loadUserCreationTemplate("src/main/java/smp/group/task/tracking/EmailTemplate/UserCreationTemplate.html" , name , to );
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper =  new MimeMessageHelper(mimeMessage , true);
        helper.setFrom(fromEmail);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setSentDate(new Date());
        helper.setText(content ,true);
        mailSender.send(mimeMessage);
    }

    @Override
    public void requestForTeam(String to, String from, String subject, String senderName, String name, String teamName) throws MessagingException, IOException {
//        src/main/java/smp/group/task/tracking/EmailTemplate/TeamRequest.html
        String content = loadteamRequestTemplate("src/main/java/smp/group/task/tracking/EmailTemplate/TeamRequest.html" , name , senderName , teamName );

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper =  new MimeMessageHelper(mimeMessage , true);
        helper.setFrom(fromEmail);
        helper.setTo(to);
        helper.setBcc(from);
        helper.setSubject(subject);
        helper.setSentDate(new Date());
        helper.setText(content ,true);
        mailSender.send(mimeMessage);



    }

    private String loadteamRequestTemplate(String fileName, String name, String senderName, String teamName) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(fileName)));

        // Replace user-specific details
        content =content.replace("[Reciver]", name);
        content = content.replace("[sender]", senderName);
        content = content.replace("[team Name]", teamName);


        return content;
    }

    private String loadUserCreationTemplate(String fileName, String name, String email ) throws IOException {
        // Read the HTML template file
        String content = new String(Files.readAllBytes(Paths.get(fileName)));

        // Replace user-specific details
        content =content.replace("[Subuser's Name]", name);
        content = content.replace("[username]", email);


        return content;
    }
    private String loadTemplate(String fileName, String name, String newPassword) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(fileName)));
        content = content.replace("{{name}}", name);
        content = content.replace("{{newPassword}}", newPassword);
        return content;
    }
}
