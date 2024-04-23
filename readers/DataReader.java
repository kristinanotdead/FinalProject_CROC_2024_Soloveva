package readers;
import models.Voter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class DataReader {
    public static List<Voter> readFromFile(String filename) throws IOException {
        List<Voter> VoterList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                if (parts.length == 0 || parts[0].equals("Фамилия")){
                    continue;
                }

                StringBuilder addressBuilder = new StringBuilder();

                for(int i = 4; i < parts.length; i++){
                    addressBuilder.append(parts[i]);
                }

                String address = addressBuilder.toString().replace("\"", "");

                Voter voter = new Voter(parts[0],parts[1],parts[2],parts[3],address);
                VoterList.add(voter);
            }
        }

        return VoterList;
    }
}

