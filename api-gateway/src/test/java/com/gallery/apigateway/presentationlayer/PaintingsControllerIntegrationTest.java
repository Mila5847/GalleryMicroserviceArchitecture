package com.gallery.apigateway.presentationlayer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class PaintingsControllerIntegrationTest {

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


    @Test
    public void testGetPaintingsInGallery() throws JsonProcessingException {
        String galleryId = "123";

        // Mock the response from the service
        PaintingResponseModel paintingResponseModel1 = PaintingResponseModel.builder()
                .paintingId("5678")
                .title("paintingName")
                .yearCreated(2020)
                .painterId("1111")
                .galleryId(galleryId)
                .build();

        PaintingResponseModel paintingResponseModel2 = PaintingResponseModel.builder()
                .paintingId("5678")
                .title("paintingName")
                .yearCreated(2020)
                .painterId("1111")
                .galleryId(galleryId)
                .build();

        PaintingResponseModel[] paintingResponses = new PaintingResponseModel[]{paintingResponseModel1, paintingResponseModel2};

        mockRestServiceServer.expect(requestTo("http://localhost:7002/api/v1/galleries/" + galleryId + "/paintings"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(paintingResponses)));

        // Call the controller method
        String url = "/api/v1/galleries/" + galleryId + "/paintings";
        webTestClient.get()
                .uri(url)
                .exchange()
                .expectStatus().isOk()
                .expectBody(PaintingResponseModel[].class)
                .value(result -> assertThat(result).isEqualTo(paintingResponses));

        // Verify the mock server received the request
        mockRestServiceServer.verify();
    }

    @Test
    public void testGetPaintingAggregateByIdInGallery() throws JsonProcessingException {
        String galleryId = "123";
        String paintingId = "456";
        String painterId = "1111";

        PaintingResponseModel paintingResponseModel = PaintingResponseModel.builder()
                .paintingId(paintingId)
                .title("paintingName")
                .yearCreated(2020)
                .painterId(painterId)
                .galleryId(galleryId)
                .build();

        PainterResponseModel painterResponseModel = PainterResponseModel.builder()
                .painterId(painterId)
                .name("Mila")
                .origin("Bulgaria")
                .birthDate("01/01/1990")
                .deathDate("01/01/1990")
                .build();

        PaintingPainterResponseModel expectedResponse = new PaintingPainterResponseModel(paintingResponseModel, painterResponseModel);
        mockRestServiceServer.expect(requestTo("http://localhost:7002/api/v1/galleries/" + galleryId + "/paintings/" + paintingId))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(mapper.writeValueAsString(expectedResponse), MediaType.APPLICATION_JSON));


        // Call the controller method
        webTestClient.get()
                .uri("/api/v1/galleries/" + galleryId + "/paintings/" + paintingId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(PaintingPainterResponseModel.class)
                .value(result -> assertThat(result).isEqualTo(expectedResponse));

        // Verify the mock server received the request
        mockRestServiceServer.verify();
    }

    @Test
    public void testGetPaintingsByPainterIdInGallery() throws JsonProcessingException {
        String galleryId = "1234";
        String paintingId = "5678";
        String painterId = "1111";

        PainterResponseModel painterResponseModel = PainterResponseModel.builder()
                .painterId(painterId)
                .name("Mila")
                .origin("Bulgaria")
                .birthDate("01/01/1990")
                .deathDate("01/01/1990")
                .build();

        List<PaintingResponseModel> paintingResponseModels = new ArrayList<>();
        paintingResponseModels.add(PaintingResponseModel.builder()
                .paintingId(paintingId)
                .title("paintingName")
                .yearCreated(2020)
                .painterId(painterId)
                .galleryId(galleryId)
                .build());

        PaintingsOfPainterResponseModel expectedResponse = new PaintingsOfPainterResponseModel(painterId, painterResponseModel, paintingResponseModels);
        mockRestServiceServer.expect(requestTo("http://localhost:7002/api/v1/galleries/" + galleryId + "/painters/" + painterId + "/paintings"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(mapper.writeValueAsString(expectedResponse), MediaType.APPLICATION_JSON));

        // Call the controller method
        webTestClient.get()
                .uri("/api/v1/galleries/" + galleryId + "/painters/" + painterId + "/paintings")
                .exchange()
                .expectStatus().isOk()
                .expectBody(PaintingsOfPainterResponseModel.class)
                .value(result -> assertThat(result).isEqualTo(expectedResponse));

        // Verify the mock server received the request
        mockRestServiceServer.verify();
    }

    @Test
    public void testAddPaintingInGallery() throws JsonProcessingException {
        String galleryId = "123";
        String paintingId = "456";
        String painterId = "1111";
        PaintingRequestModel paintingRequest = PaintingRequestModel.builder()
                .title("paintingName")
                .yearCreated(2020)
                .painterId("1111")
                .galleryId(galleryId)
                .build();

        PaintingResponseModel paintingResponseModel = PaintingResponseModel.builder()
                .paintingId(paintingId)
                .title("paintingName")
                .yearCreated(2020)
                .painterId(painterId)
                .galleryId(galleryId)
                .build();

        PainterResponseModel painterResponseModel = PainterResponseModel.builder()
                .painterId(painterId)
                .name("Mila")
                .origin("Bulgaria")
                .birthDate("01/01/1990")
                .deathDate("01/01/1990")
                .build();

        PaintingPainterResponseModel expectedResponse = new PaintingPainterResponseModel(paintingResponseModel, painterResponseModel);
        mockRestServiceServer.expect(requestTo("http://localhost:7002/api/v1/galleries/" + galleryId + "/paintings"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().json(mapper.writeValueAsString(paintingRequest)))
                .andRespond(withSuccess(mapper.writeValueAsString(expectedResponse), MediaType.APPLICATION_JSON));

        // Call the controller method
        webTestClient.post()
                .uri("/api/v1/galleries/{galleryId}/paintings", galleryId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(paintingRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(PaintingPainterResponseModel.class)
                .value(result -> assertThat(result).isEqualTo(expectedResponse));

        // Verify the mock server received the request
        mockRestServiceServer.verify();
    }

    @Test
    public void testAddPainterToPaintingInGallery() throws JsonProcessingException {
        String galleryId = "123";
        String paintingId = "456";
        PainterRequestModel painterRequestModel = PainterRequestModel.builder()
                .name("Mila")
                .origin("Bulgaria")
                .birthDate("01/01/1990")
                .deathDate("01/01/1990")
                .build();

        PaintingPainterResponseModel paintingResponse = new PaintingPainterResponseModel();

        mockRestServiceServer.expect(requestTo("http://localhost:7002/api/v1/galleries/" + galleryId + "/paintings/" + paintingId + "/painters"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().json(mapper.writeValueAsString(painterRequestModel)))
                .andRespond(withSuccess(mapper.writeValueAsString(paintingResponse), MediaType.APPLICATION_JSON));

        // Call the controller method
        webTestClient.post()
                .uri("/api/v1/galleries/{galleryId}/paintings/{paintingId}/painters", galleryId, paintingId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(painterRequestModel)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(PaintingPainterResponseModel.class)
                .value(result -> assertThat(result).isEqualTo(paintingResponse));

        // Verify the mock server received the request
        mockRestServiceServer.verify();
    }


    @Test
    public void testUpdatePaintingInGallery() throws JsonProcessingException {
        String galleryId = "123";
        String paintingId = "456";
        PaintingRequestModel paintingRequestModel = PaintingRequestModel.builder()
                .title("paintingNameUpdated")
                .yearCreated(2020)
                .painterId("1111")
                .galleryId(galleryId)
                .build();

        mockRestServiceServer.expect(requestTo("http://localhost:7002/api/v1/galleries/" + galleryId + "/paintings/" + paintingId))
                .andExpect(method(HttpMethod.PUT))
                .andExpect(content().json(mapper.writeValueAsString(paintingRequestModel)))
                .andRespond(withSuccess());

        // Call the controller method
        webTestClient.put()
                .uri("/api/v1/galleries/{galleryId}/paintings/{paintingId}", galleryId, paintingId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(paintingRequestModel)
                .exchange()
                .expectStatus().isNoContent()
                .expectBody(Void.class);

        // Verify the mock server received the request
        mockRestServiceServer.verify();
    }

    @Test
    public void testUpdatePainterOfPaintingInGallery() throws JsonProcessingException {
        String galleryId = "1234";
        String paintingId = "5678";
        String painterId = "1111";
        PainterRequestModel painterRequestModel = PainterRequestModel.builder()
                .name("Mila")
                .origin("Canada")
                .birthDate("01/01/1990")
                .deathDate("01/01/1990")
                .build();


        // Mock the response from the service
        mockRestServiceServer.expect(requestTo("http://localhost:7002/api/v1/galleries/" + galleryId + "/paintings/" + paintingId + "/painters/" + painterId))
                .andExpect(method(HttpMethod.PUT))
                .andExpect(content().json(mapper.writeValueAsString(painterRequestModel)))
                .andRespond(withSuccess());

        // Call the controller method
        webTestClient.put()
                .uri("/api/v1/galleries/{galleryId}/paintings/{paintingId}/painters/{painterId}", galleryId, paintingId, painterId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(painterRequestModel)
                .exchange()
                .expectStatus().isNoContent()
                .expectBody(Void.class);

        // Verify the mock server received the request
        mockRestServiceServer.verify();
    }

    @Test
    public void testRemovePaintingByIdInGallery() {
        String galleryId = "123";
        String paintingId = "456";

        // Mock the response from the service
        mockRestServiceServer.expect(requestTo("http://localhost:7002/api/v1/galleries/" + galleryId + "/paintings/" + paintingId))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withSuccess());

        // Call the controller method
        webTestClient.delete()
                .uri("/api/v1/galleries/{galleryId}/paintings/{paintingId}", galleryId, paintingId)
                .exchange()
                .expectStatus().isNoContent()
                .expectBody(Void.class);

        // Verify the mock server received the request
        mockRestServiceServer.verify();
    }

    @Test
    public void testRemovePainterFromPaintingInGallery() {
        String galleryId = "123";
        String paintingId = "456";
        String painterId = "789";

        // Mock the response from the service
        mockRestServiceServer.expect(requestTo("http://localhost:7002/api/v1/galleries/" + galleryId + "/paintings/" + paintingId + "/painters/" + painterId))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withSuccess());

        // Call the controller method
        webTestClient.delete()
                .uri("/api/v1/galleries/{galleryId}/paintings/{paintingId}/painters/{painterId}", galleryId, paintingId, painterId)
                .exchange()
                .expectStatus().isNoContent()
                .expectBody(Void.class);

        // Verify the mock server received the request
        mockRestServiceServer.verify();
    }



}