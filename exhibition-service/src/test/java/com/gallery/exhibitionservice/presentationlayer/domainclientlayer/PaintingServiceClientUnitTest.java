package com.gallery.exhibitionservice.presentationlayer.domainclientlayer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gallery.exhibitionservice.domainclientlayer.*;
import com.gallery.exhibitionservice.utils.exceptions.InvalidInputException;
import com.gallery.exhibitionservice.utils.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpStatus.*;

public class PaintingServiceClientUnitTest {

    private RestTemplate restTemplate;
    private ObjectMapper objectMapper;
    private String PAINTING_SERVICE_BASE_URL;
    private PaintingServiceClient paintingServiceClient;

    @BeforeEach
    public void setUp() {
        restTemplate = Mockito.mock(RestTemplate.class);
        paintingServiceClient = new PaintingServiceClient(restTemplate, objectMapper, "localhost", "8080");
        this.objectMapper = new ObjectMapper();
        this.PAINTING_SERVICE_BASE_URL = "http://" + "localhost" + ":" + "8080" + "/api/v1/galleries";
    }

    @Test
    public void getAllPaintingsReturnsArrayOfPaintings() {
        PaintingResponseModel[] expectedPaintingResponseModels = new PaintingResponseModel[2];
        expectedPaintingResponseModels[0] = PaintingResponseModel.builder()
                .paintingId("123")
                .title("Title 1")
                .yearCreated(1234)
                .painterId("111")
                .build();
        expectedPaintingResponseModels[1] = PaintingResponseModel.builder()
                .paintingId("456")
                .title("Title 2")
                .yearCreated(5678)
                .painterId("222")
                .build();

        when(restTemplate.getForObject(PAINTING_SERVICE_BASE_URL + "/" + "1" + "/paintings", PaintingResponseModel[].class))
                .thenReturn(expectedPaintingResponseModels);

        PaintingResponseModel[] actualPaintingResponseModels = paintingServiceClient.getPaintingsInGallery("1");
        assertArrayEquals(expectedPaintingResponseModels, actualPaintingResponseModels);
    }

    @Test
    void getAllPaintings_whenRestTemplateThrowsHttpClientErrorException_ThrowNotFoundException() {
        HttpClientErrorException exception = new HttpClientErrorException(HttpStatus.NOT_FOUND, "Not found");

        when(restTemplate.getForObject(PAINTING_SERVICE_BASE_URL + "/" + "1" + "/paintings", PaintingResponseModel[].class))
                .thenThrow(exception);

        assertThrows(NotFoundException.class, () -> paintingServiceClient.getPaintingsInGallery("1"));
    }

    @Test
    void getAllPaintings_whenRestTemplateThrowsHttpClientErrorException_ThrowUnprocessableEntityException() {
        HttpClientErrorException exception = new HttpClientErrorException(UNPROCESSABLE_ENTITY, "Unprocessable Entity");

        when(restTemplate.getForObject(PAINTING_SERVICE_BASE_URL + "/" + "1" + "/paintings", PaintingResponseModel[].class))
                .thenThrow(exception);

        assertThrows(InvalidInputException.class, () ->paintingServiceClient.getPaintingsInGallery("1"));
    }

    @Test
    void getAllPaintings_whenRestTemplateThrowsHttpClientErrorException_ThrowException() {
        HttpClientErrorException exception = new HttpClientErrorException(BAD_REQUEST, "");

        when(restTemplate.getForObject(PAINTING_SERVICE_BASE_URL + "/" + "1" + "/paintings", PaintingResponseModel[].class))
                .thenThrow(exception);

        assertThrows(HttpClientErrorException.class, () ->paintingServiceClient.getPaintingsInGallery("1"));
    }

    @Test
    public void getPaintingReturnsPainting() {
        String paintingId = "1";
        PaintingResponseModel expectedPaintingResponseModel = PaintingResponseModel.builder()
                .paintingId("123")
                .title("Title 1")
                .yearCreated(1234)
                .painterId("111")
                .build();
        PainterResponseModel expectedPainterResponseModel = PainterResponseModel.builder()
                .painterId("111")
                .name("Name 1")
                .origin("Origin 1")
                .birthDate("12-12-1212")
                .deathDate("12-12-1212")
                .build();

        PaintingPainterResponseModel expectedPaintingPainterResponseModel = new PaintingPainterResponseModel(expectedPaintingResponseModel, expectedPainterResponseModel);

        when(restTemplate.getForObject(PAINTING_SERVICE_BASE_URL + "/" + "1" + "/paintings" + "/" + paintingId, PaintingPainterResponseModel.class))
                .thenReturn(expectedPaintingPainterResponseModel);

        PaintingPainterResponseModel actualPaintingResponseModel = paintingServiceClient.getPaintingAggregateById("1", paintingId);
        assertEquals(expectedPaintingPainterResponseModel, actualPaintingResponseModel);
    }

    @Test
    void getPainting_whenRestTemplateThrowsHttpClientErrorException_ThrowException() {
        String paintingId = "1";
        when(restTemplate.getForObject(PAINTING_SERVICE_BASE_URL + "/" + "1" + "/paintings" + "/" + paintingId, PaintingPainterResponseModel.class))
                .thenThrow(new HttpClientErrorException(BAD_REQUEST));

        assertThrows(HttpClientErrorException.class, () ->  paintingServiceClient.getPaintingAggregateById("1", paintingId));
    }

    @Test
    void getPainting_whenRestTemplateThrowsHttpClientErrorException_ThrowNotFoundException() {
        String paintingId = "1";
        HttpClientErrorException exception = new HttpClientErrorException(HttpStatus.NOT_FOUND, "Not found");

        when(restTemplate.getForObject(PAINTING_SERVICE_BASE_URL + "/" + "1" + "/paintings" + "/" + paintingId, PaintingPainterResponseModel.class))
                .thenThrow(exception);

        assertThrows(NotFoundException.class, () ->  paintingServiceClient.getPaintingAggregateById("1", paintingId));
    }

    @Test
    void getPainting_whenRestTemplateThrowsHttpClientErrorException_ThrowUnprocessableEntityException() {
        String paintingId = "1";
        HttpClientErrorException exception = new HttpClientErrorException(UNPROCESSABLE_ENTITY, "Unprocessable Entity");

        when(restTemplate.getForObject(PAINTING_SERVICE_BASE_URL + "/" + "1" + "/paintings" + "/" + paintingId, PaintingPainterResponseModel.class))
                .thenThrow(exception);

        assertThrows(InvalidInputException.class, () ->  paintingServiceClient.getPaintingAggregateById("1", paintingId));
    }

    @Test
    public void getPaintingAggregateByPainterIdInGallery(){
        String painterId = "111";
        PaintingResponseModel expectedPaintingResponseModel = PaintingResponseModel.builder()
                .paintingId("123")
                .title("Title 1")
                .yearCreated(1234)
                .painterId("111")
                .build();
        PainterResponseModel expectedPainterResponseModel = PainterResponseModel.builder()
                .painterId("111")
                .name("Name 1")
                .origin("Origin 1")
                .birthDate("12-12-1212")
                .deathDate("12-12-1212")
                .build();

        List<PaintingResponseModel> paintingResponseModels = new ArrayList<>();
        paintingResponseModels.add(expectedPaintingResponseModel);
        PaintingsOfPainterResponseModel expectedPaintingPainterResponseModel = new PaintingsOfPainterResponseModel();
        expectedPaintingPainterResponseModel.setPainterId("1");
        expectedPaintingPainterResponseModel.setPainterResponseModel(expectedPainterResponseModel);
        expectedPaintingPainterResponseModel.setPaintings(paintingResponseModels);
            when(restTemplate.getForObject(PAINTING_SERVICE_BASE_URL + "/" + "1" + "/painters" + "/" + painterId + "/paintings", PaintingsOfPainterResponseModel.class))
                    .thenReturn(expectedPaintingPainterResponseModel);

        PaintingsOfPainterResponseModel actualPaintingsOfPainterResponseModel = paintingServiceClient.getPaintingAggregateByPainterIdInGallery("1", painterId);
        assertEquals(expectedPaintingPainterResponseModel, actualPaintingsOfPainterResponseModel);
    }

    @Test
    void getPaintingByPainter_whenRestTemplateThrowsHttpClientErrorException_ThrowException() {
        String painterId = "111";
        when(restTemplate.getForObject(PAINTING_SERVICE_BASE_URL + "/" + "1" + "/painters" + "/" + painterId + "/paintings", PaintingsOfPainterResponseModel.class))
                .thenThrow(new HttpClientErrorException(BAD_REQUEST));

        assertThrows(HttpClientErrorException.class, () ->  paintingServiceClient.getPaintingAggregateByPainterIdInGallery("1", painterId));
    }

    @Test
    void getPaintingByPainter_whenRestTemplateThrowsHttpClientErrorException_ThrowNotFoundException() {
        String painterId = "111";
        HttpClientErrorException exception = new HttpClientErrorException(HttpStatus.NOT_FOUND, "Not found");

        when(restTemplate.getForObject(PAINTING_SERVICE_BASE_URL + "/" + "1" + "/painters" + "/" + painterId + "/paintings", PaintingsOfPainterResponseModel.class))
                .thenThrow(exception);

        assertThrows(NotFoundException.class, () ->  paintingServiceClient.getPaintingAggregateByPainterIdInGallery("1", painterId));
    }

    @Test
    void getPaintingByPainter_whenRestTemplateThrowsHttpClientErrorException_ThrowUnprocessableEntityException() {
        String painterId = "111";
        HttpClientErrorException exception = new HttpClientErrorException(UNPROCESSABLE_ENTITY, "Unprocessable Entity");

        when(restTemplate.getForObject(PAINTING_SERVICE_BASE_URL + "/" + "1" + "/painters" + "/" + painterId + "/paintings", PaintingsOfPainterResponseModel.class))
                .thenThrow(exception);

        assertThrows(InvalidInputException.class, () -> paintingServiceClient.getPaintingAggregateByPainterIdInGallery("1", painterId));
    }

    @Test
    public void addPaintingReturnsPainting() {
        PaintingRequestModel paintingRequestModel = PaintingRequestModel.builder()
                .title("Title 1")
                .yearCreated(1234)
                .painterId("111")
                .build();

        PaintingResponseModel expectedPaintingResponseModel = PaintingResponseModel.builder()
                .paintingId("123")
                .title("Title 1")
                .yearCreated(1234)
                .painterId("111")
                .build();
        PainterResponseModel expectedPainterResponseModel = PainterResponseModel.builder()
                .painterId("111")
                .name("Name 1")
                .origin("Origin 1")
                .birthDate("12-12-1212")
                .deathDate("12-12-1212")
                .build();

        PaintingPainterResponseModel expectedPaintingPainterResponseModel = new PaintingPainterResponseModel(expectedPaintingResponseModel, expectedPainterResponseModel);

        when(restTemplate.postForObject(PAINTING_SERVICE_BASE_URL + "/" + "1" + "/paintings", paintingRequestModel, PaintingPainterResponseModel.class))
                .thenReturn(expectedPaintingPainterResponseModel);

        PaintingPainterResponseModel actualPaintingPainterResponseModel = paintingServiceClient.addPaintingInGallery("1", paintingRequestModel);
        assertEquals(expectedPaintingPainterResponseModel, actualPaintingPainterResponseModel);
    }

    @Test
    void addPainting_whenRestTemplateThrowsHttpClientErrorException_ThrowException() {
        PaintingRequestModel paintingRequestModel = PaintingRequestModel.builder()
                .title("Title 1")
                .yearCreated(1234)
                .painterId("111")
                .build();
        when(restTemplate.postForObject(PAINTING_SERVICE_BASE_URL + "/" + "1" + "/paintings", paintingRequestModel, PaintingPainterResponseModel.class))
                .thenThrow(new HttpClientErrorException(BAD_REQUEST));
        assertThrows(HttpClientErrorException.class, () -> paintingServiceClient.addPaintingInGallery("1", paintingRequestModel));
    }

    @Test
    void addPainting_whenRestTemplateThrowsHttpClientErrorException_ThrowNotFoundException() {
        PaintingRequestModel paintingRequestModel = PaintingRequestModel.builder()
                .title("Title 1")
                .yearCreated(1234)
                .painterId("111")
                .build();
        HttpClientErrorException exception = new HttpClientErrorException(HttpStatus.NOT_FOUND, "Not found");

        when(restTemplate.postForObject(PAINTING_SERVICE_BASE_URL + "/" + "1" + "/paintings", paintingRequestModel, PaintingPainterResponseModel.class))
                .thenThrow(new HttpClientErrorException(NOT_FOUND));

        assertThrows(NotFoundException.class, () ->paintingServiceClient.addPaintingInGallery("1", paintingRequestModel));
    }

    @Test
    void addPainting_whenRestTemplateThrowsHttpClientErrorException_ThrowUnprocessableEntityException() {
        PaintingRequestModel paintingRequestModel = PaintingRequestModel.builder()
                .title("Title 1")
                .yearCreated(1234)
                .painterId("111")
                .build();
        HttpClientErrorException exception = new HttpClientErrorException(UNPROCESSABLE_ENTITY, "Unprocessable Entity");

        when(restTemplate.postForObject(PAINTING_SERVICE_BASE_URL + "/" + "1" + "/paintings", paintingRequestModel, PaintingPainterResponseModel.class))
                .thenThrow(new HttpClientErrorException(UNPROCESSABLE_ENTITY));

        assertThrows(InvalidInputException.class, () -> paintingServiceClient.addPaintingInGallery("1", paintingRequestModel));
    }

    @Test
    public void addPainterToPaintingInGallery(){
        String galleryId = "1";
        String paintingId = "123";

        PainterRequestModel painterRequestModel = PainterRequestModel.builder()
                .name("Name 1")
                .origin("Origin 1")
                .birthDate("12-12-1212")
                .deathDate("12-12-1212")
                .build();

        PaintingResponseModel expectedPaintingResponseModel = PaintingResponseModel.builder()
                .paintingId("123")
                .title("Title 1")
                .yearCreated(1234)
                .painterId("111")
                .build();
        PainterResponseModel expectedPainterResponseModel = PainterResponseModel.builder()
                .painterId("111")
                .name("Name 1")
                .origin("Origin 1")
                .birthDate("12-12-1212")
                .deathDate("12-12-1212")
                .build();

        PaintingPainterResponseModel expectedPaintingPainterResponseModel = new PaintingPainterResponseModel(expectedPaintingResponseModel, expectedPainterResponseModel);

            when(restTemplate.postForObject(PAINTING_SERVICE_BASE_URL + "/" + galleryId + "/paintings" + "/" + paintingId + "/painters", painterRequestModel, PaintingPainterResponseModel.class))
                    .thenReturn(expectedPaintingPainterResponseModel);

            assertEquals(expectedPaintingPainterResponseModel, paintingServiceClient.addPainterToPaintingInGallery(galleryId, paintingId, painterRequestModel));

    }

    @Test
    void addPainterToPainting_whenRestTemplateThrowsHttpClientErrorException_ThrowException() {
        String galleryId = "1";
        String paintingId = "123";

        PainterRequestModel painterRequestModel = PainterRequestModel.builder()
                .name("Name 1")
                .origin("Origin 1")
                .birthDate("12-12-1212")
                .deathDate("12-12-1212")
                .build();
        when(restTemplate.postForObject(PAINTING_SERVICE_BASE_URL + "/" + galleryId + "/paintings" + "/" + paintingId + "/painters", painterRequestModel, PaintingPainterResponseModel.class))
                .thenThrow(new HttpClientErrorException(BAD_REQUEST));
        assertThrows(HttpClientErrorException.class, () -> paintingServiceClient.addPainterToPaintingInGallery(galleryId, paintingId, painterRequestModel));

    }

    @Test
    void addPainterToPainting_whenRestTemplateThrowsHttpClientErrorException_ThrowNotFoundException() {
        String galleryId = "1";
        String paintingId = "123";

        PainterRequestModel painterRequestModel = PainterRequestModel.builder()
                .name("Name 1")
                .origin("Origin 1")
                .birthDate("12-12-1212")
                .deathDate("12-12-1212")
                .build();
        HttpClientErrorException exception = new HttpClientErrorException(HttpStatus.NOT_FOUND, "Not found");

        when(restTemplate.postForObject(PAINTING_SERVICE_BASE_URL + "/" + galleryId + "/paintings" + "/" + paintingId + "/painters", painterRequestModel, PaintingPainterResponseModel.class))
                .thenThrow(new HttpClientErrorException(NOT_FOUND));

        assertThrows(NotFoundException.class, () ->paintingServiceClient.addPainterToPaintingInGallery(galleryId, paintingId, painterRequestModel));

    }

    @Test
    void addPainterToPainting_whenRestTemplateThrowsHttpClientErrorException_ThrowUnprocessableEntityException() {
        String galleryId = "1";
        String paintingId = "123";

        PainterRequestModel painterRequestModel = PainterRequestModel.builder()
                .name("Name 1")
                .origin("Origin 1")
                .birthDate("12-12-1212")
                .deathDate("12-12-1212")
                .build();
        HttpClientErrorException exception = new HttpClientErrorException(UNPROCESSABLE_ENTITY, "Unprocessable Entity");

        when(restTemplate.postForObject(PAINTING_SERVICE_BASE_URL + "/" + galleryId + "/paintings" + "/" + paintingId + "/painters", painterRequestModel, PaintingPainterResponseModel.class))
                .thenThrow(new HttpClientErrorException(UNPROCESSABLE_ENTITY));

        assertThrows(InvalidInputException.class, () -> paintingServiceClient.addPainterToPaintingInGallery(galleryId, paintingId, painterRequestModel));

    }

    @Test
    public void updatePainting_ReturnsUpdatedPainting() {
        String galleryId = "1";
        String paintingId = "123";
        PaintingRequestModel paintingRequestModel = PaintingRequestModel.builder()
                .title("Title 1")
                .yearCreated(1234)
                .painterId("111")
                .build();

        assertDoesNotThrow(() -> paintingServiceClient.updatePaintingInGallery(galleryId, paintingId, paintingRequestModel));
    }

    @Test
    void updatePainting__whenRestTemplateThrowsHttpClientErrorException_ThrowNotFoundException() {
        String galleryId = "1";
        String paintingId = "123";
        PaintingRequestModel paintingRequestModel = PaintingRequestModel.builder()
                .title("Title 1")
                .yearCreated(1234)
                .painterId("111")
                .build();
        HttpClientErrorException exception = new HttpClientErrorException(HttpStatus.NOT_FOUND, "Not found");
        Mockito.doThrow(exception)
                .when(restTemplate).put(PAINTING_SERVICE_BASE_URL + "/" + galleryId + "/paintings" + "/" + paintingId, paintingRequestModel);
        assertThrows(NotFoundException.class, () -> paintingServiceClient.updatePaintingInGallery(galleryId, paintingId, paintingRequestModel));
    }

    @Test
    void updatePainting__whenRestTemplateThrowsHttpClientErrorException_ThrowUnprocessableEntityException() {
        String galleryId = "1";
        String paintingId = "123";
        PaintingRequestModel paintingRequestModel = PaintingRequestModel.builder()
                .title("Title 1")
                .yearCreated(1234)
                .painterId("111")
                .build();
        HttpClientErrorException exception = new HttpClientErrorException(UNPROCESSABLE_ENTITY, "Unprocessable Entity");
        Mockito.doThrow(exception)
                .when(restTemplate).put(PAINTING_SERVICE_BASE_URL + "/" + galleryId + "/paintings" + "/" + paintingId, paintingRequestModel);

        assertThrows(InvalidInputException.class, () -> paintingServiceClient.updatePaintingInGallery(galleryId, paintingId, paintingRequestModel));
    }

    @Test
    void updatePainting__whenRestTemplateThrowsHttpClientErrorException_ThrowException() {
        String galleryId = "1";
        String paintingId = "123";
        PaintingRequestModel paintingRequestModel = PaintingRequestModel.builder()
                .title("Title 1")
                .yearCreated(1234)
                .painterId("111")
                .build();
        HttpClientErrorException exception = new HttpClientErrorException(BAD_REQUEST, "");
        Mockito.doThrow(exception)
                .when(restTemplate).put(PAINTING_SERVICE_BASE_URL + "/" + galleryId + "/paintings" + "/" + paintingId, paintingRequestModel);
        assertThrows(HttpClientErrorException.class, () -> paintingServiceClient.updatePaintingInGallery(galleryId, paintingId, paintingRequestModel));
    }

    @Test
    public void updatePainterOfPaintingInGallery_shouldUpdatePainter(){
        String galleryId = "1";
        String paintingId = "123";
        String painterId = "111";

        PainterRequestModel painterRequestModel = PainterRequestModel.builder()
                .name("Name 1")
                .origin("Origin 1")
                .birthDate("12-12-1212")
                .deathDate("12-12-1212")
                .build();
        // Call the updateGallery method and expect an exception to be thrown
        assertDoesNotThrow(() -> {
            paintingServiceClient.updatePainTerOfPaintingInGallery("1", "123", "111", painterRequestModel);
        });

        Mockito.verify(restTemplate, Mockito.times(1)).put(PAINTING_SERVICE_BASE_URL + "/" + galleryId + "/paintings" + "/" + paintingId + "/painters/" + painterId, painterRequestModel);
    }

    @Test
    void updatePainterOfPainting__whenRestTemplateThrowsHttpClientErrorException_ThrowNotFoundException() {
        String galleryId = "1";
        String paintingId = "123";
        String painterId = "111";

        PainterRequestModel painterRequestModel = PainterRequestModel.builder()
                .name("Name 1")
                .origin("Origin 1")
                .birthDate("12-12-1212")
                .deathDate("12-12-1212")
                .build();
        HttpClientErrorException exception = new HttpClientErrorException(HttpStatus.NOT_FOUND, "Not found");
        Mockito.doThrow(exception)
                .when(restTemplate).put(PAINTING_SERVICE_BASE_URL + "/" + galleryId + "/paintings" + "/" + paintingId + "/painters/" + painterId, painterRequestModel);
        assertThrows(NotFoundException.class, () ->  paintingServiceClient.updatePainTerOfPaintingInGallery("1", "123", "111", painterRequestModel));
    }

    @Test
    void updatePainterOfPainting__whenRestTemplateThrowsHttpClientErrorException_ThrowUnprocessableEntityException() {
        String galleryId = "1";
        String paintingId = "123";
        String painterId = "111";

        PainterRequestModel painterRequestModel = PainterRequestModel.builder()
                .name("Name 1")
                .origin("Origin 1")
                .birthDate("12-12-1212")
                .deathDate("12-12-1212")
                .build();
        HttpClientErrorException exception = new HttpClientErrorException(UNPROCESSABLE_ENTITY, "Unprocessable Entity");
        Mockito.doThrow(exception)
                .when(restTemplate).put(PAINTING_SERVICE_BASE_URL + "/" + galleryId + "/paintings" + "/" + paintingId + "/painters/" + painterId, painterRequestModel);

        assertThrows(InvalidInputException.class, () ->  paintingServiceClient.updatePainTerOfPaintingInGallery("1", "123", "111", painterRequestModel));
    }

    @Test
    void updatePainterOfPainting__whenRestTemplateThrowsHttpClientErrorException_ThrowException() {
        String galleryId = "1";
        String paintingId = "123";
        String painterId = "111";

        PainterRequestModel painterRequestModel = PainterRequestModel.builder()
                .name("Name 1")
                .origin("Origin 1")
                .birthDate("12-12-1212")
                .deathDate("12-12-1212")
                .build();
        HttpClientErrorException exception = new HttpClientErrorException(BAD_REQUEST, "");
        Mockito.doThrow(exception)
                .when(restTemplate).put(PAINTING_SERVICE_BASE_URL + "/" + galleryId + "/paintings" + "/" + paintingId + "/painters/" + painterId, painterRequestModel);
        assertThrows(HttpClientErrorException.class, () ->  paintingServiceClient.updatePainTerOfPaintingInGallery("1", "123", "111", painterRequestModel));
    }

    @Test
    public void removeValidPaintingInGallery_ShouldSucceed(){
        String galleryId = "1";
        String paintingId = "123";
        PaintingRequestModel paintingRequestModel = PaintingRequestModel.builder()
                .title("Title 1")
                .yearCreated(1234)
                .painterId("111")
                .build();

        PaintingResponseModel expectedPaintingResponseModel = PaintingResponseModel.builder()
                .paintingId(paintingId)
                .title("Title 1")
                .yearCreated(1234)
                .painterId("111")
                .build();
        PainterResponseModel expectedPainterResponseModel = PainterResponseModel.builder()
                .painterId("111")
                .name("Name 1")
                .origin("Origin 1")
                .birthDate("12-12-1212")
                .deathDate("12-12-1212")
                .build();

        PaintingPainterResponseModel expectedPaintingPainterResponseModel = new PaintingPainterResponseModel(expectedPaintingResponseModel, expectedPainterResponseModel);

        when(paintingServiceClient.addPaintingInGallery(galleryId, paintingRequestModel)).thenReturn(expectedPaintingPainterResponseModel);
        assertDoesNotThrow(() -> paintingServiceClient.removePaintingByIdInGallery(galleryId, paintingId));
        assertThrows(NullPointerException.class, () -> paintingServiceClient.getPaintingAggregateById(galleryId, paintingId));
        Mockito.verify(restTemplate, Mockito.times(1)).delete(PAINTING_SERVICE_BASE_URL + "/" + galleryId + "/paintings" + "/" + paintingId);
    }

    @Test
    void removePainting_whenRestTemplateThrowsHttpClientErrorException_ThrowNotFoundException() {
        String galleryId = "1";
        String paintingId = "123";
        HttpClientErrorException exception = new HttpClientErrorException(HttpStatus.NOT_FOUND, "Not found");

        Mockito.doThrow(exception).when(restTemplate).delete(PAINTING_SERVICE_BASE_URL + "/" + galleryId + "/paintings" + "/" + paintingId);

        assertThrows(NotFoundException.class, () ->paintingServiceClient.removePaintingByIdInGallery(galleryId, paintingId));
    }

    @Test
    void removePainting_whenRestTemplateThrowsHttpClientErrorException_ThrowUnprocessableEntityException() {
        String galleryId = "1";
        String paintingId = "123";
        HttpClientErrorException exception = new HttpClientErrorException(UNPROCESSABLE_ENTITY, "Unprocessable Entity");

        Mockito.doThrow(exception).when(restTemplate).delete(PAINTING_SERVICE_BASE_URL + "/" + galleryId + "/paintings" + "/" + paintingId);

        assertThrows(InvalidInputException.class, () ->paintingServiceClient.removePaintingByIdInGallery(galleryId, paintingId));
    }

    @Test
    void removePainting_whenRestTemplateThrowsHttpClientErrorException_ThrowException() {
        String galleryId = "1";
        String paintingId = "123";
        HttpClientErrorException exception = new HttpClientErrorException(BAD_REQUEST, "");

        Mockito.doThrow(exception).when(restTemplate).delete(PAINTING_SERVICE_BASE_URL + "/" + galleryId + "/paintings" + "/" + paintingId);

        assertThrows(HttpClientErrorException.class, () -> paintingServiceClient.removePaintingByIdInGallery(galleryId, paintingId));
    }

    @Test
    public void removePainterOfPaintingInGallery_shouldRemovePainter() {
        String painterId = "111";

        assertDoesNotThrow(() -> paintingServiceClient.removePainterOfPaintingInGallery("1", "123", painterId));
        Mockito.verify(restTemplate, Mockito.times(1)).delete(PAINTING_SERVICE_BASE_URL + "/" + "1" + "/paintings" + "/" + "123" + "/painters" + "/" + painterId);
    }

    @Test
    void removePainterOfPainting_whenRestTemplateThrowsHttpClientErrorException_ThrowNotFoundException() {
        String painterId = "111";
        HttpClientErrorException exception = new HttpClientErrorException(HttpStatus.NOT_FOUND, "Not found");

        Mockito.doThrow(exception).when(restTemplate).delete(PAINTING_SERVICE_BASE_URL + "/" + "1" + "/paintings" + "/" + "123" + "/painters" + "/" + painterId);

        assertThrows(NotFoundException.class, () ->  paintingServiceClient.removePainterOfPaintingInGallery("1", "123", painterId));
    }

    @Test
    void removePainterOfPainting_whenRestTemplateThrowsHttpClientErrorException_ThrowUnprocessableEntityException() {
        String painterId = "111";
        HttpClientErrorException exception = new HttpClientErrorException(UNPROCESSABLE_ENTITY, "Unprocessable Entity");

        Mockito.doThrow(exception).when(restTemplate).delete(PAINTING_SERVICE_BASE_URL + "/" + "1" + "/paintings" + "/" + "123" + "/painters" + "/" + painterId);

        assertThrows(InvalidInputException.class, () -> paintingServiceClient.removePainterOfPaintingInGallery("1", "123", painterId));
    }

    @Test
    void removePainterOfPainting_whenRestTemplateThrowsHttpClientErrorException_ThrowException() {
        String painterId = "111";
        HttpClientErrorException exception = new HttpClientErrorException(BAD_REQUEST, "");

        Mockito.doThrow(exception).when(restTemplate).delete(PAINTING_SERVICE_BASE_URL + "/" + "1" + "/paintings" + "/" + "123" + "/painters" + "/" + painterId);

        assertThrows(HttpClientErrorException.class, () ->  paintingServiceClient.removePainterOfPaintingInGallery("1", "123", painterId));
    }

}
