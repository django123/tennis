package org.django.tennis.exception;

public class PlayerAlreadyExistException extends RuntimeException{
    public PlayerAlreadyExistException(String lastName) {
        super("Player with lastname " + lastName + " already exists.");
    }
}
