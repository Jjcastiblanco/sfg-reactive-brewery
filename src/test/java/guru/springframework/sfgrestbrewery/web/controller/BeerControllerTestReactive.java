package guru.springframework.sfgrestbrewery.web.controller;

import guru.springframework.sfgrestbrewery.bootstrap.BeerLoader;
import guru.springframework.sfgrestbrewery.services.BeerService;
import guru.springframework.sfgrestbrewery.web.model.BeerDto;
import guru.springframework.sfgrestbrewery.web.model.BeerPagedList;
import io.netty.handler.codec.string.LineSeparator;
import org.assertj.core.util.Arrays;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.lang.reflect.Array;
import java.util.List;
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
        BDDMockito.given(beerService.getById(Mockito.any(), Mockito.any())).willReturn(Mono.just(validBeer));

        webTestClient.get()
                .uri("/api/v1/beer/" + uuid)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BeerDto.class)
                .value(BeerDto::getBeerName, Matchers.equalTo(validBeer.getBeerName()));
    }

    @Test
    void getBeerUpc() {
        BDDMockito.given(beerService.getByUpc(Mockito.anyString())).willReturn(Mono.just(validBeer));

        webTestClient.get()
                .uri("/api/v1/beerUpc/" + validBeer.getUpc())
                .exchange()
                .expectStatus().isOk()
                .expectBody(BeerDto.class)
                .value(BeerDto::getBeerName, Matchers.equalTo(validBeer.getBeerName()));
    }

    @Test
    void getListBeers() {
        List<BeerDto> beerDtos = List.of(validBeer);
        BeerPagedList beerPagedList = new BeerPagedList(beerDtos, PageRequest.of(1, 1), beerDtos.size());
        BDDMockito.given(beerService.listBeers(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).willReturn(Mono.just(beerPagedList));
        webTestClient.get()
                .uri("/api/v1/beer")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(BeerPagedList.class);
    }
}