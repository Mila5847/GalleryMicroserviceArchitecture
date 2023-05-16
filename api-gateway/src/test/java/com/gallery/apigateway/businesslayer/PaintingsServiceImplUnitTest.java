package com.gallery.apigateway.businesslayer;

import com.gallery.apigateway.domainclientlayer.PaintingServiceClient;
import com.gallery.apigateway.presentationlayer.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestPropertySource(properties = "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration")
class PaintingsServiceImplUnitTest {

    @MockBean
    private PaintingServiceClient paintingServiceClient;

    @Autowired
    private PaintingsServiceImpl paintingsService;


    @Test
    public void testGetPaintingsInGallery() {
        String galleryId = "1234";
        PaintingResponseModel paintingResponseModel1 = PaintingResponseModel.builder()
                .paintingId("5678")
                .title("paintingName")
                .yearCreated(2020)
                .painterId("1111")
                .galleryId(galleryId)
                .build();

        PaintingResponseModel paintingResponseModel2 = PaintingResponseModel.builder()
                .paintingId("5678")
                .title("paintingName")
                .yearCreated(2020)
                .painterId("1111")
                .galleryId(galleryId)
                .build();
        PaintingResponseModel[] expectedResponse = {paintingResponseModel1, paintingResponseModel2};
        when(paintingServiceClient.getPaintingsInGallery(galleryId)).thenReturn(expectedResponse);

        PaintingResponseModel[] actualResponse = paintingsService.getPaintingsInGallery(galleryId);

        assertEquals(expectedResponse, actualResponse);
        verify(paintingServiceClient, times(1)).getPaintingsInGallery(galleryId);
    }

    @Test
    public void testGetPaintingAggregateByIdInGallery() {
        String galleryId = "1234";
        String paintingId = "5678";
        String painterId = "1111";

        PaintingResponseModel paintingResponseModel = PaintingResponseModel.builder()
                .paintingId(paintingId)
                .title("paintingName")
                .yearCreated(2020)
                .painterId(painterId)
                .galleryId(galleryId)
                .build();

        PainterResponseModel painterResponseModel = PainterResponseModel.builder()
                .painterId(painterId)
                .name("Mila")
                .origin("Bulgaria")
                .birthDate("01/01/1990")
                .deathDate("01/01/1990")
                .build();

        PaintingPainterResponseModel expectedResponse = new PaintingPainterResponseModel(paintingResponseModel, painterResponseModel);
        when(paintingServiceClient.getPaintingAggregateById(galleryId, paintingId)).thenReturn(expectedResponse);

        PaintingPainterResponseModel actualResponse = paintingsService.getPaintingAggregateByIdInGallery(galleryId, paintingId);

        assertEquals(expectedResponse, actualResponse);
        verify(paintingServiceClient, times(1)).getPaintingAggregateById(galleryId, paintingId);
    }

    @Test
    public void testGetPaintingsByPainterIdInGallery() {
        String galleryId = "1234";
        String paintingId = "5678";
        String painterId = "1111";

        PainterResponseModel painterResponseModel = PainterResponseModel.builder()
                .painterId(painterId)
                .name("Mila")
                .origin("Bulgaria")
                .birthDate("01/01/1990")
                .deathDate("01/01/1990")
                .build();

        List<PaintingResponseModel> paintingResponseModels = new ArrayList<>();
        paintingResponseModels.add(PaintingResponseModel.builder()
                .paintingId(paintingId)
                .title("paintingName")
                .yearCreated(2020)
                .painterId(painterId)
                .galleryId(galleryId)
                .build());

        PaintingsOfPainterResponseModel expectedResponse = new PaintingsOfPainterResponseModel(painterId, painterResponseModel, paintingResponseModels);
        when(paintingServiceClient.getPaintingAggregateByPainterIdInGallery(galleryId, painterId)).thenReturn(expectedResponse);

        PaintingsOfPainterResponseModel actualResponse = paintingsService.getPaintingsByPainterIdInGallery(galleryId, painterId);

        assertEquals(expectedResponse, actualResponse);
        verify(paintingServiceClient, times(1)).getPaintingAggregateByPainterIdInGallery(galleryId, painterId);
    }

    @Test
    public void testAddPaintingInGallery() {
        String galleryId = "1234";
        PaintingRequestModel paintingRequestModel = PaintingRequestModel.builder()
                .title("paintingName")
                .yearCreated(2020)
                .painterId("1111")
                .galleryId(galleryId)
                .build();
        PaintingPainterResponseModel expectedResponse = new PaintingPainterResponseModel();
        when(paintingServiceClient.addPaintingInGallery(galleryId, paintingRequestModel)).thenReturn(expectedResponse);

        PaintingPainterResponseModel actualResponse = paintingsService.addPaintingInGallery(galleryId, paintingRequestModel);

        assertEquals(expectedResponse, actualResponse);
        verify(paintingServiceClient, times(1)).addPaintingInGallery(galleryId, paintingRequestModel);
    }

    @Test
    public void testAddPainterToPaintingInGallery() {
        String galleryId = "1234";
        String paintingId = "5678";
        PainterRequestModel painterResponseModel = PainterRequestModel.builder()
                .name("Mila")
                .origin("Bulgaria")
                .birthDate("01/01/1990")
                .deathDate("01/01/1990")
                .build();
        PaintingPainterResponseModel expectedResponse = new PaintingPainterResponseModel();
        when(paintingServiceClient.addPainterToPaintingInGallery(galleryId, paintingId, painterResponseModel))
                .thenReturn(expectedResponse);

        PaintingPainterResponseModel actualResponse = paintingsService.addPainterToPaintingInGallery(
                galleryId, paintingId, painterResponseModel);

        assertEquals(expectedResponse, actualResponse);
        verify(paintingServiceClient, times(1))
                .addPainterToPaintingInGallery(galleryId, paintingId, painterResponseModel);
    }

    @Test
    public void testUpdatePaintingInGallery() {
        String galleryId = "1234";
        String paintingId = "5678";
        PaintingRequestModel paintingRequestModel = PaintingRequestModel.builder()
                .title("paintingNameUpdated")
                .yearCreated(2020)
                .painterId("1111")
                .galleryId(galleryId)
                .build();

        paintingsService.updatePaintingInGallery(galleryId, paintingId, paintingRequestModel);

        verify(paintingServiceClient, times(1))
                .updatePaintingInGallery(galleryId, paintingId, paintingRequestModel);
    }

    @Test
    public void testUpdatePainterOfPaintingInGallery() {
        String galleryId = "1234";
        String paintingId = "5678";
        String painterId = "1111";
        PainterRequestModel painterRequestModel = PainterRequestModel.builder()
                .name("Mila")
                .origin("Canada")
                .birthDate("01/01/1990")
                .deathDate("01/01/1990")
                .build();

        paintingsService.updatePainterOfPaintingInGallery(galleryId, paintingId, painterId, painterRequestModel);

        verify(paintingServiceClient, times(1))
                .updatePainTerOfPaintingInGallery(galleryId, paintingId, painterId, painterRequestModel);
    }

    @Test
    public void testRemovePaintingByIdInGallery() {
        String galleryId = "gallery1";
        String paintingId = "painting1";

        paintingsService.removePaintingByIdInGallery(galleryId, paintingId);

        verify(paintingServiceClient, times(1)).removePaintingByIdInGallery(galleryId, paintingId);
    }

    @Test
    public void testRemovePainterOfPaintingInGallery() {
        String galleryId = "gallery1";
        String paintingId = "painting1";
        String painterId = "painter1";

        paintingsService.removePainterOfPaintingInGallery(galleryId, paintingId, painterId);

        verify(paintingServiceClient, times(1))
                .removePainterOfPaintingInGallery(galleryId, paintingId, painterId);
    }

}