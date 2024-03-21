package org.django.tennis.service;

import org.django.tennis.entities.Player;
import org.django.tennis.model.PlayerToRegister;

import java.util.List;

public interface PlayerService {

    List<Player>getAllPlayers();

    Player getByLastName(String lastName);
    Player create(PlayerToRegister playerToRegister);
    Player update(PlayerToRegister playerToSave);
    void delete(String lastName);
}
