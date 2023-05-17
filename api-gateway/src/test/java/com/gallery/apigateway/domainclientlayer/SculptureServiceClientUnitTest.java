package com.gallery.apigateway.domainclientlayer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gallery.apigateway.presentationlayer.SculptureRequestModel;
import com.gallery.apigateway.presentationlayer.SculptureResponseModel;
import com.gallery.apigateway.utils.exceptions.InvalidInputException;
import com.gallery.apigateway.utils.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.*;

public class SculptureServiceClientUnitTest {

    private RestTemplate restTemplate;

    private ObjectMapper objectMapper;
    private String SCULPTURE_SERVICE_BASE_URL;

    private SculptureServiceClient sculptureServiceClient;

    @BeforeEach
    void setUp() {
        restTemplate = Mockito.mock(RestTemplate.class);
        sculptureServiceClient = new SculptureServiceClient(restTemplate, objectMapper, "localhost", "8080");
        this.objectMapper = new ObjectMapper();
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
    void getAllSculptures_whenRestTemplateThrowsHttpClientErrorException_ThrowNotFoundException() {
        HttpClientErrorException exception = new HttpClientErrorException(HttpStatus.NOT_FOUND, "Not found");

        when(restTemplate.getForObject(SCULPTURE_SERVICE_BASE_URL + "/" + "1" + "/sculptures", SculptureResponseModel[].class))
                .thenThrow(exception);

        assertThrows(NotFoundException.class, () -> sculptureServiceClient.getAllSculpturesInGallery("1"));
    }

    @Test
    void getAllSculptures_whenRestTemplateThrowsHttpClientErrorException_ThrowUnprocessableEntityException() {
        HttpClientErrorException exception = new HttpClientErrorException(UNPROCESSABLE_ENTITY, "Unprocessable Entity");

        when(restTemplate.getForObject(SCULPTURE_SERVICE_BASE_URL + "/" + "1" + "/sculptures", SculptureResponseModel[].class))
                .thenThrow(exception);

        assertThrows(InvalidInputException.class, () -> sculptureServiceClient.getAllSculpturesInGallery("1"));
    }

    @Test
    void getAllSculptures_whenRestTemplateThrowsHttpClientErrorException_ThrowException() {
        HttpClientErrorException exception = new HttpClientErrorException(BAD_REQUEST, "");

        when(restTemplate.getForObject(SCULPTURE_SERVICE_BASE_URL + "/" + "1" + "/sculptures", SculptureResponseModel[].class))
                .thenThrow(exception);

        assertThrows(HttpClientErrorException.class, () -> sculptureServiceClient.getAllSculpturesInGallery("1"));
    }

    @Test
    void getSculptureReturnsSculpture() {
        String sculptureId = "123";
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
    void getSculpture_whenRestTemplateThrowsHttpClientErrorException_ThrowException() {
        String sculptureId = "123";
        when(restTemplate.getForObject(SCULPTURE_SERVICE_BASE_URL + "/" + "1" + "/sculptures" + "/" + sculptureId, SculptureResponseModel.class))
                .thenThrow(new HttpClientErrorException(BAD_REQUEST));

        assertThrows(HttpClientErrorException.class, () -> sculptureServiceClient.getSculptureById("1", sculptureId));
    }

    @Test
    void getSculpture_whenRestTemplateThrowsHttpClientErrorException_ThrowNotFoundException() {
        String sculptureId = "123";
        HttpClientErrorException exception = new HttpClientErrorException(HttpStatus.NOT_FOUND, "Not found");

        when(restTemplate.getForObject(SCULPTURE_SERVICE_BASE_URL + "/" + "1" + "/sculptures" + "/" + sculptureId, SculptureResponseModel.class))
                .thenThrow(exception);

        assertThrows(NotFoundException.class, () -> sculptureServiceClient.getSculptureById("1", sculptureId));
    }

    @Test
    void getSculpture_whenRestTemplateThrowsHttpClientErrorException_ThrowUnprocessableEntityException() {
        String sculptureId = "123";
        HttpClientErrorException exception = new HttpClientErrorException(UNPROCESSABLE_ENTITY, "Unprocessable Entity");

        when(restTemplate.getForObject(SCULPTURE_SERVICE_BASE_URL + "/" + "1" + "/sculptures" + "/" + sculptureId, SculptureResponseModel.class))
                .thenThrow(exception);

        assertThrows(InvalidInputException.class, () -> sculptureServiceClient.getSculptureById("1", sculptureId));
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
    void addSculpture_whenRestTemplateThrowsHttpClientErrorException_ThrowException() {
        String galleryId = "1";
        SculptureRequestModel sculptureRequestModel = SculptureRequestModel.builder()
                .title("Title 1")
                .texture("Texture 1")
                .material("Material 1")
                .build();
        when(restTemplate.postForObject(SCULPTURE_SERVICE_BASE_URL + "/" + galleryId + "/sculptures", sculptureRequestModel, SculptureResponseModel.class))
                .thenThrow(new HttpClientErrorException(BAD_REQUEST));
        assertThrows(HttpClientErrorException.class, () -> sculptureServiceClient.addSculptureInGallery(galleryId, sculptureRequestModel));
    }

    @Test
    void addSculpture_whenRestTemplateThrowsHttpClientErrorException_ThrowNotFoundException() {
        String galleryId = "1";
        SculptureRequestModel sculptureRequestModel = SculptureRequestModel.builder()
                .title("Title 1")
                .texture("Texture 1")
                .material("Material 1")
                .build();
        HttpClientErrorException exception = new HttpClientErrorException(HttpStatus.NOT_FOUND, "Not found");

        when(restTemplate.postForObject(SCULPTURE_SERVICE_BASE_URL + "/" + galleryId + "/sculptures", sculptureRequestModel, SculptureResponseModel.class))
                .thenThrow(new HttpClientErrorException(NOT_FOUND));

        assertThrows(NotFoundException.class, () ->sculptureServiceClient.addSculptureInGallery(galleryId, sculptureRequestModel));
    }

    @Test
    void addSculpture_whenRestTemplateThrowsHttpClientErrorException_ThrowUnprocessableEntityException() {
        String galleryId = "1";
        SculptureRequestModel sculptureRequestModel = SculptureRequestModel.builder()
                .title("Title 1")
                .texture("Texture 1")
                .material("Material 1")
                .build();
        HttpClientErrorException exception = new HttpClientErrorException(UNPROCESSABLE_ENTITY, "Unprocessable Entity");

        when(restTemplate.postForObject(SCULPTURE_SERVICE_BASE_URL + "/" + galleryId + "/sculptures", sculptureRequestModel, SculptureResponseModel.class))
                .thenThrow(new HttpClientErrorException(UNPROCESSABLE_ENTITY));

        assertThrows(InvalidInputException.class, () -> sculptureServiceClient.addSculptureInGallery(galleryId, sculptureRequestModel));
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
    void updateSculpture_whenRestTemplateThrowsHttpClientErrorException_ThrowNotFoundException() {
        String galleryId = "1";
        String sculptureId = "123";
        SculptureRequestModel sculptureRequestModel = SculptureRequestModel.builder()
                .title("Title 1")
                .texture("Texture 1")
                .material("Material 1")
                .build();
        HttpClientErrorException exception = new HttpClientErrorException(HttpStatus.NOT_FOUND, "Not found");
        Mockito.doThrow(exception)
                .when(restTemplate).put(SCULPTURE_SERVICE_BASE_URL + "/" + galleryId + "/sculptures" + "/" + sculptureId, sculptureRequestModel);
        assertThrows(NotFoundException.class, () -> sculptureServiceClient.updateSculptureInGallery(galleryId, sculptureId, sculptureRequestModel));
    }

    @Test
    void updateSculpture_whenRestTemplateThrowsHttpClientErrorException_ThrowUnprocessableEntityException() {
        String galleryId = "1";
        String sculptureId = "123";
        SculptureRequestModel sculptureRequestModel = SculptureRequestModel.builder()
                .title("Title 1")
                .texture("Texture 1")
                .material("Material 1")
                .build();
        HttpClientErrorException exception = new HttpClientErrorException(UNPROCESSABLE_ENTITY, "Unprocessable Entity");
        Mockito.doThrow(exception)
                .when(restTemplate).put(SCULPTURE_SERVICE_BASE_URL + "/" + galleryId + "/sculptures" + "/" + sculptureId, sculptureRequestModel);

        assertThrows(InvalidInputException.class, () -> sculptureServiceClient.updateSculptureInGallery(galleryId, sculptureId, sculptureRequestModel));
    }

    @Test
    void updateSculpture_whenRestTemplateThrowsHttpClientErrorException_ThrowException() {
        String galleryId = "1";
        String sculptureId = "123";
        SculptureRequestModel sculptureRequestModel = SculptureRequestModel.builder()
                .title("Title 1")
                .texture("Texture 1")
                .material("Material 1")
                .build();
        HttpClientErrorException exception = new HttpClientErrorException(BAD_REQUEST, "");
        Mockito.doThrow(exception)
                .when(restTemplate).put(SCULPTURE_SERVICE_BASE_URL + "/" + galleryId + "/sculptures" + "/" + sculptureId, sculptureRequestModel);
        assertThrows(HttpClientErrorException.class, () -> sculptureServiceClient.updateSculptureInGallery(galleryId, sculptureId, sculptureRequestModel));
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
    void deleteGalleries_whenRestTemplateThrowsHttpClientErrorException_ThrowNotFoundException() {
        String galleryId = "1";
        String sculptureId = "123";
        HttpClientErrorException exception = new HttpClientErrorException(HttpStatus.NOT_FOUND, "Not found");

        Mockito.doThrow(exception).when(restTemplate).delete(SCULPTURE_SERVICE_BASE_URL + "/" + galleryId + "/sculptures" + "/" + sculptureId);

        assertThrows(NotFoundException.class, () -> sculptureServiceClient.deleteSculpture(galleryId, sculptureId));
    }

    @Test
    void deleteGallery_whenRestTemplateThrowsHttpClientErrorException_ThrowUnprocessableEntityException() {
        String galleryId = "1";
        String sculptureId = "123";
        HttpClientErrorException exception = new HttpClientErrorException(UNPROCESSABLE_ENTITY, "Unprocessable Entity");

        Mockito.doThrow(exception).when(restTemplate).delete(SCULPTURE_SERVICE_BASE_URL + "/" + galleryId + "/sculptures" + "/" + sculptureId);

        assertThrows(InvalidInputException.class, () ->sculptureServiceClient.deleteSculpture(galleryId, sculptureId));
    }

    @Test
    void deleteGallery_whenRestTemplateThrowsHttpClientErrorException_ThrowException() {
        String galleryId = "1";
        String sculptureId = "123";
        HttpClientErrorException exception = new HttpClientErrorException(BAD_REQUEST, "");

        Mockito.doThrow(exception).when(restTemplate).delete(SCULPTURE_SERVICE_BASE_URL + "/" + galleryId + "/sculptures" + "/" + sculptureId);

        assertThrows(HttpClientErrorException.class, () -> sculptureServiceClient.deleteSculpture(galleryId, sculptureId));
    }


}
