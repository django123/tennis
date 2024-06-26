package org.django.tennis.exception;

public class PlayerDataRetrievalException extends RuntimeException {
    public PlayerDataRetrievalException(Exception e) {
        super("Could not retrieve player data", e);
    }
}
