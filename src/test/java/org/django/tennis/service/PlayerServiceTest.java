package org.django.tennis.service;

import org.django.tennis.data.PlayerList;
import org.django.tennis.entities.Player;
import org.django.tennis.exception.PlayerNotFoundException;
import org.django.tennis.repository.PlayerRepository;
import org.django.tennis.service.impl.PlayerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PlayerServiceTest {

    @Mock
    private PlayerRepository  playerRepository;

    private PlayerService playerService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        playerService = new PlayerServiceImpl(playerRepository);
    }

    @Test
    void shouldReturnPlayersRanking(){

        //Given
        Mockito.when(playerRepository.findAll()).thenReturn(PlayerList.ALL);
        //When
        List<Player> allPlayers = playerService.getAllPlayers();
        //Then
        assertThat(allPlayers)
                .extracting("lastName")
                .containsExactly("Nadal", "Djokovic", "Federer","Murray");

    }

    @Test
    void shouldRetrievePlayer(){
        //Given
        String playerRetrieve = "Nadal";
        Mockito.when(playerRepository.findOneByLastNameIgnoreCase(playerRetrieve)).thenReturn(Optional.of(PlayerList.RAFAEL_NADAL));

        //When
        Player player = playerService.getByLastName(playerRetrieve);

        //Then
        assertThat(player.lastName()).isEqualTo("Nadal");
        assertThat(player.firstName()).isEqualTo("Rafael");
        assertThat(player.rank().position()).isEqualTo(1);
    }

    @Test
    void shouldFailToRetrieve_WhenPlayerDoesNotExist(){
        //Given
        String unknwonPlayer = "doe";
        Mockito.when(playerRepository.findOneByLastNameIgnoreCase(unknwonPlayer)).thenReturn(Optional.empty());

        //When / Then
        Exception exception = assertThrows(PlayerNotFoundException.class,() -> {
            playerService.getByLastName(unknwonPlayer);
        });

        assertThat(exception.getMessage()).isEqualTo("Player with last name " + unknwonPlayer + " could not be found");
    }
}
