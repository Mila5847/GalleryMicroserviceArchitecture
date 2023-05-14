package com.gallery.exhibitionservice.presentationlayer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gallery.exhibitionservice.datalayer.Exhibition;
import com.gallery.exhibitionservice.domainclientlayer.GalleryResponseModel;
import com.gallery.exhibitionservice.domainclientlayer.PaintingResponseModel;
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
        GalleryResponseModel galleryResponseModel = GalleryResponseModel.builder()
                .galleryId("23456")
                .name("Quebec's Gallery")
                .openFrom("Wednesday 12:00 AM")
                .openUntil("Sunday 6:00 PM")
                .city("Brossard")
                .province("Quebec")
                .country("Canada")
                .postalCode("J4W 1W6")
                .build();

        PaintingResponseModel paintingResponseModel = PaintingResponseModel.builder()
                .paintingId("111111")
                .title("Painting 1")
                .yearCreated(2020)
                .painterId("111112")
                .galleryId("23456")
                .build();
        PaintingResponseModel paintingResponseModel2 = PaintingResponseModel.builder()
                .paintingId("222222")
                .title("Painting 1")
                .yearCreated(2020)
                .painterId("111112")
                .galleryId("23456")
                .build();
        List<PaintingResponseModel> paintings = new ArrayList<>(Arrays.asList(paintingResponseModel, paintingResponseModel2));

        SculptureResponseModel sculptureResponseModel = SculptureResponseModel.builder()
                .sculptureId("333333")
                .title("Sculpture 1")
                .material("Wood")
                .texture("Smooth")
                .galleryId("23456")
                .build();

        List<SculptureResponseModel> sculptures = new ArrayList<>(Arrays.asList(sculptureResponseModel));

        ExhibitionRequestModel exhibitionRequestModel = ExhibitionRequestModel.builder()
                .exhibitionName("Exhibition 1")
                .roomNumber(123)
                .duration(120)
                .startDay("Monday")
                .endDay("Saturday")
                .paintings(paintings)
                .sculptures(sculptures)
                .build();

        mockRestServiceServer.expect(ExpectedCount.once(),
                        requestTo(new URI("http://localhost:7001/api/v1/galleries/23456")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(galleryResponseModel)));

        mockRestServiceServer.expect(ExpectedCount.once(),
                        requestTo(new URI("http://localhost:7002/api/v1/23456/paintings/111111")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(paintingResponseModel)));

        mockRestServiceServer.expect(ExpectedCount.once(),
                        requestTo(new URI("http://localhost:7002/api/v1/23456/paintings/222222")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(paintingResponseModel2)));


        mockRestServiceServer.expect(ExpectedCount.once(),
                        requestTo(new URI("http://localhost:7003/api/v1/23456/sculptures/333333")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(sculptureResponseModel)));

        String url1 = "/api/v1/exhibitions/galleries/23456";
        webTestClient.post().uri(url1)
                .bodyValue(exhibitionRequestModel)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Exhibition.class);

        String url2 = "/api/v1/exhibitions";
        webTestClient.get().uri(url2)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Exhibition.class)
                .hasSize(2)
                .value(responseModels -> {
                    responseModels.forEach(rsp -> {
                                assertThat(rsp.getExhibitionName()).isIn("Ontario's Exhibition", "Exhibition 1");
                                assertThat(rsp.getRoomNumber()).isIn(203, 123);
                                assertThat(rsp.getDuration()).isIn(60, 120);
                                assertThat(rsp.getStartDay()).isIn("Monday", "Monday");
                                assertThat(rsp.getEndDay()).isIn("Wednesday", "Saturday");
                            }
                        );
                    });
    }

    // GET EXHIBITION BY ID
    @Test
    public void whenValidExhibitionId_thenExhibitionShouldBeFound() throws JsonProcessingException, URISyntaxException {
        String url2 = "/api/v1/exhibitions/5ea10d1d-46fb-4374-a39c-27d643aa37e3" ;
        webTestClient.get().uri(url2)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Exhibition.class)
                .hasSize(1)
                .value(responseModels -> {
            responseModels.forEach(rsp -> {
                        assertThat(rsp.getExhibitionName()).isEqualTo("Ontario's Exhibition");
                        assertThat(rsp.getRoomNumber()).isEqualTo(203);
                        assertThat(rsp.getDuration()).isEqualTo(60);
                        assertThat(rsp.getStartDay()).isEqualTo("Monday");
                        assertThat(rsp.getEndDay()).isEqualTo("Wednesday");
                    }
            );
        });
    }


    // POST EXHIBITION
    @Test
    public void whenGalleryAndPaintingsAndSculpturesAreValid_thenCreateExhibition() throws JsonProcessingException, URISyntaxException {
        // arrange
        GalleryResponseModel galleryResponseModel = GalleryResponseModel.builder()
                .galleryId("23456")
                .name("Quebec's Gallery")
                .openFrom("Wednesday 12:00 AM")
                .openUntil("Sunday 6:00 PM")
                .city("Brossard")
                .province("Quebec")
                .country("Canada")
                .postalCode("J4W 1W6")
                .build();

        PaintingResponseModel paintingResponseModel = PaintingResponseModel.builder()
                .paintingId("12341")
                .title("Painting 1")
                .yearCreated(2020)
                .painterId("12345")
                .galleryId("23456")
                .build();
        PaintingResponseModel paintingResponseModel2 = PaintingResponseModel.builder()
                .paintingId("12342")
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

        List<SculptureResponseModel> sculptures = new ArrayList<>(Arrays.asList(sculptureResponseModel));

        ExhibitionRequestModel exhibitionRequestModel = ExhibitionRequestModel.builder()
                .exhibitionName("Exhibition 1")
                .roomNumber(123)
                .duration(120)
                .startDay("Monday")
                .endDay("Saturday")
                .paintings(paintings)
                .sculptures(sculptures)
                .build();

        mockRestServiceServer.expect(ExpectedCount.once(),
                        requestTo(new URI("http://localhost:7001/api/v1/galleries/23456")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(galleryResponseModel)));

        mockRestServiceServer.expect(ExpectedCount.once(),
                        requestTo(new URI("http://localhost:7002/api/v1/23456/paintings/111111")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(paintingResponseModel)));

        mockRestServiceServer.expect(ExpectedCount.once(),
                        requestTo(new URI("http://localhost:7002/api/v1/23456/paintings/222222")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(paintingResponseModel2)));


        mockRestServiceServer.expect(ExpectedCount.once(),
                        requestTo(new URI("http://localhost:7003/api/v1/23456/sculptures/333333")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(sculptureResponseModel)));


        String url1 = "/api/v1/exhibitions/galleries/23456";
        webTestClient.post()
                .uri(url1)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(exhibitionRequestModel)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ExhibitionResponseModel.class)
                .value(response -> {
                    assertEquals(exhibitionRequestModel.getExhibitionName(), response.getExhibitionName());
                    assertEquals(exhibitionRequestModel.getRoomNumber(), response.getRoomNumber());
                    assertEquals(exhibitionRequestModel.getDuration(), response.getDuration());
                    assertEquals(exhibitionRequestModel.getStartDay(), response.getStartDay());
                    assertEquals(exhibitionRequestModel.getEndDay(), response.getEndDay());
                    assertEquals(exhibitionRequestModel.getPaintings().get(0).getPaintingId(), response.getPaintings().get(0).getPaintingId());
                    assertEquals(exhibitionRequestModel.getPaintings().get(0).getTitle(), response.getPaintings().get(0).getTitle());
                    assertEquals(exhibitionRequestModel.getPaintings().get(0).getYearCreated(), response.getPaintings().get(0).getYearCreated());
                    assertEquals(exhibitionRequestModel.getPaintings().get(0).getPainterId(), response.getPaintings().get(0).getPainterId());
                    assertEquals(exhibitionRequestModel.getPaintings().get(0).getGalleryId(), response.getPaintings().get(0).getGalleryId());
                    assertEquals(exhibitionRequestModel.getPaintings().get(1).getPaintingId(), response.getPaintings().get(1).getPaintingId());
                    assertEquals(exhibitionRequestModel.getPaintings().get(1).getTitle(), response.getPaintings().get(1).getTitle());
                    assertEquals(exhibitionRequestModel.getPaintings().get(1).getYearCreated(), response.getPaintings().get(1).getYearCreated());
                    assertEquals(exhibitionRequestModel.getPaintings().get(1).getPainterId(), response.getPaintings().get(1).getPainterId());
                    assertEquals(exhibitionRequestModel.getPaintings().get(1).getGalleryId(), response.getPaintings().get(1).getGalleryId());
                    assertEquals(exhibitionRequestModel.getSculptures().get(0).getSculptureId(), response.getSculptures().get(0).getSculptureId());
                    assertEquals(exhibitionRequestModel.getSculptures().get(0).getTitle(), response.getSculptures().get(0).getTitle());
                    assertEquals(exhibitionRequestModel.getSculptures().get(0).getMaterial(), response.getSculptures().get(0).getMaterial());
                    assertEquals(exhibitionRequestModel.getSculptures().get(0).getTexture(), response.getSculptures().get(0).getTexture());
                    assertEquals(exhibitionRequestModel.getSculptures().get(0).getGalleryId(), response.getSculptures().get(0).getGalleryId());
                });
    }

    // UPDATE EXHIBITION
    /*@Test
    public void whenExhibitionIsValid_thenUpdateExhibition() throws JsonProcessingException, URISyntaxException {
        GalleryResponseModel galleryResponseModel = GalleryResponseModel.builder()
                .galleryId("23456")
                .name("Quebec's Gallery")
                .openFrom("Wednesday 12:00 AM")
                .openUntil("Sunday 6:00 PM")
                .city("Brossard")
                .province("Quebec")
                .country("Canada")
                .postalCode("J4W 1W6")
                .build();

        PaintingResponseModel paintingResponseModel = PaintingResponseModel.builder()
                .paintingId("111111")
                .title("Painting 1")
                .yearCreated(2020)
                .painterId("111112")
                .galleryId("23456")
                .build();
        PaintingResponseModel paintingResponseModel2 = PaintingResponseModel.builder()
                .paintingId("222222")
                .title("Painting 1")
                .yearCreated(2020)
                .painterId("111112")
                .galleryId("23456")
                .build();
        List<PaintingResponseModel> paintings = new ArrayList<>(Arrays.asList(paintingResponseModel, paintingResponseModel2));

        SculptureResponseModel sculptureResponseModel = SculptureResponseModel.builder()
                .sculptureId("333333")
                .title("Sculpture 1")
                .material("Wood")
                .texture("Smooth")
                .galleryId("23456")
                .build();

        List<SculptureResponseModel> sculptures = new ArrayList<>(Arrays.asList(sculptureResponseModel));

        // create an exhibition
        ExhibitionRequestModel exhibitionRequestModel = ExhibitionRequestModel.builder()
                .exhibitionName("Exhibition 1")
                .roomNumber(123)
                .duration(120)
                .startDay("Monday")
                .endDay("Saturday")
                .paintings(paintings)
                .sculptures(sculptures)
                .build();

        mockRestServiceServer.expect(ExpectedCount.once(),
                        requestTo(new URI("http://localhost:7001/api/v1/galleries/23456")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(galleryResponseModel)));

                    String url2 = "/api/v1/exhibitions/5ea10d1d-46fb-4374-a39c-27d643aa37e3";
                    webTestClient.put().uri(url2)
                            .bodyValue(exhibitionRequestModel)
                            .exchange()
                            .expectStatus().isOk()
                            .expectBody(Exhibition.class);

    }*/

    // DELETE EXHIBITION
    @Test
    public void whenExhibitionIdIsValid_thenDeleteExhibition() throws URISyntaxException {
        mockRestServiceServer.expect(ExpectedCount.once(),
                        requestTo(new URI("http://localhost:7004/api/v1/exhibitions/5ea10d1d-46fb-4374-a39c-27d643aa37e3")))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withStatus(HttpStatus.OK));

        String url = "/api/v1/exhibitions/5ea10d1d-46fb-4374-a39c-27d643aa37e3";
        webTestClient.delete().uri(url)
                .exchange()
                .expectStatus().isNoContent();

        webTestClient.get().uri(url)
                .exchange()
                .expectStatus().isNotFound();
    }

}