package org.django.tennis.rest;

import org.django.tennis.data.PlayerListBis;
import org.django.tennis.exception.PlayerNotFoundException;
import org.django.tennis.service.PlayerService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PlayerController.class)
class PlayerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlayerService playerService;

    @Test
     void shouldListAllPlayers() throws Exception {
        // Given
        when(playerService.getAllPlayers()).thenReturn(PlayerListBis.ALL);

        // When / Then
        mockMvc.perform(get("/players"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].lastName", CoreMatchers.is("Nadal")))
                .andExpect(jsonPath("$[1].lastName", CoreMatchers.is("Djokovic")))
                .andExpect(jsonPath("$[2].lastName", CoreMatchers.is("Federer")))
                .andExpect(jsonPath("$[3].lastName", CoreMatchers.is("Murray")));
    }

    @Test
    public void shouldRetrievePlayer() throws Exception {
        // Given
        String playerToRetrieve = "nadal";
        when(playerService.getByLastName(playerToRetrieve)).thenReturn(PlayerListBis.RAFAEL_NADAL);

        // When / Then
        mockMvc.perform(get("/players/nadal"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName", CoreMatchers.is("Nadal")))
                .andExpect(jsonPath("$.rank.position", CoreMatchers.is(1)));
    }

    @Test
     void shouldReturn404NotFound_WhenPlayerDoesNotExist() throws Exception {
        // Given
        String playerToRetrieve = "doe";
        when(playerService.getByLastName(playerToRetrieve)).thenThrow(new PlayerNotFoundException("Player doe does not exist"));

        // When / Then
        mockMvc.perform(get("/players/doe"))
                .andExpect(status().isNotFound());
    }
}
