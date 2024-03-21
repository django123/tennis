package org.django.tennis.service;

import org.django.tennis.repository.HealthCheckRepository;
import org.django.tennis.rest.ApplicationStatus;
import org.django.tennis.rest.HealthCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HealthService {



    public HealthCheck getApplicationStatus(){
        return new HealthCheck(ApplicationStatus.OK, "Welcome to Dyma Tennis !");

      /*  Long applicationConnections = healthCheckRepository.countApplicationConnections();
        if (applicationConnections > 0){
            return new HealthCheck(ApplicationStatus.OK, "Welcome to Dyma Tennis !");

        }else {
            return new HealthCheck(ApplicationStatus.KO, "Dyma Tennis is not fully functional, please check your configuration.");
        }*/
    }
}
