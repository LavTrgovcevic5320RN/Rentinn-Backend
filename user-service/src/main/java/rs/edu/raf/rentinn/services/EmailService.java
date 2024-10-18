package rs.edu.raf.rentinn.services;

public interface EmailService {

    Boolean sendEmail(String to, String subject, String body);
}
