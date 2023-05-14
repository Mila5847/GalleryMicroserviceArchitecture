package com.gallery.exhibitionservice.presentationlayer.domainclientlayer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gallery.exhibitionservice.domainclientlayer.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

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
    void updateSculptureReturnsSculpture() {
        String galleryId = "1";
        String sculptureId = "123";
        SculptureRequestModel sculptureRequestModel = SculptureRequestModel.builder()
                .title("Title 1")
                .texture("Texture 1")
                .material("Material 1")
                .build();

        sculptureServiceClient.updateSculptureInGallery(galleryId, sculptureId, sculptureRequestModel);
        Mockito.verify(restTemplate).put(SCULPTURE_SERVICE_BASE_URL + "/" + galleryId + "/sculptures" + "/" + sculptureId, sculptureRequestModel);
        assertDoesNotThrow(() -> sculptureServiceClient.updateSculptureInGallery(galleryId, sculptureId, sculptureRequestModel));
    }

    @Test
    void deleteSculptureReturnsVoid() {
        String galleryId = "1";
        String sculptureId = "123";

        sculptureServiceClient.deleteSculpture(galleryId, sculptureId);
        Mockito.verify(restTemplate).delete(SCULPTURE_SERVICE_BASE_URL + "/" + galleryId + "/sculptures" + "/" + sculptureId);
        assertDoesNotThrow(() -> sculptureServiceClient.deleteSculpture(galleryId, sculptureId));
    }


}
