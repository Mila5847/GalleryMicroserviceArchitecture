package com.gallery.apigateway.domainclientlayer.exhibition;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gallery.apigateway.domainclientlayer.GalleryServiceClient;
import com.gallery.apigateway.presentationlayer.GalleryRequestModel;
import com.gallery.apigateway.presentationlayer.GalleryResponseModel;
import com.gallery.apigateway.presentationlayer.PaintingResponseModel;
import com.gallery.apigateway.presentationlayer.SculptureResponseModel;
import com.gallery.apigateway.presentationlayer.exhibition.ExhibitionRequestModel;
import com.gallery.apigateway.presentationlayer.exhibition.ExhibitionResponseModel;
import com.gallery.apigateway.utils.exceptions.ExistingGalleryNotFoundException;
import com.gallery.apigateway.utils.exceptions.InvalidInputException;
import com.gallery.apigateway.utils.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

class ExhibitionServiceClientUnitTest {
    private RestTemplate restTemplate;
    private ObjectMapper objectMapper;
    private String  EXHIBITION_SERVICE_BASE_URL;
    private ExhibitionServiceClient exhibitionServiceClient;


    @BeforeEach
    void setUp() {
        restTemplate = Mockito.mock(RestTemplate.class);
        exhibitionServiceClient = new ExhibitionServiceClient(restTemplate, objectMapper, "localhost", "8080");
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.EXHIBITION_SERVICE_BASE_URL = "http://" + "localhost" + ":" + "8080" + "/api/v1/exhibitions";
    }

    @Test
    public void testGetAllExhibitions() {
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

        ExhibitionResponseModel[] expectedResponse = {expectedExhibitionResponseModel};

        when(restTemplate.getForObject(EXHIBITION_SERVICE_BASE_URL, ExhibitionResponseModel[].class))
                .thenReturn(expectedResponse);

        ExhibitionResponseModel[] result = exhibitionServiceClient.getAllExhibitions();

        assertArrayEquals(expectedResponse, result);

    }

    @Test
    void getAllExhibitions_whenRestTemplateThrowsHttpClientErrorException_ThrowNotFoundException() {
        HttpClientErrorException exception = new HttpClientErrorException(HttpStatus.NOT_FOUND, "Not found");

        when(restTemplate.getForObject(EXHIBITION_SERVICE_BASE_URL, ExhibitionResponseModel[].class))
                .thenThrow(exception);

        assertThrows(NotFoundException.class, () -> exhibitionServiceClient.getAllExhibitions());
    }

    @Test
    void getAllExhibitions_whenRestTemplateThrowsHttpClientErrorException_ThrowUnprocessableEntityException() {
        HttpClientErrorException exception = new HttpClientErrorException(UNPROCESSABLE_ENTITY, "Unprocessable Entity");

        when(restTemplate.getForObject(EXHIBITION_SERVICE_BASE_URL, ExhibitionResponseModel[].class))
                .thenThrow(exception);

        assertThrows(InvalidInputException.class, () -> exhibitionServiceClient.getAllExhibitions());
    }

    @Test
    void getAllExhibitions_whenRestTemplateThrowsHttpClientErrorException_ThrowException() {
        HttpClientErrorException exception = new HttpClientErrorException(BAD_REQUEST, "");

        when(restTemplate.getForObject(EXHIBITION_SERVICE_BASE_URL, ExhibitionResponseModel[].class))
                .thenThrow(exception);

        assertThrows(HttpClientErrorException.class, () -> exhibitionServiceClient.getAllExhibitions());
    }

    @Test
    public void testGetExhibition() {
        String exhibitionId = "123";
        ExhibitionResponseModel expectedResponse = ExhibitionResponseModel.builder()
                .exhibitionId("123")
                .galleryId("1")
                .galleryName("Gallery 1")
                .exhibitionName("Exhibition 1")
                .roomNumber(1)
                .duration(1)
                .build();

        when(restTemplate.getForObject(EXHIBITION_SERVICE_BASE_URL + "/" + exhibitionId, ExhibitionResponseModel.class))
                .thenReturn(expectedResponse);

        ExhibitionResponseModel result = exhibitionServiceClient.getExhibition(exhibitionId);

        assertEquals(expectedResponse, result);

        verify(restTemplate, times(1)).getForObject(EXHIBITION_SERVICE_BASE_URL + "/" + exhibitionId, ExhibitionResponseModel.class);
    }

    @Test
    void getExhibition_whenRestTemplateThrowsHttpClientErrorException_ThrowException() {
        String exhibitionId = "123";
        when(restTemplate.getForObject(EXHIBITION_SERVICE_BASE_URL + "/" + exhibitionId, ExhibitionResponseModel.class))
                .thenThrow(new HttpClientErrorException(BAD_REQUEST));

        assertThrows(HttpClientErrorException.class, () ->exhibitionServiceClient.getExhibition(exhibitionId));
    }

    @Test
    void getExhibition_whenRestTemplateThrowsHttpClientErrorException_ThrowNotFoundException() {
        String exhibitionId = "123";
        HttpClientErrorException exception = new HttpClientErrorException(HttpStatus.NOT_FOUND, "Not found");

        when(restTemplate.getForObject(EXHIBITION_SERVICE_BASE_URL + "/" + exhibitionId, ExhibitionResponseModel.class))
                .thenThrow(exception);

        assertThrows(NotFoundException.class, () -> exhibitionServiceClient.getExhibition(exhibitionId));
    }

    @Test
    void getExhibition_whenRestTemplateThrowsHttpClientErrorException_ThrowUnprocessableEntityException() {
        String exhibitionId = "123";
        HttpClientErrorException exception = new HttpClientErrorException(UNPROCESSABLE_ENTITY, "Unprocessable Entity");

        when(restTemplate.getForObject(EXHIBITION_SERVICE_BASE_URL + "/" + exhibitionId, ExhibitionResponseModel.class))
                .thenThrow(exception);

        assertThrows(InvalidInputException.class, () -> exhibitionServiceClient.getExhibition(exhibitionId));
    }

    @Test
    public void testCreateExhibition() {
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

        when(restTemplate.postForObject(EXHIBITION_SERVICE_BASE_URL + "/galleries/" + galleryId, requestModel, ExhibitionResponseModel.class))
                .thenReturn(expectedResponse);

        ExhibitionResponseModel result = exhibitionServiceClient.createExhibition(galleryId, requestModel);

        assertEquals(expectedResponse, result);

        verify(restTemplate, times(1)).postForObject(EXHIBITION_SERVICE_BASE_URL + "/galleries/" + galleryId, requestModel, ExhibitionResponseModel.class);
    }

    @Test
    void createExhibition_whenRestTemplateThrowsHttpClientErrorException_ThrowException() {
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
        when(restTemplate.postForObject(EXHIBITION_SERVICE_BASE_URL + "/galleries/" + galleryId, requestModel, ExhibitionResponseModel.class))
                .thenThrow(new HttpClientErrorException(BAD_REQUEST));
        assertThrows(HttpClientErrorException.class, () ->exhibitionServiceClient.createExhibition(galleryId, requestModel));
    }

    @Test
    void createExhibition_whenRestTemplateThrowsHttpClientErrorException_ThrowNotFoundException() {
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
        when(restTemplate.postForObject(EXHIBITION_SERVICE_BASE_URL + "/galleries/" + galleryId, requestModel, ExhibitionResponseModel.class))
                .thenThrow(new HttpClientErrorException(NOT_FOUND));

        assertThrows(NotFoundException.class, () ->exhibitionServiceClient.createExhibition(galleryId, requestModel));
    }

    @Test
    void createExhibition_whenRestTemplateThrowsHttpClientErrorException_ThrowUnprocessableEntityException() {
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
        when(restTemplate.postForObject(EXHIBITION_SERVICE_BASE_URL + "/galleries/" + galleryId, requestModel, ExhibitionResponseModel.class))
                .thenThrow(new HttpClientErrorException(UNPROCESSABLE_ENTITY));

        assertThrows(InvalidInputException.class, () -> exhibitionServiceClient.createExhibition(galleryId, requestModel));
    }
    @Test
    void updateExhibition_Successful() {
        // Arrange
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

        // Act
        exhibitionServiceClient.updateExhibition(exhibitionId, exhibitionRequestModel);

        // Assert
        // Verify that the PUT request was made to the correct URL
        verify(restTemplate).put(EXHIBITION_SERVICE_BASE_URL + "/" + exhibitionId, exhibitionRequestModel);

        // Verify that the actual response matches the expected response
        assertDoesNotThrow(() -> exhibitionServiceClient.updateExhibition(exhibitionId, exhibitionRequestModel));
    }

    @Test
    void updateExhibition_whenRestTemplateThrowsHttpClientErrorException_ThrowNotFoundException() {
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
        HttpClientErrorException exception = new HttpClientErrorException(HttpStatus.NOT_FOUND, "Not found");
        Mockito.doThrow(exception)
                .when(restTemplate).put(EXHIBITION_SERVICE_BASE_URL + "/" + exhibitionId, exhibitionRequestModel);
        assertThrows(NotFoundException.class, () ->  exhibitionServiceClient.updateExhibition(exhibitionId, exhibitionRequestModel));
    }

    @Test
    void updateExhibition_whenRestTemplateThrowsHttpClientErrorException_ThrowUnprocessableEntityException() {
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
        HttpClientErrorException exception = new HttpClientErrorException(UNPROCESSABLE_ENTITY, "Unprocessable Entity");
        Mockito.doThrow(exception)
                .when(restTemplate).put(EXHIBITION_SERVICE_BASE_URL + "/" + exhibitionId, exhibitionRequestModel);

        assertThrows(InvalidInputException.class, () -> exhibitionServiceClient.updateExhibition(exhibitionId, exhibitionRequestModel));
    }

    @Test
    void updateExhibition_whenRestTemplateThrowsHttpClientErrorException_ThrowException() {
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
        HttpClientErrorException exception = new HttpClientErrorException(BAD_REQUEST, "");
        Mockito.doThrow(exception)
                .when(restTemplate).put(EXHIBITION_SERVICE_BASE_URL + "/" + exhibitionId, exhibitionRequestModel);
        assertThrows(HttpClientErrorException.class, () -> exhibitionServiceClient.updateExhibition(exhibitionId, exhibitionRequestModel));
    }

    @Test
    void removeExhibition_Successful() {
        // Arrange
        String exhibitionId = "1234";

        // Mock the successful DELETE request
        doNothing().when(restTemplate).delete(EXHIBITION_SERVICE_BASE_URL + "/" + exhibitionId);

        // Act
        exhibitionServiceClient.removeExhibition(exhibitionId);

        // Assert
        // Verify that the DELETE request was made to the correct URL
        verify(restTemplate).delete(EXHIBITION_SERVICE_BASE_URL + "/" + exhibitionId);
    }

    @Test
    void deleteExhibition_whenRestTemplateThrowsHttpClientErrorException_ThrowNotFoundException() {
        String exhibitionId = "1234";
        HttpClientErrorException exception = new HttpClientErrorException(HttpStatus.NOT_FOUND, "Not found");

        Mockito.doThrow(exception).when(restTemplate).delete(EXHIBITION_SERVICE_BASE_URL + "/" + exhibitionId);

        assertThrows(NotFoundException.class, () -> exhibitionServiceClient.removeExhibition(exhibitionId));
    }

    @Test
    void deleteExhibition_whenRestTemplateThrowsHttpClientErrorException_ThrowUnprocessableEntityException() {
        String exhibitionId = "1234";
        HttpClientErrorException exception = new HttpClientErrorException(UNPROCESSABLE_ENTITY, "Unprocessable Entity");

        Mockito.doThrow(exception).when(restTemplate).delete(EXHIBITION_SERVICE_BASE_URL + "/" + exhibitionId);

        assertThrows(InvalidInputException.class, () -> exhibitionServiceClient.removeExhibition(exhibitionId));
    }

    @Test
    void deleteExhibition_whenRestTemplateThrowsHttpClientErrorException_ThrowException() {
        String exhibitionId = "1234";
        HttpClientErrorException exception = new HttpClientErrorException(BAD_REQUEST, "");

        Mockito.doThrow(exception).when(restTemplate).delete(EXHIBITION_SERVICE_BASE_URL + "/" + exhibitionId);

        assertThrows(HttpClientErrorException.class, () -> exhibitionServiceClient.removeExhibition(exhibitionId));
    }

    @Test
    void removeAllExhibitions_Successful() {
        // Mock the successful DELETE request
        doNothing().when(restTemplate).delete(EXHIBITION_SERVICE_BASE_URL);

        // Act
        exhibitionServiceClient.removeAllExhibitions();

        // Assert
        // Verify that the DELETE request was made to the correct URL
        verify(restTemplate).delete(EXHIBITION_SERVICE_BASE_URL);
    }

    @Test
    void deleteAllExhibitions_whenRestTemplateThrowsHttpClientErrorException_ThrowNotFoundException() {
        HttpClientErrorException exception = new HttpClientErrorException(HttpStatus.NOT_FOUND, "Not found");

        Mockito.doThrow(exception).when(restTemplate).delete(EXHIBITION_SERVICE_BASE_URL);

        assertThrows(NotFoundException.class, () ->    exhibitionServiceClient.removeAllExhibitions());
    }

    @Test
    void deleteAllExhibitions_whenRestTemplateThrowsHttpClientErrorException_ThrowUnprocessableEntityException() {
        HttpClientErrorException exception = new HttpClientErrorException(UNPROCESSABLE_ENTITY, "Unprocessable Entity");

        Mockito.doThrow(exception).when(restTemplate).delete(EXHIBITION_SERVICE_BASE_URL);

        assertThrows(InvalidInputException.class, () ->    exhibitionServiceClient.removeAllExhibitions());
    }

    @Test
    void deleteAllExhibitions_whenRestTemplateThrowsHttpClientErrorException_ThrowException() {
        HttpClientErrorException exception = new HttpClientErrorException(BAD_REQUEST, "");

        Mockito.doThrow(exception).when(restTemplate).delete(EXHIBITION_SERVICE_BASE_URL);

        assertThrows(HttpClientErrorException.class, () ->    exhibitionServiceClient.removeAllExhibitions());
    }


}