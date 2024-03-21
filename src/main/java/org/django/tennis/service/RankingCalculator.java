package org.django.tennis.service;

import org.django.tennis.entities.Player;
import org.django.tennis.entities.PlayerEntity;
import org.django.tennis.entities.Rank;
import org.django.tennis.model.PlayerToRegister;

import java.util.ArrayList;
import java.util.List;

public class RankingCalculator {
    private final List<PlayerEntity> currentPlayersRanking;


    public RankingCalculator(List<PlayerEntity> currentPlayersRanking) {
        this.currentPlayersRanking = currentPlayersRanking;
    }

    public List<PlayerEntity> getNewPlayersRanking(){
        currentPlayersRanking.sort((player1, player2) -> Integer.compare(player2.getPoints(), player1.getPoints()));
        List<PlayerEntity> updatedPlayers = new ArrayList<>();
        for (int i = 0; i < currentPlayersRanking.size() ; i++) {
            PlayerEntity updatedPlayer = currentPlayersRanking.get(i);
            updatedPlayer.setRank(i + 1);
            updatedPlayers.add(updatedPlayer);
        }

        return updatedPlayers;
    }

}
