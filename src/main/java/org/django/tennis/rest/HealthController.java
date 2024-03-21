package org.django.tennis.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.django.tennis.service.HealthService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "HealthCheck API")
@RestController
public class HealthController {
    private final HealthService healthService;

    public HealthController(HealthService healthService) {
        this.healthService = healthService;
    }
    @Operation(summary = "Returns application status", description = "Returns the application status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Healthcheck status with some details",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = HealthCheck.class))})

    })
    @GetMapping("/health-check")
    public HealthCheck healthCheck(){
        return healthService.getApplicationStatus();
    }
}
