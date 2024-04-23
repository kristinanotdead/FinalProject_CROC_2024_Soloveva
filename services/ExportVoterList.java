package services;

import models.Voter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ExportVoterList {
    public static void exportVotersList(List<Voter> voters, String filename) throws IOException {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.append("Surname,Name,Patronymic\n");

            for(Voter voter : voters) {
                writer.append(String.join(",", voter.getSurname(), voter.getName(), voter.getPatronymic()));
                writer.append("\n");
            }

            System.out.println("Список избирателей экспортирован в файл " + filename);
        }
    }
}

