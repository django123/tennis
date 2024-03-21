package org.django.tennis.repository;

import org.django.tennis.entities.PlayerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlayerRepository extends JpaRepository<PlayerEntity, Long> {
    Optional<PlayerEntity> findOneByLastNameIgnoreCase(String lastName);
}
