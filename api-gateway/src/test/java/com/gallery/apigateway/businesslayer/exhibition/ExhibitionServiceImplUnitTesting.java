package com.gallery.apigateway.businesslayer.exhibition;

import com.gallery.apigateway.domainclientlayer.exhibition.ExhibitionServiceClient;
import com.gallery.apigateway.presentationlayer.PaintingResponseModel;
import com.gallery.apigateway.presentationlayer.SculptureResponseModel;
import com.gallery.apigateway.presentationlayer.exhibition.ExhibitionRequestModel;
import com.gallery.apigateway.presentationlayer.exhibition.ExhibitionResponseModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestPropertySource(properties = "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration")
class ExhibitionServiceImplUnitTesting {

    @MockBean
    private ExhibitionServiceClient exhibitionServiceClient;

    @Autowired
    private ExhibitionServiceImpl exhibitionService;

    @Test
    public void testGetAllExhibitions() {
        ExhibitionResponseModel[] expectedResponse = {new ExhibitionResponseModel(), new ExhibitionResponseModel()};
        when(exhibitionServiceClient.getAllExhibitions()).thenReturn(expectedResponse);

        ExhibitionResponseModel[] actualResponse = exhibitionService.getAllExhibitions();

        assertEquals(expectedResponse, actualResponse);
        verify(exhibitionServiceClient, times(1)).getAllExhibitions();
    }

    @Test
    public void testGetExhibitionById() {
        String exhibitionId = "exhibition1";
        ExhibitionResponseModel expectedResponse = new ExhibitionResponseModel();
        when(exhibitionServiceClient.getExhibition(exhibitionId)).thenReturn(expectedResponse);

        ExhibitionResponseModel actualResponse = exhibitionService.getExhibitionById(exhibitionId);

        assertEquals(expectedResponse, actualResponse);
        verify(exhibitionServiceClient, times(1)).getExhibition(exhibitionId);
    }

    @Test
    public void testCreateExhibition() {
        String galleryId = "1234";
        ExhibitionRequestModel exhibitionRequestModel = new ExhibitionRequestModel();
        ExhibitionResponseModel expectedResponse = new ExhibitionResponseModel();
        when(exhibitionServiceClient.createExhibition(galleryId, exhibitionRequestModel)).thenReturn(expectedResponse);

        ExhibitionResponseModel actualResponse = exhibitionService.createExhibition(galleryId, exhibitionRequestModel);

        assertEquals(expectedResponse, actualResponse);
        verify(exhibitionServiceClient, times(1)).createExhibition(galleryId, exhibitionRequestModel);
    }

    @Test
    public void testUpdateExhibition() {
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

        exhibitionServiceClient.updateExhibition(exhibitionId, exhibitionRequestModel);

        assertDoesNotThrow(() -> {
            exhibitionService.updateExhibition(exhibitionId, exhibitionRequestModel);
        });

    }

    @Test
    public void testRemoveExhibition() {
        String exhibitionId = "1234";

        exhibitionService.removeExhibition(exhibitionId);

        verify(exhibitionServiceClient, times(1)).removeExhibition(exhibitionId);
    }

    @Test
    public void testRemoveAllExhibitions() {
        exhibitionService.removeAllExhibitions();

        verify(exhibitionServiceClient, times(1)).removeAllExhibitions();
    }

}