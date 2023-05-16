package com.gallery.apigateway.domainclientlayer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gallery.apigateway.presentationlayer.SculptureRequestModel;
import com.gallery.apigateway.presentationlayer.SculptureResponseModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.NOT_FOUND;

public class SculptureServiceClientUnitTest {

    private RestTemplate restTemplate;

    private ObjectMapper objectMapper;
    private String SCULPTURE_SERVICE_BASE_URL;

    private SculptureServiceClient sculptureServiceClient;

    @BeforeEach
    void setUp() {
        restTemplate = Mockito.mock(RestTemplate.class);
        sculptureServiceClient = new SculptureServiceClient(restTemplate, objectMapper, "localhost", "8080");
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.SCULPTURE_SERVICE_BASE_URL = "http://" + "localhost" + ":" + "8080" + "/api/v1/galleries";
    }

    @Test
    void getAllSculpturesReturnsArrayOfSculptures() {
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

        when(restTemplate.getForObject(SCULPTURE_SERVICE_BASE_URL + "/" + "1" + "/sculptures", SculptureResponseModel[].class))
                .thenReturn(expectedSculptureResponseModels);

        SculptureResponseModel[] actualSculptureResponseModels = sculptureServiceClient.getAllSculpturesInGallery("1");
        assertArrayEquals(expectedSculptureResponseModels, actualSculptureResponseModels);

    }

    @Test
    void getAllSculptures_ThrowsException() {
        String galleryId = "1";

            when(restTemplate.getForObject(SCULPTURE_SERVICE_BASE_URL + "/" + galleryId + "/sculptures", SculptureResponseModel[].class))
                    .thenThrow(new HttpClientErrorException(NOT_FOUND));

        assertThrows(NullPointerException.class, () -> sculptureServiceClient.getAllSculpturesInGallery(galleryId));
    }

    @Test
    void getSculptureReturnsSculpture() {
        String sculptureId = "1";
        SculptureResponseModel expectedSculptureResponseModel = SculptureResponseModel.builder()
                .sculptureId("123")
                .title("Title 1")
                .texture("Texture 1")
                .material("Material 1")
                .build();

        when(restTemplate.getForObject(SCULPTURE_SERVICE_BASE_URL + "/" + "1" + "/sculptures" + "/" + sculptureId, SculptureResponseModel.class))
                .thenReturn(expectedSculptureResponseModel);

        SculptureResponseModel actualSculptureResponseModel = sculptureServiceClient.getSculptureById("1", sculptureId);
        assertEquals(expectedSculptureResponseModel, actualSculptureResponseModel);
    }

    @Test
    void getSculpture_ThrowsException() {
        String galleryId = "1";
        String sculptureId = "123";

        when(restTemplate.getForObject(SCULPTURE_SERVICE_BASE_URL + "/" + galleryId + "/sculptures" + "/" + sculptureId, SculptureResponseModel.class))
                .thenThrow(new HttpClientErrorException(NOT_FOUND));

        assertThrows(NullPointerException.class, () -> sculptureServiceClient.getSculptureById(galleryId, sculptureId));
    }

    @Test
    void addSculptureReturnsSculpture() {
        String galleryId = "1";
        SculptureResponseModel expectedSculptureResponseModel = SculptureResponseModel.builder()
                .sculptureId("123")
                .title("Title 1")
                .texture("Texture 1")
                .material("Material 1")
                .build();
        SculptureRequestModel sculptureRequestModel = SculptureRequestModel.builder()
                .title("Title 1")
                .texture("Texture 1")
                .material("Material 1")
                .build();

        when(restTemplate.postForObject(SCULPTURE_SERVICE_BASE_URL + "/" + galleryId + "/sculptures", sculptureRequestModel, SculptureResponseModel.class))
                .thenReturn(expectedSculptureResponseModel);

        SculptureResponseModel actualSculptureResponseModel = sculptureServiceClient.addSculptureInGallery(galleryId, sculptureRequestModel);
        assertEquals(expectedSculptureResponseModel, actualSculptureResponseModel);
    }

    @Test
    void addSculpture_ThrowsException() {
        String galleryId = "1";
        SculptureRequestModel sculptureRequestModel = SculptureRequestModel.builder()
                .title("Title 1")
                .texture("Texture 1")
                .material("Material 1")
                .build();

        when(restTemplate.postForObject(SCULPTURE_SERVICE_BASE_URL + "/" + galleryId + "/sculptures", sculptureRequestModel, SculptureResponseModel.class))
                .thenThrow(new HttpClientErrorException(NOT_FOUND));

        assertThrows(NullPointerException.class, () -> sculptureServiceClient.addSculptureInGallery(galleryId, sculptureRequestModel));
    }

    @Test
    void updateSculptureReturnsSculpture() {
        String galleryId = "1";
        String sculptureId = "123";
        SculptureRequestModel sculptureRequestModel = SculptureRequestModel.builder()
                .title("Title 1")
                .texture("Texture 1")
                .material("Material 1")
                .build();

        sculptureServiceClient.updateSculptureInGallery(galleryId, sculptureId, sculptureRequestModel);
        assertDoesNotThrow(() -> sculptureServiceClient.updateSculptureInGallery(galleryId, sculptureId, sculptureRequestModel));
    }

    @Test
    void updateSculpture_ThrowsException() {
        String galleryId = "1";
        String sculptureId = "123";
        SculptureRequestModel sculptureRequestModel = SculptureRequestModel.builder()
                .title("Title 1")
                .texture("Texture 1")
                .material("Material 1")
                .build();

        doThrow(new HttpClientErrorException(NOT_FOUND))
                .when(restTemplate).put(SCULPTURE_SERVICE_BASE_URL + "/" + galleryId + "/sculptures" + "/" + sculptureId, sculptureRequestModel);
        assertThrows(NullPointerException.class, () -> sculptureServiceClient.updateSculptureInGallery(galleryId, sculptureId, sculptureRequestModel));
    }

    @Test
    void deleteSculptureReturnsVoid() {
        String galleryId = "1";
        String sculptureId = "123";

        SculptureRequestModel sculptureRequestModel = SculptureRequestModel.builder()
                .title("Title 1")
                .texture("Texture 1")
                .material("Material 1")
                .build();
        SculptureResponseModel sculptureResponseModel = SculptureResponseModel.builder()
                .sculptureId(sculptureId)
                .title("Title 1")
                .texture("Texture 1")
                .material("Material 1")
                .build();

        when(sculptureServiceClient.addSculptureInGallery(galleryId, sculptureRequestModel))
                .thenReturn(sculptureResponseModel);
        assertDoesNotThrow(() -> sculptureServiceClient.deleteSculpture(galleryId, sculptureId));
        assertTrue(sculptureServiceClient.getSculptureById(galleryId, sculptureId) == null);
        Mockito.verify(restTemplate).delete(SCULPTURE_SERVICE_BASE_URL + "/" + galleryId + "/sculptures" + "/" + sculptureId);
    }

    @Test
    void deleteSculpture_ThrowsException() {
        String galleryId = "1";
        String sculptureId = "123";

        doThrow(new HttpClientErrorException(NOT_FOUND))
                .when(restTemplate).delete(SCULPTURE_SERVICE_BASE_URL + "/" + galleryId + "/sculptures" + "/" + sculptureId);
        assertThrows(NullPointerException.class, () -> sculptureServiceClient.deleteSculpture(galleryId, sculptureId));
    }


}
