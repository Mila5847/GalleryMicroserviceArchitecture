package com.gallery.apigateway.businesslayer;

import com.gallery.apigateway.domainclientlayer.SculptureServiceClient;
import com.gallery.apigateway.presentationlayer.SculptureRequestModel;
import com.gallery.apigateway.presentationlayer.SculptureResponseModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@SpringBootTest
@TestPropertySource(properties = "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration")
class SculptureServiceImplUnitTest {

    @MockBean
    private SculptureServiceClient sculptureServiceClient;

    @Autowired
    private SculptureServiceImpl sculptureService;

    @Test
    public void testGetSculptureByIdInGallery() {
        String galleryId = "1234";
        String sculptureId = "5678";
        SculptureResponseModel expectedResponse = SculptureResponseModel.builder()
                .sculptureId(sculptureId)
                .title("title")
                .texture("texture")
                .material("material")
                .galleryId(galleryId)
                .build();
        when(sculptureServiceClient.getSculptureById(galleryId, sculptureId)).thenReturn(expectedResponse);

        SculptureResponseModel actualResponse = sculptureService.getSculptureByIdInGallery(galleryId, sculptureId);

        assertEquals(expectedResponse, actualResponse);
        verify(sculptureServiceClient, times(1)).getSculptureById(galleryId, sculptureId);
    }

    @Test
    public void testGetAllSculpturesInGallery() {
        String galleryId = "1234";
        SculptureResponseModel sculptureResponseModel1 = SculptureResponseModel.builder()
                .sculptureId("5678")
                .title("title")
                .texture("texture")
                .material("material")
                .galleryId(galleryId)
                .build();

        SculptureResponseModel sculptureResponseModel2 = SculptureResponseModel.builder()
                .sculptureId("5678")
                .title("title")
                .texture("texture")
                .material("material")
                .galleryId(galleryId)
                .build();


        SculptureResponseModel[] expectedResponse = {sculptureResponseModel1, sculptureResponseModel2};
        when(sculptureServiceClient.getAllSculpturesInGallery(galleryId)).thenReturn(expectedResponse);

        SculptureResponseModel[] actualResponse = sculptureService.getAllSculpturesInGallery(galleryId);

        assertEquals(expectedResponse, actualResponse);
        verify(sculptureServiceClient, times(1)).getAllSculpturesInGallery(galleryId);
    }

    @Test
    public void testAddSculptureToGallery() {
        String galleryId = "1234";
        SculptureRequestModel sculptureRequestModel = SculptureRequestModel.builder()
                .title("title")
                .texture("texture")
                .material("material")
                .galleryId(galleryId)
                .build();
        SculptureResponseModel expectedResponse = SculptureResponseModel.builder()
                .sculptureId("5678")
                .title("title")
                .texture("texture")
                .material("material")
                .galleryId(galleryId)
                .build();

        when(sculptureServiceClient.addSculptureInGallery(galleryId, sculptureRequestModel)).thenReturn(expectedResponse);

        SculptureResponseModel actualResponse = sculptureService.addSculptureToGallery(galleryId, sculptureRequestModel);

        assertEquals(expectedResponse, actualResponse);
        verify(sculptureServiceClient, times(1)).addSculptureInGallery(galleryId, sculptureRequestModel);
    }

    @Test
    public void testUpdateSculptureInGallery() {
        String galleryId = "1234";
        String sculptureId = "5678";
        SculptureRequestModel sculptureRequestModel = SculptureRequestModel.builder()
                .title("title")
                .texture("texture")
                .material("material")
                .galleryId(galleryId)
                .build();

        sculptureService.updateSculptureInGallery(galleryId, sculptureId, sculptureRequestModel);

        verify(sculptureServiceClient, times(1))
                .updateSculptureInGallery(galleryId, sculptureId, sculptureRequestModel);
    }

    @Test
    public void testDeleteSculptureInGallery() {
        String galleryId = "1234";
        String sculptureId = "5678";

        sculptureService.deleteSculptureInGallery(galleryId, sculptureId);

        verify(sculptureServiceClient, times(1)).deleteSculpture(galleryId, sculptureId);
    }
}