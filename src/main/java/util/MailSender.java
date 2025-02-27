package util;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;

public class MailSender {
    public static void main(String[] args) {
        final String senderEmail = "pokedex694@gmail.com";
        final String appPassword = "ffoymgvwsnwzaapj"; // Mot de passe d'application

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, appPassword);
            }
        });
        session.setDebug(true); // Ajoutez cette ligne après la création de la session

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress("pokedex694@gmail.com"));
            message.setSubject("Test Java 22");
            message.setText("Ça fonctionne !");

            Transport.send(message);
            System.out.println("E-mail envoyé ✅");
        } catch (MessagingException e) {
            System.err.println("Erreur : " + e.getMessage());
        }
    }
}