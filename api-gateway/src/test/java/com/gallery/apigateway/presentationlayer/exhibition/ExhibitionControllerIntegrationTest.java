package com.gallery.apigateway.presentationlayer.exhibition;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gallery.apigateway.presentationlayer.PaintingResponseModel;
import com.gallery.apigateway.presentationlayer.SculptureResponseModel;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
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


    @Test
    public void testGetAllExhibitions() throws Exception {
        // Mock the response from the service
        ExhibitionResponseModel[] exhibitionResponses = {/* Populate with exhibition response models */};
        ResponseEntity<ExhibitionResponseModel[]> responseEntity = ResponseEntity.ok(exhibitionResponses);

        mockRestServiceServer.expect(ExpectedCount.once(),
                        requestTo("http://localhost:7004/api/v1/exhibitions"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(exhibitionResponses)));

        // Call the controller method
        String url = "/api/v1/exhibitions";
        webTestClient.get()
                .uri(url)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ExhibitionResponseModel[].class)
                .value(result -> assertThat(result).isEqualTo(exhibitionResponses));

        // Verify the mock server received the request
        mockRestServiceServer.verify();
    }

    @Test
    public void testGetExhibition() throws Exception {
        String exhibitionId = "123";
        List<SculptureResponseModel> expectedSculptureResponseModels = new ArrayList<>();
        expectedSculptureResponseModels.add(SculptureResponseModel.builder()
                .sculptureId("111")
                .title("Title 1")
                .texture("Texture 1")
                .material("Material 1")
                .build());
        expectedSculptureResponseModels.add(SculptureResponseModel.builder()
                .sculptureId("456")
                .title("Title 2")
                .texture("Texture 2")
                .material("Material 2")
                .build());

        List<PaintingResponseModel> expectedPaintingResponseModels = new ArrayList<>();
        expectedPaintingResponseModels.add(PaintingResponseModel.builder()
                .paintingId("122")
                .title("Title1")
                .yearCreated(2020)
                .painterId("1")
                .galleryId("1")
                .build());
        ExhibitionResponseModel expectedExhibitionResponseModel = ExhibitionResponseModel.builder()
                .exhibitionId("123")
                .galleryId("1")
                .galleryName("Gallery 1")
                .exhibitionName("Exhibition 1")
                .roomNumber(1)
                .duration(1)
                .paintings(expectedPaintingResponseModels)
                .sculptures(expectedSculptureResponseModels)
                .build();

        mockRestServiceServer.expect(ExpectedCount.once(),
                        requestTo("http://localhost:7004/api/v1/exhibitions/" + exhibitionId))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(expectedExhibitionResponseModel)));

        // Call the controller method
        String url = "/api/v1/exhibitions/" + exhibitionId;
        webTestClient.get()
                .uri(url)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ExhibitionResponseModel.class)
                .value(result -> assertThat(result).isEqualTo(expectedExhibitionResponseModel));

        // Verify the mock server received the request
        mockRestServiceServer.verify();
    }

    @Test
    public void testCreateExhibition() throws Exception {
        List<SculptureResponseModel> expectedSculptureResponseModels = new ArrayList<>();
        expectedSculptureResponseModels.add(SculptureResponseModel.builder()
                .sculptureId("123")
                .title("Title 1")
                .texture("Texture 1")
                .material("Material 1")
                .build());
        expectedSculptureResponseModels.add(SculptureResponseModel.builder()
                .sculptureId("456")
                .title("Title 2")
                .texture("Texture 2")
                .material("Material 2")
                .build());

        List<PaintingResponseModel> expectedPaintingResponseModels = new ArrayList<>();
        expectedPaintingResponseModels.add(PaintingResponseModel.builder()
                .paintingId("123")
                .title("Title1")
                .yearCreated(2020)
                .painterId("1")
                .galleryId("1")
                .build());
        ExhibitionResponseModel expectedExhibitionResponseModel = ExhibitionResponseModel.builder()
                .exhibitionId("123")
                .galleryId("1")
                .galleryName("Gallery 1")
                .exhibitionName("Exhibition 1")
                .roomNumber(1)
                .duration(1)
                .paintings(expectedPaintingResponseModels)
                .sculptures(expectedSculptureResponseModels)
                .build();

        String galleryId = "456";
        ExhibitionRequestModel requestModel = ExhibitionRequestModel.builder()
                .exhibitionName("Gallery 1")
                .roomNumber(1)
                .duration(1)
                .startDay("Monday")
                .endDay("Friday")
                .paintings(expectedPaintingResponseModels)
                .sculptures(expectedSculptureResponseModels)
                .build();
        ExhibitionResponseModel expectedResponse = ExhibitionResponseModel.builder()
                .exhibitionId("123")
                .galleryId("1")
                .galleryName("Gallery 1")
                .exhibitionName("Exhibition 1")
                .roomNumber(1)
                .duration(1)
                .build();

        mockRestServiceServer.expect(ExpectedCount.once(),
                        requestTo("http://localhost:7004/api/v1/exhibitions/galleries/" + galleryId))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().json(mapper.writeValueAsString(requestModel)))
                .andRespond(withStatus(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(expectedResponse)));

        // Call the controller method
        String url = "/api/v1/exhibitions/galleries/" + galleryId;
        webTestClient.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestModel)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ExhibitionResponseModel.class)
                .value(result -> assertThat(result).isEqualTo(expectedResponse));

        // Verify the mock server received the request
        mockRestServiceServer.verify();
    }

    @Test
    public void testUpdateExhibition() throws Exception {
        String exhibitionId = "1234";
        PaintingResponseModel paintingResponseModel1 = PaintingResponseModel.builder()
                .paintingId("123")
                .title("Title1")
                .yearCreated(2020)
                .painterId("1")
                .galleryId("1")
                .build();
        PaintingResponseModel paintingResponseModel2 = PaintingResponseModel.builder()
                .paintingId("124")
                .title("Title2")
                .yearCreated(2220)
                .painterId("2")
                .galleryId("2")
                .build();
        SculptureResponseModel sculptureResponseModel1 = SculptureResponseModel.builder()
                .sculptureId("123")
                .title("Title 1")
                .texture("Texture 1")
                .material("Material 1")
                .build();
        SculptureResponseModel sculptureResponseModel2 = SculptureResponseModel.builder()
                .sculptureId("125")
                .title("Title 2")
                .texture("Texture 2")
                .material("Material 2")
                .build();

        ExhibitionRequestModel exhibitionRequestModel = ExhibitionRequestModel.builder()
                .exhibitionName("Exhibition 1")
                .roomNumber(1)
                .duration(1)
                .startDay("Monday")
                .endDay("Friday")
                .paintings(Arrays.asList(paintingResponseModel1, paintingResponseModel2))
                .sculptures(Arrays.asList(sculptureResponseModel1, sculptureResponseModel2))
                .build();

        mockRestServiceServer.expect(ExpectedCount.once(),
                        requestTo("http://localhost:7004/api/v1/exhibitions/" + exhibitionId))
                .andExpect(method(HttpMethod.PUT))
                .andExpect(content().json(mapper.writeValueAsString(exhibitionRequestModel)))
                .andRespond(withStatus(HttpStatus.NO_CONTENT));

        // Call the controller method
        String url = "/api/v1/exhibitions/" + exhibitionId;
        webTestClient.put()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(exhibitionRequestModel)
                .exchange()
                .expectStatus().isNoContent();

        // Verify the mock server received the request
        mockRestServiceServer.verify();
    }

    @Test
    public void testDeleteExhibition() throws Exception {
        String exhibitionId = "123";

        mockRestServiceServer.expect(ExpectedCount.once(),
                        requestTo("http://localhost:7004/api/v1/exhibitions/" + exhibitionId))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withStatus(HttpStatus.NO_CONTENT));

        // Call the controller method
        String url = "/api/v1/exhibitions/" + exhibitionId;
        webTestClient.delete()
                .uri(url)
                .exchange()
                .expectStatus().isNoContent();

        // Verify the mock server received the request
        mockRestServiceServer.verify();
    }

    @Test
    public void testDeleteExhibitions(){
        mockRestServiceServer.expect(ExpectedCount.once(),
                        requestTo("http://localhost:7004/api/v1/exhibitions"))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withStatus(HttpStatus.NO_CONTENT));

        // Call the controller method
        String url = "/api/v1/exhibitions";
        webTestClient.delete()
                .uri(url)
                .exchange()
                .expectStatus().isNoContent();

        // Verify the mock server received the request
        mockRestServiceServer.verify();
    }


}