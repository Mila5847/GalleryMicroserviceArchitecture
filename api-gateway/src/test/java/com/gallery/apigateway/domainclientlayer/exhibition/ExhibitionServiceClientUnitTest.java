package com.gallery.apigateway.domainclientlayer.exhibition;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gallery.apigateway.domainclientlayer.GalleryServiceClient;
import com.gallery.apigateway.presentationlayer.PaintingResponseModel;
import com.gallery.apigateway.presentationlayer.SculptureResponseModel;
import com.gallery.apigateway.presentationlayer.exhibition.ExhibitionRequestModel;
import com.gallery.apigateway.presentationlayer.exhibition.ExhibitionResponseModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

        when(restTemplate.getForObject("http://localhost:8080/api/v1/exhibitions/123", ExhibitionResponseModel.class))
                .thenReturn(expectedResponse);

        ExhibitionResponseModel result = exhibitionServiceClient.getExhibition(exhibitionId);

        assertEquals(expectedResponse, result);

        verify(restTemplate, times(1)).getForObject("http://localhost:8080/api/v1/exhibitions/123", ExhibitionResponseModel.class);
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

        when(restTemplate.postForObject("http://localhost:8080/api/v1/exhibitions/galleries/456", requestModel, ExhibitionResponseModel.class))
                .thenReturn(expectedResponse);

        ExhibitionResponseModel result = exhibitionServiceClient.createExhibition(galleryId, requestModel);

        assertEquals(expectedResponse, result);

        verify(restTemplate, times(1)).postForObject("http://localhost:8080/api/v1/exhibitions/galleries/456", requestModel, ExhibitionResponseModel.class);
    }
}