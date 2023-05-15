package com.gallery.exhibitionservice.presentationlayer.domainclientlayer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gallery.exhibitionservice.domainclientlayer.*;
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
import static org.springframework.http.HttpStatus.NOT_FOUND;

public class PaintingServiceClientUnitTest {

    private RestTemplate restTemplate;
    private ObjectMapper objectMapper;
    private String PAINTING_SERVICE_BASE_URL;
    private PaintingServiceClient paintingServiceClient;

    @BeforeEach
    public void setUp() {
        restTemplate = Mockito.mock(RestTemplate.class);
        paintingServiceClient = new PaintingServiceClient(restTemplate, objectMapper, "localhost", "8080");
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
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
    void getAllPaintings_whenRestTemplateThrowsHttpClientErrorException_ThrowException() {
        when(restTemplate.getForObject( PAINTING_SERVICE_BASE_URL  + "/" + "1" + "/paintings", PaintingResponseModel[].class))
                .thenThrow(new HttpClientErrorException(NOT_FOUND));

        assertThrows(NullPointerException.class, () -> paintingServiceClient.getPaintingsInGallery("1"));
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
    public void getPainting_whenRestTemplateThrowsHttpClientErrorException_ThrowException() {
        when(restTemplate.getForObject( PAINTING_SERVICE_BASE_URL  + "/" + "1" + "/paintings" + "/" + "1", PaintingPainterResponseModel.class))
                .thenThrow(new HttpClientErrorException(NOT_FOUND));

        assertThrows(NullPointerException.class, () -> paintingServiceClient.getPaintingAggregateById("1", "1"));
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
    public void getPaintingAggregateByPainterIdInGallery_whenRestTemplateThrowsHttpClientErrorException_ThrowException() {
        when(restTemplate.getForObject( PAINTING_SERVICE_BASE_URL  + "/" + "1" + "/painters" + "/" + "111" + "/paintings", PaintingsOfPainterResponseModel.class))
                .thenThrow(new HttpClientErrorException(NOT_FOUND));

        assertThrows(NullPointerException.class, () -> paintingServiceClient.getPaintingAggregateByPainterIdInGallery("1", "111"));
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
    public void addPainting_whenRestTemplateThrowsHttpClientErrorException_ThrowException() {
        PaintingRequestModel paintingRequestModel = PaintingRequestModel.builder()
                .title("Title 1")
                .yearCreated(1234)
                .painterId("111")
                .build();

        when(restTemplate.postForObject(PAINTING_SERVICE_BASE_URL + "/" + "1" + "/paintings", paintingRequestModel, PaintingPainterResponseModel.class))
                .thenThrow(new HttpClientErrorException(NOT_FOUND));

        assertThrows(NullPointerException.class, () -> paintingServiceClient.addPaintingInGallery("1", paintingRequestModel));
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
    void addPainterToPainting_WhenRestTemplateThrowsHttpClientErrorException_ThrowException(){
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
                    .thenThrow(new HttpClientErrorException(NOT_FOUND));
            assertThrows(NullPointerException.class, () -> paintingServiceClient.addPainterToPaintingInGallery(galleryId, paintingId, painterRequestModel));
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
    void updatePainting_WhenRestTemplateThrowsHttpClientErrorException_ThrowException(){
        // Mock the RestTemplate to throw an HttpClientErrorException
        HttpStatus errorStatus = HttpStatus.NOT_FOUND;
        HttpClientErrorException exception = new HttpClientErrorException(errorStatus);
        Mockito.doThrow(exception).when(restTemplate).put(Mockito.anyString(), Mockito.any(PaintingRequestModel.class));

        // Create a mock GalleryRequestModel
        PaintingRequestModel paintingRequestModel = PaintingRequestModel.builder()
                .title("Title 1")
                .yearCreated(1234)
                .painterId("111")
                .build();

        // Call the updateGallery method and expect an exception to be thrown
        assertThrows(NullPointerException.class, () -> {
            paintingServiceClient.updatePaintingInGallery("1", "123", paintingRequestModel);
        });

    }

    @Test
    public void updatePainterOfPaintingInGallery_shouldUpdatePainter(){
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

        Mockito.verify(restTemplate, Mockito.times(1)).put(Mockito.anyString(), Mockito.any(PainterRequestModel.class));
    }

    @Test
    public void updatePainterOfPaintingInGallery_shouldThrowException() {
        HttpStatus errorStatus = HttpStatus.NOT_FOUND;
        HttpClientErrorException exception = new HttpClientErrorException(errorStatus);
        Mockito.doThrow(exception).when(restTemplate).put(Mockito.anyString(), Mockito.any(PainterRequestModel.class));

        PainterRequestModel painterRequestModel = PainterRequestModel.builder()
                .name("Name 1")
                .origin("Origin 1")
                .birthDate("12-12-1212")
                .deathDate("12-12-1212")
                .build();

        // Call the updateGallery method and expect an exception to be thrown
        assertThrows(NullPointerException.class, () -> {
            paintingServiceClient.updatePainTerOfPaintingInGallery("1", "123", "111", painterRequestModel);
        });
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
    public void removePaintingInGallery_shouldThrowException() {
        HttpStatus errorStatus = HttpStatus.NOT_FOUND;
        HttpClientErrorException exception = new HttpClientErrorException(errorStatus);
        Mockito.doThrow(exception).when(restTemplate).delete(Mockito.anyString());

        assertThrows(NullPointerException.class, () -> {
            paintingServiceClient.removePaintingByIdInGallery("1", "123");
        });
    }

    @Test
    public void removePainterOfPaintingInGallery_shouldRemovePainter() {
        String painterId = "111";

        assertDoesNotThrow(() -> paintingServiceClient.removePainterOfPaintingInGallery("1", "123", painterId));
        Mockito.verify(restTemplate, Mockito.times(1)).delete(PAINTING_SERVICE_BASE_URL + "/" + "1" + "/paintings" + "/" + "123" + "/painters" + "/" + painterId);
    }

    @Test
public void removePainterOfPaintingInGallery_shouldThrowException() {
        HttpStatus errorStatus = HttpStatus.NOT_FOUND;
        HttpClientErrorException exception = new HttpClientErrorException(errorStatus);
        Mockito.doThrow(exception).when(restTemplate).delete(Mockito.anyString());

        assertThrows(NullPointerException.class, () -> {
            paintingServiceClient.removePainterOfPaintingInGallery("1", "123", "111");
        });
    }
}
