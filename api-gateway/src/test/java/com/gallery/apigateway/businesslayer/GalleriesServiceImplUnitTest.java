package com.gallery.apigateway.businesslayer;

import com.gallery.apigateway.domainclientlayer.GalleryServiceClient;
import com.gallery.apigateway.presentationlayer.GalleryRequestModel;
import com.gallery.apigateway.presentationlayer.GalleryResponseModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestPropertySource(properties = "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration")
class GalleriesServiceImplUnitTest {

    @MockBean
    private GalleryServiceClient galleryServiceClient;

    @Autowired
    private GalleriesServiceImpl galleriesService;


    @Test
    public void testGetGalleryById() {
        String galleryId = "1234";
        GalleryResponseModel expectedResponse = GalleryResponseModel.builder()
                .galleryId(galleryId)
                .name("gallery1")
                .openFrom("Monday")
                .openUntil("Friday")
                .streetAddress("1234 Main St")
                .city("City")
                .province("Province")
                .postalCode("A1A 1A1")
                .country("Country")
                .build();
        when(galleryServiceClient.getGallery(galleryId)).thenReturn(expectedResponse);

        GalleryResponseModel actualResponse = galleriesService.getGalleryById(galleryId);

        assertEquals(expectedResponse, actualResponse);
        verify(galleryServiceClient, times(1)).getGallery(galleryId);
    }

    @Test
    public void testGetAllGalleries() {
        GalleryResponseModel galleryResponseModel1 = GalleryResponseModel.builder()
                .galleryId("1234")
                .name("gallery1")
                .openFrom("Monday")
                .openUntil("Friday")
                .streetAddress("1234 Main St")
                .city("City")
                .province("Province")
                .postalCode("A1A 1A1")
                .country("Country")
                .build();

        GalleryResponseModel galleryResponseModel2 = GalleryResponseModel.builder()
                .galleryId("5678")
                .name("gallery2")
                .openFrom("Monday")
                .openUntil("Friday")
                .streetAddress("1234 Main St")
                .city("City")
                .province("Province")
                .postalCode("A1A 1A1")
                .country("Country")
                .build();

        GalleryResponseModel[] expectedResponse = {galleryResponseModel1, galleryResponseModel2};
        when(galleryServiceClient.getAllGalleries()).thenReturn(expectedResponse);

        GalleryResponseModel[] actualResponse = galleriesService.getAllGalleries();

        assertEquals(expectedResponse, actualResponse);
        verify(galleryServiceClient, times(1)).getAllGalleries();
    }

    @Test
    public void testAddGallery() {
        GalleryRequestModel galleryRequestModel = GalleryRequestModel.builder()
                .name("gallery1")
                .openFrom("Monday")
                .openUntil("Friday")
                .streetAddress("1234 Main St")
                .city("City")
                .province("Province")
                .postalCode("A1A 1A1")
                .country("Country")
                .build();
        GalleryResponseModel expectedResponse = new GalleryResponseModel();
        when(galleryServiceClient.addGallery(galleryRequestModel)).thenReturn(expectedResponse);

        GalleryResponseModel actualResponse = galleriesService.addGallery(galleryRequestModel);

        assertEquals(expectedResponse, actualResponse);
        verify(galleryServiceClient, times(1)).addGallery(galleryRequestModel);
    }

    @Test
    public void testUpdateGallery() {
        String galleryId = "1234";
        GalleryRequestModel galleryRequestModel = GalleryRequestModel.builder()
                .name("gallery1")
                .openFrom("Monday")
                .openUntil("Friday")
                .streetAddress("1234 Main St")
                .city("City")
                .province("Province")
                .postalCode("A1A 1A1")
                .country("Country")
                .build();

        galleriesService.updateGallery(galleryId, galleryRequestModel);

        verify(galleryServiceClient, times(1)).updateGallery(galleryId, galleryRequestModel);
    }

    @Test
    public void testDeleteGallery() {
        String galleryId = "1234";

        galleriesService.deleteGallery(galleryId);

        verify(galleryServiceClient, times(1)).deleteGallery(galleryId);
    }
}