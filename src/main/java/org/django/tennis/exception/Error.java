package org.django.tennis.exception;

public class Error {

    String errorDetails;

    public Error() {
    }

    public Error(String errorDetails) {
        this.errorDetails = errorDetails;
    }

    public String getErrorDetails() {
        return errorDetails;
    }

    public void setErrorDetails(String errorDetails) {
        this.errorDetails = errorDetails;
    }
}
