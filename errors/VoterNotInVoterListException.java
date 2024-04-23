package errors;

import models.Voter;

public class VoterNotInVoterListException extends Exception{
    private Voter voter;

    public VoterNotInVoterListException(Voter voter) {
        super(String.format("Избирателя %s %s нет в списке!",
                voter.getSurname(), voter.getName()));
        this.voter = voter;
    }
}
