package models;

import java.util.Objects;

public class Ballot {
    private int ballotNumber;

    public Ballot(int ballotNumber){
        this.ballotNumber = ballotNumber;
    }

    public int getBallotNumber(){
        return ballotNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ballot ballot = (Ballot) o;
        return ballotNumber == ballot.ballotNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ballotNumber);
    }
}


