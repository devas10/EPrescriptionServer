package com.Devas.BackendPrescription.mail;

import com.Devas.BackendPrescription.models.Doctor;
import com.Devas.BackendPrescription.models.Medicines;
import com.Devas.BackendPrescription.models.Prescription;
import com.Devas.BackendPrescription.repositories.PrescriptionRepository;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class EmailService {
    JavaMail javaMail =new JavaMail();
    Session session = javaMail.getSession();
    List<Prescription> scheduledPrescription;
    @Autowired
    PrescriptionRepository prescriptionRepository;

    @Scheduled(cron ="0 0 9,14,21 * * *")
    public void sendReminderMail() throws MessagingException {
        scheduledPrescription = prescriptionRepository.findAll();
        System.out.println("Scheduled mail sending begins........"+scheduledPrescription.size());

        SimpleDateFormat sdf = new SimpleDateFormat("HH");
        Date now = new Date();
        String CurrentTime = sdf.format(now);
        int index = 0;
        if(!CurrentTime.equals("9")) index = (CurrentTime.equals("14"))?1:2;

        for(Prescription p:scheduledPrescription) {
            if(p.getScheduledCourse()>0) {
                StringBuilder reminderText = new StringBuilder("Take the mentioned medicines:\n");
                int tempNum = 1;
                for (Medicines med : p.getMedicines()) {
                    if (med.getTimings()[index] == 1) {
                        reminderText.append(tempNum++).append(". ").append(med.getMedicineName()).append("  ").append(med.getDosage()).append("\n");
                    }
                }
                if(index==2){
                    p.setScheduledCourse(p.getScheduledCourse()-1);
                    prescriptionRepository.save(p);
                }
                if (reminderText.toString().equals("Take the mentioned medicines:\n"))
                    continue;
                reminderText.append("\n  Be well, Stay safe and Take care.\n\nRegards, \nE-Pres team");
                MimeMessage message = new MimeMessage(session);
                message.setRecipient(Message.RecipientType.TO, new InternetAddress(p.getPatientEmailId()));
                message.setFrom(new InternetAddress("abhishek.dmce.fyp@gmail.com"));
                message.setSubject("Reminder: Take your prescribed medicine.");
                message.setText("Hello! "+p.getPatientName()+",\nHope you are having a fine day.\n This is to remind you to take your medicines.\n" + reminderText);
                Transport.send(message);
            }
        }
        System.out.println("Scheduled mail sending Ends........");
    }

    @Async
    public void sendAttachmentMail(String to, String subject, Prescription prescription,Doctor doctor) {
        PdfConverter pdfConverter = new PdfConverter();
        MimeMessage mimeMessage = new MimeMessage(session);
        ByteArrayOutputStream outputStream = null;
        try {
            mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            mimeMessage.setFrom(new InternetAddress("abhishek.dmce.fyp@gmail.com"));
            mimeMessage.setSubject(subject);

            MimeBodyPart textBodyPart = new MimeBodyPart();
            textBodyPart.setText("Hello " + prescription.getPatientName() + ",\n" + "Hope you are having a fine day. You can find your prescription below.\nBe well, Stay safe and Take care. \n\nRegards,\nE-Pres Team.");

            outputStream = new ByteArrayOutputStream();
            pdfConverter.CovertDataToPrescriptionPdf(outputStream, prescription, doctor);
            byte[] bytes = outputStream.toByteArray();

            DataSource dataSource = new ByteArrayDataSource(bytes, "application/pdf");
            MimeBodyPart pdfBodyPart = new MimeBodyPart();
            pdfBodyPart.setDataHandler(new DataHandler(dataSource));
            pdfBodyPart.setFileName(prescription.getPatientName() + ".pdf");

            MimeMultipart mimeMultipart = new MimeMultipart();
            mimeMultipart.addBodyPart(textBodyPart);
            mimeMultipart.addBodyPart(pdfBodyPart);
            mimeMessage.setContent(mimeMultipart);
            Transport.send(mimeMessage);

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (null != outputStream) {
                try {
                    outputStream.close();
                } catch (Exception ex) {
                    System.out.println("Error in mailing prescription");
                }
            }
        }
    }
}
