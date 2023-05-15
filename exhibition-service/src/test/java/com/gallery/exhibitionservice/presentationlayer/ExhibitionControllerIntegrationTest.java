package com.gallery.exhibitionservice.presentationlayer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gallery.exhibitionservice.datalayer.Exhibition;
import com.gallery.exhibitionservice.domainclientlayer.GalleryResponseModel;
import com.gallery.exhibitionservice.domainclientlayer.PaintingResponseModel;
import com.gallery.exhibitionservice.domainclientlayer.SculptureRequestModel;
import com.gallery.exhibitionservice.domainclientlayer.SculptureResponseModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class ExhibitionControllerIntegrationTest {

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    RestTemplate restTemplate;

    private MockRestServiceServer mockRestServiceServer;
    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        mockRestServiceServer = MockRestServiceServer.createServer(restTemplate);
    }

    // GET ALL EXHIBITIONS
    @Test
    public void whenGetAllExhibitions_thenReturnAllExhibitions() throws JsonProcessingException, URISyntaxException {
        PaintingResponseModel painting1 = new PaintingResponseModel("3ed9654a-b773-4aa0-ae6b-b22afb636c8e", "The Starry Night", 1889, "4c80444-5acf-4d57-8902-9f55255e9e55", "ea85d3ba-d708-4ff3-bbbb-dd9c5c77b8e8");
        PaintingResponseModel painting2 = new PaintingResponseModel("b5fff508-79d2-4fdc-aecf-5a7b504f9fcc", "Sunflowers", 1888, "4c80444-5acf-4d57-8902-9f55255e9e55", "ea85d3ba-d708-4ff3-bbbb-dd9c5c77b8e8");
        SculptureResponseModel sculpture1 = new SculptureResponseModel("acf18748-b00c-4f3a-9d0b-b1b1fdf9c240", "Vase", "Wood", "Rough", "ea85d3ba-d708-4ff3-bbbb-dd9c5c77b8e8");
        SculptureResponseModel sculpture2 = new SculptureResponseModel("04875097-6dab-451c-b08d-b1e48da9ded8", "Face", "Stone", "Smooth", "ea85d3ba-d708-4ff3-bbbb-dd9c5c77b8e8");
        mockRestServiceServer.expect(ExpectedCount.once(),
                requestTo(new URI("http://localhost:7001/api/v1/exhibitions")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(Arrays.asList(
                                new ExhibitionResponseModel("5ea10d1d-46fb-4374-a39c-27d643aa37e3", "ea85d3ba-d708-4ff3-bbbb-dd9c5c77b8e8", "Art Gallery of Ontario", "Ontario's Exhibition", 203, 60, "Monday", "Wednesday",
                                        Arrays.asList(painting1, painting2), Arrays.asList(sculpture1, sculpture2))))));

        String url = "/api/v1/exhibitions";
        webTestClient.get().uri(url)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ExhibitionResponseModel.class)
                .value(responseModels -> {
                    responseModels.forEach(rsp -> {
                                assertThat(rsp.getExhibitionId()).isNotNull();
                                assertThat(rsp.getExhibitionName()).isEqualTo("Ontario's Exhibition");
                                assertThat(rsp.getRoomNumber()).isEqualTo(203);
                                assertThat(rsp.getDuration()).isEqualTo(60);
                                assertThat(rsp.getStartDay()).isEqualTo("Monday");
                                assertThat(rsp.getEndDay()).isEqualTo("Wednesday");
                                assertThat(rsp.getPaintings()).isNotNull();
                                assertThat(rsp.getSculptures()).isNotNull();
                                assertThat(rsp.getPaintings().size()).isEqualTo(2);
                                assertThat(rsp.getSculptures().size()).isEqualTo(2);
                            }
                    );
                });
    }

    // GET EXHIBITION BY ID
    @Test
    public void whenGetExhibitionById_thenReturnExhibition() throws JsonProcessingException, URISyntaxException {
        mockRestServiceServer.expect(ExpectedCount.once(),
                requestTo(new URI("http://localhost:7004/api/v1/exhibitions/5ea10d1d-46fb-4374-a39c-27d643aa37e3")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(new ExhibitionResponseModel("5ea10d1d-46fb-4374-a39c-27d643aa37e3", "ea85d3ba-d708-4ff3-bbbb-dd9c5c77b8e8", "Art Gallery of Ontario", "Ontario's Exhibition", 203, 60, "Monday", "Wednesday",
                                Arrays.asList(new PaintingResponseModel("3ed9654a-b773-4aa0-ae6b-b22afb636c8e", "The Starry Night", 1889, "4c80444-5acf-4d57-8902-9f55255e9e55", "ea85d3ba-d708-4ff3-bbbb-dd9c5c77b8e8"),
                                        new PaintingResponseModel("b5fff508-79d2-4fdc-aecf-5a7b504f9fcc", "Sunflowers", 1888, "4c80444-5acf-4d57-8902-9f55255e9e55", "ea85d3ba-d708-4ff3-bbbb-dd9c5c77b8e8")),
                                Arrays.asList(new SculptureResponseModel("acf18748-b00c-4f3a-9d0b-b1b1fdf9c240", "Vase", "Wood", "Rough", "ea85d3ba-d708-4ff3-bbbb-dd9c5c77b8e8"),
                                        new SculptureResponseModel("04875097-6dab-451c-b08d-b1e48da9ded8", "Face", "Stone", "Smooth", "ea85d3ba-d708-4ff3-bbbb-dd9c5c77b8e8"))))));


        String url = "/api/v1/exhibitions/5ea10d1d-46fb-4374-a39c-27d643aa37e3";
        webTestClient.get().uri(url)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ExhibitionResponseModel.class)
                .value(responseModel -> {
                    assertThat(responseModel.getExhibitionId()).isNotNull();
                    assertThat(responseModel.getExhibitionName()).isEqualTo("Ontario's Exhibition");
                    assertThat(responseModel.getRoomNumber()).isEqualTo(203);
                    assertThat(responseModel.getDuration()).isEqualTo(60);
                    assertThat(responseModel.getStartDay()).isEqualTo("Monday");
                    assertThat(responseModel.getEndDay()).isEqualTo("Wednesday");
                    assertThat(responseModel.getPaintings()).isNotNull();
                    assertThat(responseModel.getSculptures()).isNotNull();
                    assertThat(responseModel.getPaintings().size()).isEqualTo(2);
                    assertThat(responseModel.getSculptures().size()).isEqualTo(2);
                });
    }

    @Test
    public void whenGetInvalidExhibitionById_thenReturnNotFound() throws URISyntaxException {
        mockRestServiceServer.expect(ExpectedCount.once(),
                requestTo(new URI("http://localhost:7004/api/v1/exhibitions/5ea10d1d-46fb-4374-a39c-27d643aa37e4")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));

        String url = "/api/v1/exhibitions/5ea10d1d-46fb-4374-a39c-27d643aa37e4";
        webTestClient.get().uri(url)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.message").isEqualTo("Unknown exhibition id 5ea10d1d-46fb-4374-a39c-27d643aa37e4");
    }

    @Test
    public void whenCreateExhibition_thenReturnExhibition() throws URISyntaxException, JsonProcessingException {
        // Get gallery

        GalleryResponseModel galleryResponseModel = GalleryResponseModel.builder()
                .galleryId("ea85d3ba-d708-4ff3-bbbb-dd9c5c77b8e8")
                .name("Art Gallery of Montreal")
                .openFrom("Monday")
                .openUntil("Friday")
                .build();

        PaintingResponseModel paintingResponseModel = PaintingResponseModel.builder()
                .paintingId("3ed9654a-b773-4aa0-ae6b-b22afb636c8d")
                .title("The Night")
                .yearCreated(1889)
                .painterId("4c80444-5acf-4d57-8902-9f55255e9e55")
                .galleryId("ea85d3ba-d708-4ff3-bbbb-dd9c5c77b8e8")
                .build();

        SculptureResponseModel sculptureResponseModel = SculptureResponseModel.builder()
                .sculptureId("acf18748-b00c-4f3a-9d0b-b1b1fdf9c244")
                .title("Chair")
                .material("Wood")
                .texture("Rough")
                .galleryId("ea85d3ba-d708-4ff3-bbbb-dd9c5c77b8e8")
                .build();

        SculptureResponseModel sculptureResponseModel2 = SculptureResponseModel.builder()
                .sculptureId("04875097-6dab-451c-b08d-b1e48da9ded4")
                .title("Face")
                .material("Stone")
                .texture("Smooth")
                .galleryId("ea85d3ba-d708-4ff3-bbbb-dd9c5c77b8e8")
                .build();

        ExhibitionRequestModel exhibitionRequestModel = ExhibitionRequestModel.builder()
                .exhibitionName("Ontario's Exhibition")
                .roomNumber(203)
                .duration(60)
                .startDay("Monday")
                .endDay("Wednesday")
                .paintings(Arrays.asList(paintingResponseModel))
                .sculptures(Arrays.asList(sculptureResponseModel, sculptureResponseModel2))
                .build();

        mockRestServiceServer.expect(ExpectedCount.once(),
                        requestTo(new URI("http://localhost:7001/api/v1/galleries/ea85d3ba-d708-4ff3-bbbb-dd9c5c77b8e8")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(galleryResponseModel)));

        webTestClient.post().uri("/api/v1/exhibitions/galleries/ea85d3ba-d708-4ff3-bbbb-dd9c5c77b8e8")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(exhibitionRequestModel)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ExhibitionResponseModel.class)
                .value(responseModel -> {
                    assertThat(responseModel.getExhibitionId()).isNotNull();
                    assertThat(responseModel.getExhibitionName()).isEqualTo("Ontario's Exhibition");
                    assertThat(responseModel.getRoomNumber()).isEqualTo(203);
                    assertThat(responseModel.getDuration()).isEqualTo(60);
                    assertThat(responseModel.getStartDay()).isEqualTo("Monday");
                    assertThat(responseModel.getEndDay()).isEqualTo("Wednesday");
                    assertThat(responseModel.getPaintings().size()).isEqualTo(1);
                    assertThat(responseModel.getSculptures().size()).isEqualTo(2);
                });
    }
}