package org.django.tennis.rest;

public record HealthCheck(ApplicationStatus status, String message) {
}
