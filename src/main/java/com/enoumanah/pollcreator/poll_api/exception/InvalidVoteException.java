package com.enoumanah.pollcreator.poll_api.exception;

public class InvalidVoteException extends RuntimeException {
    public InvalidVoteException(String message) {
        super(message);
    }
}
