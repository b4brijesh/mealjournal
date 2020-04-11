package com.mealjournal.app.exceptions;

import lombok.Getter;
import lombok.Setter;

public class JournalAppException extends Exception {

    @Getter
    @Setter
    private String message;

    public JournalAppException(String message) {
        super();
        this.message = message;
    }
}
