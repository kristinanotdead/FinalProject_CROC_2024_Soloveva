package errors;

import models.Voter;

public class VoterAlreadyVoteException extends Exception{
    private Voter voter;

    public VoterAlreadyVoteException(Voter voter) {
        super(String.format("Избиратель %s %s уже голосовал!",
                voter.getSurname(), voter.getName()));
        this.voter = voter;
    }
}
