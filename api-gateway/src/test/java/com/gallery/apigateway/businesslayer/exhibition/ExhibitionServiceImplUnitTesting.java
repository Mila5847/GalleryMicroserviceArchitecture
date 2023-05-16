package com.gallery.apigateway.businesslayer.exhibition;

import com.gallery.apigateway.domainclientlayer.exhibition.ExhibitionServiceClient;
import com.gallery.apigateway.presentationlayer.exhibition.ExhibitionRequestModel;
import com.gallery.apigateway.presentationlayer.exhibition.ExhibitionResponseModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

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
        ExhibitionRequestModel exhibitionRequestModel = new ExhibitionRequestModel();
        ExhibitionResponseModel expectedResponse = new ExhibitionResponseModel();
        when(exhibitionServiceClient.updateExhibition(exhibitionId, exhibitionRequestModel)).thenReturn(expectedResponse);

        ExhibitionResponseModel actualResponse = exhibitionService.updateExhibition(exhibitionId, exhibitionRequestModel);

        assertEquals(expectedResponse, actualResponse);
        verify(exhibitionServiceClient, times(1)).updateExhibition(exhibitionId, exhibitionRequestModel);
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