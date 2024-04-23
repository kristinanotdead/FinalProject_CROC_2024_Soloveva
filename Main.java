import db.BallotDAO;
import errors.VoterAlreadyVoteException;
import errors.VoterNotInVoterListException;
import readers.PassportReader;
import services.ExportVoterList;
import db.DbProcessor;
import db.VoterDAO;
import models.Voter;
import readers.DataReader;
import services.VoterPDFExporter;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String... args) throws Exception {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e){
            System.out.println("Не установлен драйвер PostgreSQL!");
            return;
        }

        if (args.length < 1){
            System.out.println("Неверное количество аргументов!");
            return;
        }

        List<Voter> votersList;

        try {
            votersList = DataReader.readFromFile(args[0]);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return;
        }

        VoterDAO voterDAO = new VoterDAO();
        BallotDAO ballotDAO = new BallotDAO();

        try {
            DbProcessor.deleteDb();
            DbProcessor.createDb();
            voterDAO.importData(votersList);
            ballotDAO.createBallots(votersList.size());

            System.out.println("Данные импортированы успешно!");
        } catch (SQLException e){
            System.out.println(e.getMessage());
            return;
        }

        List<Voter> voters = new ArrayList<>();

        try{
            for(int i = 1; i < args.length; i++){
                voters.add(PassportReader.readFromFile(args[i]));
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return;
        }

        voters.parallelStream().forEach(voter -> {
            try {
                voterDAO.vote(voter);
            } catch (SQLException | VoterAlreadyVoteException | VoterNotInVoterListException e) {
                System.out.println(e.getMessage());
            }
        });

        try {
            String csvFile = "results.csv";
            ExportVoterList.exportVotersList(voterDAO.getVotersList(), csvFile);
            System.out.println("CSV файл успешно создан: " + csvFile);

            String pdfFile = "results.pdf";
            VoterPDFExporter.exportVotersToPDF(voterDAO.getVotersBallots(), pdfFile);
            System.out.println("PDF файл успешно создан: " + pdfFile);
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
}

