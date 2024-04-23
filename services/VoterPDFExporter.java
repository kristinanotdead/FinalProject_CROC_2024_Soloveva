package services;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import db.ConnectionsManager;
import models.Ballot;
import models.Voter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

public class VoterPDFExporter {
    public static final String FONT = "./resources/Arial.ttf";

    public static void exportVotersToPDF(Map<Voter, Ballot> votersBallots, String filename) throws IOException {
        try (PdfWriter writer = new PdfWriter(filename)) {
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            PdfFont font = PdfFontFactory.createFont(FONT, PdfEncodings.IDENTITY_H);

            for(Map.Entry<Voter, Ballot> entry : votersBallots.entrySet()) {
                Paragraph paragraph = new Paragraph()
                        .add("Фамилия: " + entry.getKey().getSurname() + "\n")
                        .add("Имя: " + entry.getKey().getName() + "\n")
                        .add("Отчество: " + entry.getKey().getPatronymic() + "\n")
                        .add("Паспортные данные: " + entry.getKey().getPassport() + "\n")
                        .add("Номер бюллетеня: " + entry.getValue().getBallotNumber() + "\n\n")
                        .setFont(font)
                        .setTextAlignment(TextAlignment.LEFT);

                document.add(paragraph);
            }

            document.close();
        }
    }

}
