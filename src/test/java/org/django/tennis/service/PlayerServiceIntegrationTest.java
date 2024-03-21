package org.django.tennis.service;

import org.assertj.core.groups.Tuple;
import org.django.tennis.entities.Player;
import org.django.tennis.exception.PlayerNotFoundException;
import org.django.tennis.model.PlayerToRegister;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class PlayerServiceIntegrationTest {

    @Autowired
    private PlayerService playerService;


    @Test
    void shouldCreatePlayer(){
        //Given
        PlayerToRegister playerTosave = new PlayerToRegister(
                "John",
                "Doe",
                LocalDate.of(2000, Month.JANUARY, 1),
                10000
        );

        //When

        playerService.create(playerTosave);
        Player createdPlayer = playerService.getByLastName("doe");

        //Then
        assertThat(createdPlayer.firstName()).isEqualTo("John");
        assertThat(createdPlayer.lastName()).isEqualTo("Doe");
        assertThat(createdPlayer.birthDate()).isEqualTo(LocalDate.of(2000,Month.JANUARY,1));
        assertThat(createdPlayer.rank().points()).isEqualTo(10000);
        assertThat(createdPlayer.rank().position()).isEqualTo(1);
    }

    @Test
    void shouldUpdatePlayer(){
        // Given
        PlayerToRegister playerToSave = new PlayerToRegister(
                "Rafael",
                "NadalTest",
                LocalDate.of(1986, Month.JUNE, 3),
                1000
        );

        // When
        playerService.update(playerToSave);
        Player updatedPlayer = playerService.getByLastName("NadalTest");

        // Then
        assertThat(updatedPlayer.rank().position()).isEqualTo(3);

    }
    @Test
    void shouldDeletePlayer() {
        // Given
        String playerToDelete = "DjokovicTest";

        // When
        playerService.delete(playerToDelete);
        List<Player> allPlayers = playerService.getAllPlayers();

        // Then
        assertThat(allPlayers)
                .extracting("lastName", "rank.position")
                .containsExactly(Tuple.tuple("NadalTest", 1), Tuple.tuple("FedererTest", 2));
    }

    @Test
     void shouldFailToDeletePlayer_WhenPlayerDoesNotExist() {
        // Given
        String playerToDelete = "DoeTest";

        // When / Then
        Exception exception = assertThrows(PlayerNotFoundException.class, () -> {
            playerService.delete(playerToDelete);
        });
        assertThat(exception.getMessage()).isEqualTo("Player with last name " + playerToDelete + " could not be found");
    }
}
