package ServiceMail;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.io.File;
import java.util.Properties;

public class EmailService {

    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final int SMTP_PORT = 587;
    private static final String EMAIL_SENDER = "pokedex694@gmail.com";
    private static final String APP_PASSWORD = "ffoymgvwsnwzaapj"; // Mot de passe d'application

    private static Session getSession() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", String.valueOf(SMTP_PORT));

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_SENDER, APP_PASSWORD);
            }
        });
        session.setDebug(true);
        return session;
    }

    public static void sendSimpleEmail(String recipientEmail, String subject, String messageBody) {
        if (!isValidEmail(recipientEmail)) {
            throw new IllegalArgumentException("Email invalide: " + recipientEmail);
        }

        try {
            Session session = getSession();
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL_SENDER));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject(subject, "UTF-8");
            message.setContent(messageBody, "text/html; charset=UTF-8");
            Transport.send(message);
            System.out.println("E-mail envoyé avec succès à " + recipientEmail);
        } catch (MessagingException e) {
            System.err.println("Échec de l'envoi : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void sendEmailWithAttachment(String recipientEmail, String subject, String messageBody, File attachment) {
        if (!isValidEmail(recipientEmail)) {
            throw new IllegalArgumentException("Email invalide: " + recipientEmail);
        }
        try {
            Session session = getSession();
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL_SENDER));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject(subject, "UTF-8");

            // Création de la partie texte
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setContent(messageBody, "text/html; charset=UTF-8");

            // Création de la partie pièce jointe
            MimeBodyPart attachmentPart = new MimeBodyPart();
            attachmentPart.attachFile(attachment);
            attachmentPart.setFileName(attachment.getName());

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(textPart);
            multipart.addBodyPart(attachmentPart);

            message.setContent(multipart);
            Transport.send(message);
            System.out.println("E-mail avec pièce jointe envoyé avec succès à " + recipientEmail);
        } catch (Exception e) {
            System.err.println("Échec de l'envoi avec pièce jointe : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static boolean isValidEmail(String email) {
        return email != null && email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }
}
