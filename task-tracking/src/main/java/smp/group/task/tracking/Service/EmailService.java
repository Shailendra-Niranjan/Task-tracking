package smp.group.task.tracking.Service;

import jakarta.mail.MessagingException;

import java.io.IOException;

public interface EmailService {
    void sendSimpleEmail(String to, String subject, String text);
    void sendResetPassword(String to, String subject, String text , String name) throws IOException, MessagingException;
    void userCreationMail(String to, String subject, String text , String name ) throws IOException, MessagingException;
    void requestForTeam(String to,String from, String subject, String senderName , String name , String teamName ) throws MessagingException, IOException;
}
