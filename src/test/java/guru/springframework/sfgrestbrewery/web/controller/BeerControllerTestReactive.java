package guru.springframework.sfgrestbrewery.web.controller;

import guru.springframework.sfgrestbrewery.bootstrap.BeerLoader;
import guru.springframework.sfgrestbrewery.services.BeerService;
import guru.springframework.sfgrestbrewery.web.model.BeerDto;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@WebFluxTest(BeerController.class)
class BeerControllerTestReactive {

    @Autowired
    WebTestClient webTestClient;

    @MockBean
    BeerService beerService;

    BeerDto validBeer;


    @BeforeEach
    void setUp() {
        validBeer = BeerDto.builder()
                .beerName("Beer1")
                .beerStyle("PALE_ALE")
                .upc(BeerLoader.BEER_2_UPC)
                .build();
    }

    @Test
    void getBeerById() {
        UUID uuid = UUID.randomUUID();
        //BDDMockito sigue el estilo BDD (Behavior-Driven Development), que es más descriptivo y se alinea mejor con ciertas prácticas de desarrollo.
        BDDMockito.given(beerService.getById(Mockito.any(), Mockito.any())).willReturn(validBeer);

        webTestClient.get()
                .uri("/api/v1/beer/" + uuid)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BeerDto.class)
                .value(BeerDto::getBeerName, Matchers.equalTo(validBeer.getBeerName()));
    }
}