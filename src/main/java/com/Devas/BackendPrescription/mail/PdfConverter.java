package com.Devas.BackendPrescription.mail;

import com.Devas.BackendPrescription.models.Doctor;
import com.Devas.BackendPrescription.models.Medicines;
import com.Devas.BackendPrescription.models.Prescription;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.IOException;
import java.io.OutputStream;

public class PdfConverter {
    public void CovertDataToPrescriptionPdf(OutputStream outputStream, Prescription prescription, Doctor doctor) throws Exception {
       //creating and opening document
        Document document = new Document();
        PdfWriter.getInstance(document, outputStream);
        document.open();
        document.addTitle("Prescription");
        document.addCreator("Devashish");

        //watermark;
       // BufferedImage img = ImageIO.read(new File("src/main/resources/static/image/DoctorSign.png"));
        //2 column and row for rx and doctor information
        PdfPTable doctorDetailsTable = new PdfPTable(2);
        doctorDetailsTable.setWidthPercentage(100);
        doctorDetailsTable.setWidths(new int[]{1, 3});
        doctorDetailsTable.addCell(createImageCell("src/main/resources/static/image/RxIcon.png"));  //adding image

        PdfPCell cell = new PdfPCell() ;
        cell.setPadding(2);
        cell.setBorder(0);
        cell.setRowspan(4);
        cell.addElement(new Paragraph("Doctor Name:  "+doctor.getName()));
        cell.addElement(new Paragraph("Email:  "+doctor.getEmail()));
        cell.addElement(new Paragraph("Mobile:  "+doctor.getMobile()));
        cell.addElement(new Paragraph("Address: "+doctor.getAddress()));

        doctorDetailsTable.addCell(cell);
        document.add(doctorDetailsTable);

        document.add(new Paragraph("\n\nName: "+ prescription.getPatientName()));
        document.add(new Paragraph("Email: "+ prescription.getPatientEmailId()));
        document.add(new Paragraph("Symptoms: "+ prescription.getSymptoms()));
        document.add(new Paragraph("Medicine Course(In days): "+prescription.getDuration()+"\n\n\n\n"));

        // medicine table
        PdfPTable medicineTable = new PdfPTable(3);
        medicineTable.setWidthPercentage(100);
        medicineTable.setWidths(new int[]{2,1,2});

        PdfPCell medicineName = new PdfPCell();
        medicineName.setPadding(5);
        medicineName.addElement(createTextCell("Medicine"));

        PdfPCell dosage = new PdfPCell();
        dosage.addElement(createTextCell("Dosage"));
        PdfPCell timings = new PdfPCell();
        timings.addElement(createTextCell("Timings"));
        PdfPTable timingsTable = new PdfPTable(3);
        timingsTable.getDefaultCell().setBorder(0);
        timingsTable.setWidths(new int[]{1,1,1});

        for(Medicines m: prescription.getMedicines()){
            medicineName.addElement(createTextCell(m.getMedicineName()));
            dosage.addElement(createTextCell(Integer.toString(m.getDosage())));
            String []convertedTiming = TimingConverter(m.getTimings());

            for(String timing:convertedTiming){
                timingsTable.addCell(createLeftCell(timing));
            }
        }
        timingsTable.setWidthPercentage(100);
        timings.addElement(timingsTable);

        medicineTable.addCell(medicineName);
        medicineTable.addCell(dosage);
        medicineTable.addCell(timings);
        document.add(medicineTable);

        document.add(new Paragraph("\n\n\n"));
        document.add(new Paragraph("Advice: "+prescription.getAdvice()));
        document.add(new Paragraph("You will receive timely reminders to take your medicine."));
        document.close();
    }
    private String[] TimingConverter(int []timing) {
        String[] convert= {"Morning","Evening","Night"};
        String[] converted = new String[3];
        for(int temp=0;temp<3;temp++) converted[temp]= (timing[temp]==1)?convert[temp]:"-";
        return converted;
    }

    public static PdfPCell createImageCell(String path) throws DocumentException, IOException {
        Image img = Image.getInstance(path);
        PdfPCell cell = new PdfPCell(img, true);
        cell.setBorder(0);
        cell.setFixedHeight(40);
        return cell;
    }
    public static Paragraph createTextCell(String text){
        Paragraph p = new Paragraph(text+"\n\n");
        p.setAlignment(Element.ALIGN_CENTER);
        return p;
    }
    public static PdfPCell createLeftCell(String text){
        PdfPCell cell = new PdfPCell();
        Paragraph p = new Paragraph(text+"\n\n");
        p.setAlignment(Element.ALIGN_CENTER);
        cell.setBorder(0);
        cell.addElement(p);
        return cell;
    }
}
