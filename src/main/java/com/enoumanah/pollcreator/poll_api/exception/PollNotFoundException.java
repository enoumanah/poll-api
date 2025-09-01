package com.enoumanah.pollcreator.poll_api.exception;

public class PollNotFoundException extends RuntimeException {
    public PollNotFoundException(String message) {
        super(message);
    }
}
