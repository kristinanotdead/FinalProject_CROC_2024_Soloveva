package readers;

import models.Voter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class PassportReader {
    public static Voter readFromFile(String filename) throws IOException {
        Voter voter = null;

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                if (parts.length == 0){
                    continue;
                }

                StringBuilder addressBuilder = new StringBuilder();

                for(int i = 4; i < parts.length; i++){
                    addressBuilder.append(parts[i]);
                }

                String address = addressBuilder.toString().replace("\"", "");

                voter = new Voter(parts[0],parts[1],parts[2],parts[3],address);
            }
        }

        return voter;
    }
}



