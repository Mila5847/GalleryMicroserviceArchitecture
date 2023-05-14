package com.gallery.exhibitionservice.presentationlayer.domainclientlayer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gallery.exhibitionservice.domainclientlayer.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

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
}
