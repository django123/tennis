package org.django.tennis.service.impl;

import org.django.tennis.entities.PlayerEntity;
import org.django.tennis.entities.Rank;
import org.django.tennis.exception.PlayerAlreadyExistException;
import org.django.tennis.exception.PlayerNotFoundException;
import org.django.tennis.entities.Player;
import org.django.tennis.entities.PlayerList;
import org.django.tennis.model.PlayerToRegister;
import org.django.tennis.repository.PlayerRepository;
import org.django.tennis.service.PlayerService;
import org.django.tennis.service.RankingCalculator;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;

    public PlayerServiceImpl(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }


    @Override
    public List<Player> getAllPlayers() {
        return playerRepository.findAll().stream()
                .map(player -> new Player(
                      player.getFirstName(),
                      player.getLastName(),
                      player.getBirthDate(),
                      new Rank(player.getRank(), player.getPoints())
                ))
                .sorted(Comparator.comparing(player -> player.rank().position()))
                .collect(Collectors.toList());
    }

    @Override
    public Player getByLastName(String lastName) {
        Optional<PlayerEntity> player = playerRepository.findOneByLastNameIgnoreCase(lastName);
        if (player.isEmpty()){
            throw new PlayerNotFoundException(lastName);
        }
        return new Player(
                player.get().getFirstName(),
                player.get().getLastName(),
                player.get().getBirthDate(),
                new Rank(player.get().getRank(), player.get().getPoints()));
    }

    @Override
    public Player create(PlayerToRegister playerToSave) {
        Optional<PlayerEntity> player = playerRepository.findOneByLastNameIgnoreCase(playerToSave.lastName());
        if (player.isPresent()) {
            throw new PlayerAlreadyExistException(playerToSave.lastName());
        }

        PlayerEntity playerToRegister = new PlayerEntity(
                playerToSave.lastName(),
                playerToSave.firstName(),
                playerToSave.birthDate(),
                playerToSave.points(),
                999999999);

        PlayerEntity registeredPlayer = playerRepository.save(playerToRegister);

        RankingCalculator rankingCalculator = new RankingCalculator(playerRepository.findAll());
        List<PlayerEntity> newRanking = rankingCalculator.getNewPlayersRanking();
        playerRepository.saveAll(newRanking);

        return getByLastName(registeredPlayer.getLastName());

    }

    @Override
    public Player update(PlayerToRegister playerToSave) {
        Optional<PlayerEntity> playerToUpdate = playerRepository.findOneByLastNameIgnoreCase(playerToSave.lastName());
        if (playerToUpdate.isEmpty()) {
            throw new PlayerNotFoundException(playerToSave.lastName());
        }

        playerToUpdate.get().setFirstName(playerToSave.firstName());
        playerToUpdate.get().setBirthDate(playerToSave.birthDate());
        playerToUpdate.get().setPoints(playerToSave.points());
        PlayerEntity updatedPlayer = playerRepository.save(playerToUpdate.get());

        RankingCalculator rankingCalculator = new RankingCalculator(playerRepository.findAll());
        List<PlayerEntity> newRanking = rankingCalculator.getNewPlayersRanking();
        playerRepository.saveAll(newRanking);

        return getByLastName(updatedPlayer.getLastName());
    }

    @Override
    public void delete(String lastName) {
        Optional<PlayerEntity> playerDelete = playerRepository.findOneByLastNameIgnoreCase(lastName);
        if (playerDelete.isEmpty()){
            throw new PlayerNotFoundException(lastName);

        }
        playerRepository.delete(playerDelete.get());
        RankingCalculator rankingCalculator = new RankingCalculator(playerRepository.findAll());
        List<PlayerEntity> newRanking = rankingCalculator.getNewPlayersRanking();
        playerRepository.saveAll(newRanking);

    }


}
