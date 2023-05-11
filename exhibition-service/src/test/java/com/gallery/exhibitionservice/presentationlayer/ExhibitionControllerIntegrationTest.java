package com.gallery.exhibitionservice.presentationlayer;

import com.gallery.exhibitionservice.domainclientlayer.PaintingResponseModel;
import com.gallery.exhibitionservice.domainclientlayer.SculptureResponseModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class ExhibitionControllerIntegrationTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    void createExhibition() {
        PaintingResponseModel paintingResponseModel = PaintingResponseModel.builder()
                .paintingId("12345")
                .title("Painting 1")
                .yearCreated(2020)
                .painterId("12345")
                .galleryId("23456")
                .build();
        PaintingResponseModel paintingResponseModel2 = PaintingResponseModel.builder()
                .paintingId("12345")
                .title("Painting 1")
                .yearCreated(2020)
                .painterId("12345")
                .galleryId("23456")
                .build();
        List<PaintingResponseModel> paintings = new ArrayList<>(Arrays.asList(paintingResponseModel, paintingResponseModel2));

        SculptureResponseModel sculptureResponseModel = SculptureResponseModel.builder()
                .sculptureId("12345")
                .title("Sculpture 1")
                .material("Wood")
                .texture("Smooth")
                .galleryId("23456")
                .build();

        List<SculptureResponseModel> sculptures = new ArrayList<>(Arrays.asList(sculptureResponseModel, sculptureResponseModel));

        ExhibitionRequestModel exhibitionRequestModel = ExhibitionRequestModel.builder()
                .exhibitionName("Exhibition 1")
                .roomNumber(123)
                .duration(120)
                .startDay("Monday")
                .endDay("Wednesday")
                .paintings(paintings)
                .sculptures(sculptures)
                .build();

        webTestClient.post()
                .uri("api/v1/exhibitions/galleries/23456")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(exhibitionRequestModel)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ExhibitionResponseModel.class);

    }
}