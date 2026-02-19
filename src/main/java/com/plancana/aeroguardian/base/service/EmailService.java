package com.plancana.aeroguardian.base.service;


import com.plancana.aeroguardian.base.enums.AqiLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String recipientEmail, String zoneName, AqiLevel level, double aqi){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipientEmail);
        message.setSubject("AIR QUALITY ALERT: " + zoneName);

        String body = String.format(
                "Attention Administration,\n\n" +
                        "The air quality near %s has reached a dangerous level.\n\n" +
                        "Current AQI: %.0f\n" +
                        "Status: %s\n" +
                        "Health Implication: %s\n\n" +
                        "CAUTION: %s\n\n" +
                        "Please take necessary precautions immediately.",
                zoneName, aqi, level, level.getDescription(), level.getCaution()
        );
        message.setText(body);
        mailSender.send(message);

        System.out.println("Mail sent successfully");
    }
}
