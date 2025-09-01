package com.enoumanah.pollcreator.poll_api.exception;

public class OptionNotFoundException extends RuntimeException {
    public OptionNotFoundException(String message) {
        super(message);
    }
}
