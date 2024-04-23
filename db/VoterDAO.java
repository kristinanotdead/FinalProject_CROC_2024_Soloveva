package db;

import errors.VoterAlreadyVoteException;
import errors.VoterNotInVoterListException;
import models.Ballot;
import models.Voter;

import java.sql.*;
import java.util.*;

public class VoterDAO {
    private static final String INSERT_INTO_VOTERS = "insert into voters " +
            "(surname, name, patronymic, passport, address) values (?, ?, ?, ?, ?)";

    private static final String SELECT_FROM_VOTERS_BALLOTS = "select * from voters inner join voters_ballots on " +
            "voters.id = voters_ballots.voter_id where voters.passport = ?";

    private static final String SELECT_FROM_VOTERS = "select * from voters where surname = ? and name = ? and " +
            "patronymic = ? and passport = ?";

    private static final String SELECT_ALL_VOTERS = "select * from voters";

    private static final String SELECT_FROM_BALLOTS = "select ballots.id from ballots where id not in " +
            "(select ballots.id from ballots " +
            "inner join voters_ballots on voters_ballots.ballot_id = ballots.id)";

    private static final String INSERT_INTO_VOTERS_BALLOTS = "insert into voters_ballots (voter_id, ballot_id) " +
            "values (?, ?)";

    private static final String SELECT_ALL_FROM_VOTERS_BALLOTS = "select * from voters inner join voters_ballots on " +
            "voters.id = voters_ballots.voter_id inner join ballots on voters_ballots.ballot_id = ballots.id";

    public void importData(List<Voter> voterList) throws SQLException {
        try (Connection conn = ConnectionsManager.getConnection()) {
            for (Voter voter : voterList) {
                PreparedStatement voterPS = conn.prepareStatement(INSERT_INTO_VOTERS);
                voterPS.setString(1, voter.getSurname());
                voterPS.setString(2, voter.getName());
                voterPS.setString(3, voter.getPatronymic());
                voterPS.setString(4, voter.getPassport());
                voterPS.setString(5, voter.getAddress());

                int affected_voters_rows = voterPS.executeUpdate();
            }
        }
    }

    public synchronized void vote(Voter voter) throws SQLException, VoterAlreadyVoteException, VoterNotInVoterListException {
        int voterId = checkIfVoterInVoterList(voter);
        if(voterId == -1){
            throw new VoterNotInVoterListException(voter);
        }

        if(checkIfVoterAlreadyVoted(voter)){
            throw new VoterAlreadyVoteException(voter);
        }

        int ballot_id = getBallotId();

        insertBallot(voterId, ballot_id);
    }

    private boolean checkIfVoterAlreadyVoted(Voter voter) throws SQLException {
        try (Connection conn = ConnectionsManager.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(SELECT_FROM_VOTERS_BALLOTS);
            ps.setString(1, voter.getPassport());

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private int checkIfVoterInVoterList(Voter voter) throws SQLException {
        try (Connection conn = ConnectionsManager.getConnection()) {
            PreparedStatement voterPS = conn.prepareStatement(SELECT_FROM_VOTERS);
            voterPS.setString(1, voter.getSurname());
            voterPS.setString(2, voter.getName());
            voterPS.setString(3, voter.getPatronymic());
            voterPS.setString(4, voter.getPassport());

            try (ResultSet rs = voterPS.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                } else{
                    return -1;
                }
            }
        }
    }

    private int getBallotId() throws SQLException {
        List<Integer> ballotsIds = new ArrayList<>();

        try (Connection conn = ConnectionsManager.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(SELECT_FROM_BALLOTS);

            try (ResultSet rs = ps.executeQuery()) {
                while(rs.next()){
                    ballotsIds.add(rs.getInt(1));
                }
            }
        }

        Random rand = new Random();
        int value = rand.nextInt(ballotsIds.size());

        return ballotsIds.get(value);
    }

    private void insertBallot(int voterId, int ballotId) throws SQLException {
        try (Connection conn = ConnectionsManager.getConnection()) {
            PreparedStatement voterBallotPS = conn.prepareStatement(INSERT_INTO_VOTERS_BALLOTS);
            voterBallotPS.setInt(1, voterId);
            voterBallotPS.setInt(2, ballotId);

            int affected_rows = voterBallotPS.executeUpdate();
        }
    }

    public List<Voter> getVotersList() throws SQLException {
        List<Voter> voters = new ArrayList<>();

        try (Connection conn = ConnectionsManager.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(SELECT_ALL_VOTERS);

            try (ResultSet rs = ps.executeQuery()) {
                while(rs.next()){
                    voters.add(new Voter(rs.getString(2), rs.getString(3),
                            rs.getString(4), rs.getString(5), rs.getString(6)));
                }
            }
        }

        return voters;
    }

    public Map<Voter, Ballot> getVotersBallots() throws SQLException {
        Map<Voter, Ballot> votersBallots = new HashMap<>();

        try (Connection conn = ConnectionsManager.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(SELECT_ALL_FROM_VOTERS_BALLOTS);

            try (ResultSet rs = ps.executeQuery()) {
                while(rs.next()){
                    Voter voter = new Voter(rs.getString(2), rs.getString(3),
                            rs.getString(4), rs.getString(5), rs.getString(6));

                    Ballot ballot = new Ballot(rs.getInt(10));

                    votersBallots.put(voter, ballot);
                }
            }
        }

        return votersBallots;
    }
}