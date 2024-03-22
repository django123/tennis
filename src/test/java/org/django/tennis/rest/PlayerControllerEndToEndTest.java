package org.django.tennis.rest;

import org.assertj.core.groups.Tuple;
import org.django.tennis.entities.Player;
import org.django.tennis.model.PlayerToRegister;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class PlayerControllerEndToEndTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;


    @Test
    void shouldCreatePlayer(){
        //Given
        PlayerToRegister playerToRegister = new PlayerToRegister(
                "Carlos",
                "Alcaraz",
                LocalDate.of(2003, Month.MAY, 5),
                4500
        );
        //When

        String url = "http://localhost:" + port + "/players";
        HttpEntity<PlayerToRegister>request = new HttpEntity<>(playerToRegister);
        ResponseEntity<Player> playerResponseEntity = this.restTemplate.exchange(url, HttpMethod.POST, request,Player.class);

        //Then

        assertThat(playerResponseEntity.getBody().lastName()).isEqualTo("Alcaraz");
        assertThat(playerResponseEntity.getBody().rank().position()).isEqualTo(2);
    }

    @Test
    void shouldFailToCreatePlayer_WhenPlayerToCreateIsInvalid(){
        //Given
        PlayerToRegister playerToRegister = new PlayerToRegister(
                "Carlos",
                null,
                LocalDate.of(2003, Month.MAY, 5),
                4500
        );

        // When
        String url = "http://localhost:" + port + "/players";
        HttpEntity<PlayerToRegister>request = new HttpEntity<>(playerToRegister);
        ResponseEntity<Player> playerResponseEntity = this.restTemplate.exchange(url, HttpMethod.POST, request,Player.class);

        //Then
        assertThat(playerResponseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    }

    @Test
    void shouldUpdatePlayerRanking() {
        // Given
        PlayerToRegister playerToUpdate = new PlayerToRegister(
                "Rafael",
                "NadalTest",
                LocalDate.of(1986, Month.JUNE, 3),
                1000
        );

        // When
        String url = "http://localhost:" + port + "/players";
        HttpEntity<PlayerToRegister> request = new HttpEntity<>(playerToUpdate);
        ResponseEntity<Player> playerResponseEntity = this.restTemplate.exchange(url, HttpMethod.PUT, request, Player.class);

        // Then
        assertThat(playerResponseEntity.getBody().lastName()).isEqualTo("NadalTest");
        assertThat(playerResponseEntity.getBody().rank().position()).isEqualTo(3);
    }


    @Test
    void shouldDeletePlayer() {
        // Given / When
        String url = "http://localhost:" + port + "/players";
        this.restTemplate.exchange(url + "/djokovictest", HttpMethod.DELETE, null, Player.class);
        HttpEntity<List<Player>> allPlayersResponseEntity = this.restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Player>>() {
                }
        );

        // Then
        assertThat(allPlayersResponseEntity.getBody())
                .extracting("lastName", "rank.position")
                .containsExactly(Tuple.tuple("NadalTest", 1), Tuple.tuple("FedererTest", 2));
    }
}
