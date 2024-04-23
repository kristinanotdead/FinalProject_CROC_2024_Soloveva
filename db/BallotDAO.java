package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BallotDAO {
    private static final String INSERT_INTO_BALLOTS = "insert into ballots (number) values (?)";

    public void createBallots(int count) throws SQLException {
        try (Connection conn = ConnectionsManager.getConnection()) {
            for (int i = 0; i < count; i++) {
                PreparedStatement ballotsPS = conn.prepareStatement(INSERT_INTO_BALLOTS);

                ballotsPS.setInt(1, 202401 + i);

                int affected_ballots_rows = ballotsPS.executeUpdate();
            }
        }
    }
}
