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
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class GalleriesControllerIntegrationTest {

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
    public void testGetGallery() throws Exception {
        String galleryId = "123";

        // Mock the response from the service
        GalleryResponseModel galleryResponse = GalleryResponseModel.builder()
                .galleryId(galleryId)
                .name("gallery1")
                .openFrom("Monday")
                .openUntil("Friday")
                .streetAddress("1234 Main St")
                .city("City")
                .province("Province")
                .postalCode("A1A 1A1")
                .country("Country")
                .build();
        ResponseEntity<GalleryResponseModel> responseEntity = ResponseEntity.ok(galleryResponse);

        mockRestServiceServer.expect(ExpectedCount.once(),
                requestTo("http://localhost:7001/api/v1/galleries/" + galleryId))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(galleryResponse)));

        // Call the controller method
        String url = "/api/v1/galleries/" + galleryId;
        webTestClient.get()
                .uri(url, galleryId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(GalleryResponseModel.class)
                .value(result -> assertThat(result).isEqualTo(galleryResponse));

        // Verify the mock server received the request
        mockRestServiceServer.verify();
    }

    @Test
    public void testGetGalleries() throws JsonProcessingException {
        // Mock the response from the service
        GalleryResponseModel galleryResponseModel1 = GalleryResponseModel.builder()
                .galleryId("1234")
                .name("gallery1")
                .openFrom("Monday")
                .openUntil("Friday")
                .streetAddress("1234 Main St")
                .city("City")
                .province("Province")
                .postalCode("A1A 1A1")
                .country("Country")
                .build();

        GalleryResponseModel galleryResponseModel2 = GalleryResponseModel.builder()
                .galleryId("5678")
                .name("gallery2")
                .openFrom("Monday")
                .openUntil("Friday")
                .streetAddress("1234 Main St")
                .city("City")
                .province("Province")
                .postalCode("A1A 1A1")
                .country("Country")
                .build();
        GalleryResponseModel[] galleryResponses = new GalleryResponseModel[]{galleryResponseModel1, galleryResponseModel2};

        mockRestServiceServer.expect(requestTo("http://localhost:7001/api/v1/galleries"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(galleryResponses)));

        // Call the controller method
        webTestClient.get()
                .uri("/api/v1/galleries")
                .exchange()
                .expectStatus().isOk()
                .expectBody(GalleryResponseModel[].class)
                .value(result -> assertThat(result).isEqualTo(galleryResponses));

        // Verify the mock server received the request
        mockRestServiceServer.verify();
    }

    @Test
    public void testAddGallery() throws JsonProcessingException {
        // Mock the response from the service
        GalleryRequestModel galleryRequest = GalleryRequestModel.builder()
                .name("gallery1")
                .openFrom("Monday")
                .openUntil("Friday")
                .streetAddress("1234 Main St")
                .city("City")
                .province("Province")
                .postalCode("A1A 1A1")
                .country("Country")
                .build();
        GalleryResponseModel galleryResponse = GalleryResponseModel.builder()
                .galleryId("1234")
                .name("gallery1")
                .openFrom("Monday")
                .openUntil("Friday")
                .streetAddress("1234 Main St")
                .city("City")
                .province("Province")
                .postalCode("A1A 1A1")
                .country("Country")
                .build();

        ResponseEntity<GalleryResponseModel> responseEntity = ResponseEntity.status(HttpStatus.CREATED).body(galleryResponse);
        mockRestServiceServer.expect(requestTo("http://localhost:7001/api/v1/galleries"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(mapper.writeValueAsString(galleryResponse), MediaType.APPLICATION_JSON));

        // Call the controller method
        webTestClient.post()
                .uri("/api/v1/galleries")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(galleryRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(GalleryResponseModel.class)
                .value(result -> assertThat(result).isEqualTo(galleryResponse));

        // Verify the mock server received the request
        mockRestServiceServer.verify();
    }

    @Test
    public void testUpdateGallery() throws JsonProcessingException {
        String galleryId = "1234";
        GalleryRequestModel galleryRequest = GalleryRequestModel.builder()
                .name("gallery1")
                .openFrom("Monday")
                .openUntil("Friday")
                .streetAddress("1234 Main St")
                .city("City")
                .province("Province")
                .postalCode("A1A 1A1")
                .country("Country")
                .build();

        // Mock the response from the service
        mockRestServiceServer.expect(requestTo("http://localhost:7001/api/v1/galleries/" + galleryId))
                .andExpect(method(HttpMethod.PUT))
                .andExpect(content().json(mapper.writeValueAsString(galleryRequest)))
                .andRespond(withNoContent());

        // Call the controller method
        String url = "/api/v1/galleries/" + galleryId;
        webTestClient.put()
                .uri(url, galleryId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(galleryRequest)
                .exchange()
                .expectStatus().isNoContent();

        // Verify the mock server received the request
        mockRestServiceServer.verify();
    }

    @Test
    public void testRemoveGallery() {
        String galleryId = "1234";

        // Mock the response from the service
        mockRestServiceServer.expect(requestTo("http://localhost:7001/api/v1/galleries/" + galleryId))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withNoContent());

        // Call the controller method
        String url = "/api/v1/galleries/" + galleryId;
        webTestClient.delete()
                .uri(url, galleryId)
                .exchange()
                .expectStatus().isNoContent();

        // Verify the mock server received the request
        mockRestServiceServer.verify();
    }




}