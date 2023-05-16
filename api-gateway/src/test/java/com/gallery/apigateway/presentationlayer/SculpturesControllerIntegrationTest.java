package com.gallery.apigateway.presentationlayer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class SculpturesControllerIntegrationTest {

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
    public void testGetSculptureInGallery() throws JsonProcessingException {
        String galleryId = "123";
        String sculptureId = "456";

        // Mock the response from the service
        SculptureResponseModel expectedSculptureResponseModel = SculptureResponseModel.builder()
                .sculptureId("123")
                .title("Title 1")
                .texture("Texture 1")
                .material("Material 1")
                .build();
        mockRestServiceServer.expect(requestTo("http://localhost:7003/api/v1/galleries/" + galleryId + "/sculptures/" + sculptureId))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(mapper.writeValueAsString(expectedSculptureResponseModel), MediaType.APPLICATION_JSON));

        // Call the controller method
        webTestClient.get()
                .uri("/api/v1/galleries/{galleryId}/sculptures/{sculptureId}", galleryId, sculptureId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(SculptureResponseModel.class)
                .value(response -> assertThat(response).isEqualTo(expectedSculptureResponseModel));

        // Verify the mock server received the request
        mockRestServiceServer.verify();
    }

    @Test
    public void testGetSculpturesInGallery() throws JsonProcessingException {
        String galleryId = "123";
        SculptureResponseModel[] expectedSculptureResponseModels = new SculptureResponseModel[2];
        expectedSculptureResponseModels[0] = SculptureResponseModel.builder()
                .sculptureId("123")
                .title("Title 1")
                .texture("Texture 1")
                .material("Material 1")
                .build();
        expectedSculptureResponseModels[1] = SculptureResponseModel.builder()
                .sculptureId("456")
                .title("Title 2")
                .texture("Texture 2")
                .material("Material 2")
                .build();
        mockRestServiceServer.expect(requestTo("http://localhost:7003/api/v1/galleries/" + galleryId + "/sculptures"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(mapper.writeValueAsString(expectedSculptureResponseModels), MediaType.APPLICATION_JSON));

        // Call the controller method
        webTestClient.get()
                .uri("/api/v1/galleries/{galleryId}/sculptures", galleryId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(SculptureResponseModel[].class)
                .value(response -> assertThat(response).isEqualTo(expectedSculptureResponseModels));

        // Verify the mock server received the request
        mockRestServiceServer.verify();
    }

    @Test
    public void testAddSculptureInGallery() throws Exception {
        String galleryId = "123";
        SculptureRequestModel sculptureRequestModel = SculptureRequestModel.builder()
                .title("Title 1")
                .texture("Texture 1")
                .material("Material 1")
                .build();

        // Mock the response from the service
        SculptureResponseModel expectedResponse = new SculptureResponseModel();
        mockRestServiceServer.expect(requestTo("http://localhost:7003/api/v1/galleries/" + galleryId + "/sculptures"))
            .andExpect(method(HttpMethod.POST))
            .andExpect(content().json(mapper.writeValueAsString(sculptureRequestModel)))
            .andRespond(withSuccess(mapper.writeValueAsString(expectedResponse), MediaType.APPLICATION_JSON));

    // Call the controller method
        webTestClient.post()
                .uri("/api/v1/galleries/{galleryId}/sculptures", galleryId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(sculptureRequestModel)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(SculptureResponseModel.class)
                .value(response -> assertThat(response).isEqualTo(expectedResponse));

    // Verify the mock server received the request
        mockRestServiceServer.verify();
}

    @Test
    public void testUpdateSculptureInGallery() throws Exception {
        String galleryId = "123";
        String sculptureId = "456";
        SculptureRequestModel sculptureRequestModel = SculptureRequestModel.builder()
                .title("Title 1")
                .texture("Texture 1")
                .material("Material 1")
                .build();
        // Mock the response from the service
        mockRestServiceServer.expect(requestTo("http://localhost:7003/api/v1/galleries/" + galleryId + "/sculptures/" + sculptureId))
                .andExpect(method(HttpMethod.PUT))
                .andExpect(content().json(mapper.writeValueAsString(sculptureRequestModel)))
                .andRespond(withSuccess());

        // Call the controller method
        webTestClient.put()
                .uri("/api/v1/galleries/{galleryId}/sculptures/{sculptureId}", galleryId, sculptureId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(sculptureRequestModel)
                .exchange()
                .expectStatus().isNoContent()
                .expectBody().isEmpty();

        // Verify the mock server received the request
        mockRestServiceServer.verify();
    }

    @Test
    public void testRemoveSculptureFromGallery() {
        String galleryId = "123";
        String sculptureId = "456";

        // Mock the response from the service
        mockRestServiceServer.expect(requestTo("http://localhost:7003/api/v1/galleries/" + galleryId + "/sculptures/" + sculptureId))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withSuccess());

        // Call the controller method
        webTestClient.delete()
                .uri("/api/v1/galleries/{galleryId}/sculptures/{sculptureId}", galleryId, sculptureId)
                .exchange()
                .expectStatus().isNoContent()
                .expectBody().isEmpty();

        // Verify the mock server received the request
        mockRestServiceServer.verify();
    }

}