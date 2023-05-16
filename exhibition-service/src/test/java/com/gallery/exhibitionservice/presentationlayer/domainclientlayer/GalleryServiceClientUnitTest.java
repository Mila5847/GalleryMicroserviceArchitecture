package com.gallery.exhibitionservice.presentationlayer.domainclientlayer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gallery.exhibitionservice.domainclientlayer.GalleryRequestModel;
import com.gallery.exhibitionservice.domainclientlayer.GalleryResponseModel;
import com.gallery.exhibitionservice.domainclientlayer.GalleryServiceClient;
import com.gallery.exhibitionservice.utils.HttpErrorInfo;
import com.gallery.exhibitionservice.utils.exceptions.InvalidInputException;
import com.gallery.exhibitionservice.utils.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.client.ResponseCreator;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;

public class GalleryServiceClientUnitTest {
    private RestTemplate restTemplate;
    private ObjectMapper objectMapper;
    private String GALLERY_SERVICE_BASE_URL;
    private GalleryServiceClient galleryServiceClient;


    @BeforeEach
    void setUp() {
        restTemplate = Mockito.mock(RestTemplate.class);
        galleryServiceClient = new GalleryServiceClient(restTemplate, objectMapper, "localhost", "8080");
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.GALLERY_SERVICE_BASE_URL = "http://" + "localhost" + ":" + "8080" + "/api/v1/galleries";
    }

    @Test
    void getAllGalleriesReturnsArrayOfGalleries() {
        GalleryResponseModel[] expectedGalleryResponseModels = new GalleryResponseModel[2];
        expectedGalleryResponseModels[0] = GalleryResponseModel.builder()
                .galleryId("123")
                .name("Name 1")
                .openFrom("Tuesday")
                .openUntil("Friday")
                .build();
        expectedGalleryResponseModels[1] = GalleryResponseModel.builder()
                .galleryId("456")
                .name("Name 2")
                .openFrom("Monday")
                .openUntil("Saturday")
                .build();

        when(restTemplate.getForObject(GALLERY_SERVICE_BASE_URL, GalleryResponseModel[].class))
                .thenReturn(expectedGalleryResponseModels);

        GalleryResponseModel[] actualGalleryResponseModels = galleryServiceClient.getAllGalleries();
        assertArrayEquals(expectedGalleryResponseModels, actualGalleryResponseModels);
    }

    @Test
    void getAllGalleries_whenRestTemplateThrowsHttpClientErrorException_ThrowException() {
        when(restTemplate.getForObject(GALLERY_SERVICE_BASE_URL, GalleryResponseModel[].class))
                .thenThrow(new HttpClientErrorException(NOT_FOUND));

        assertThrows(NullPointerException.class, () -> galleryServiceClient.getAllGalleries());
    }

    @Test
    void getGalleryReturnsGallery() {
        String galleryId = "1";
        GalleryResponseModel expectedGalleryResponseModel = GalleryResponseModel.builder().build();

        when(restTemplate.getForObject(GALLERY_SERVICE_BASE_URL + "/" + galleryId, GalleryResponseModel.class))
                .thenReturn(expectedGalleryResponseModel);

        GalleryResponseModel actualGalleryResponseModel = galleryServiceClient.getGallery(galleryId);
        assertEquals(expectedGalleryResponseModel, actualGalleryResponseModel);
    }

    @Test
    void getGallery_whenRestTemplateThrowsHttpClientErrorException_ThrowException() {
        String galleryId = "1";
        when(restTemplate.getForObject(GALLERY_SERVICE_BASE_URL + "/" + galleryId, GalleryResponseModel.class))
                .thenThrow(new HttpClientErrorException(NOT_FOUND));

        assertThrows(NullPointerException.class, () -> galleryServiceClient.getGallery(galleryId));
    }

        @Test
        void addGalleryReturnsGallery() {
            GalleryRequestModel galleryRequestModel = buildGalleryRequestModel();
            GalleryResponseModel expectedGalleryResponseModel = new GalleryResponseModel();
            expectedGalleryResponseModel.setName(galleryRequestModel.getName());
            expectedGalleryResponseModel.setOpenFrom(galleryRequestModel.getOpenFrom());
            expectedGalleryResponseModel.setOpenUntil(galleryRequestModel.getOpenUntil());
            expectedGalleryResponseModel.setStreetAddress(galleryRequestModel.getStreetAddress());
            expectedGalleryResponseModel.setCity(galleryRequestModel.getCity());
            expectedGalleryResponseModel.setProvince(galleryRequestModel.getProvince());
            expectedGalleryResponseModel.setCountry(galleryRequestModel.getCountry());
            expectedGalleryResponseModel.setPostalCode(galleryRequestModel.getPostalCode());

            when(restTemplate.postForObject(GALLERY_SERVICE_BASE_URL, galleryRequestModel, GalleryResponseModel.class))
                    .thenReturn(expectedGalleryResponseModel);

            GalleryResponseModel actualGalleryResponseModel = galleryServiceClient.addGallery(galleryRequestModel);
            assertEquals(expectedGalleryResponseModel, actualGalleryResponseModel);
            assertEquals(expectedGalleryResponseModel.getName(), actualGalleryResponseModel.getName());
            assertEquals(expectedGalleryResponseModel.getOpenFrom(), actualGalleryResponseModel.getOpenFrom());
            assertEquals(expectedGalleryResponseModel.getOpenUntil(), actualGalleryResponseModel.getOpenUntil());
            assertEquals(expectedGalleryResponseModel.getStreetAddress(), actualGalleryResponseModel.getStreetAddress());
            assertEquals(expectedGalleryResponseModel.getCity(), actualGalleryResponseModel.getCity());
            assertEquals(expectedGalleryResponseModel.getProvince(), actualGalleryResponseModel.getProvince());
            assertEquals(expectedGalleryResponseModel.getCountry(), actualGalleryResponseModel.getCountry());
            assertEquals(expectedGalleryResponseModel.getPostalCode(), actualGalleryResponseModel.getPostalCode());
            assertEquals(expectedGalleryResponseModel.getGalleryId(), actualGalleryResponseModel.getGalleryId());
        }

        @Test
        void addGallery_whenRestTemplateThrowsHttpClientErrorException_ThrowException() {
            GalleryRequestModel galleryRequestModel = buildGalleryRequestModel();
            when(restTemplate.postForObject(GALLERY_SERVICE_BASE_URL, galleryRequestModel, GalleryResponseModel.class))
                    .thenThrow(new HttpClientErrorException(UNPROCESSABLE_ENTITY));

            assertThrows(NullPointerException.class, () -> galleryServiceClient.addGallery(galleryRequestModel));
        }

        @Test
        void updateGalleryDoesNotThrowException() {
            String galleryId = "1";
            GalleryRequestModel galleryRequestModel = buildGalleryRequestModel();

            assertDoesNotThrow(() -> galleryServiceClient.updateGallery(galleryId, galleryRequestModel));
        }

        @Test
        void updateGallery_whenRestTemplateThrowsHttpClientErrorException_ThrowException() {
            // Mock the RestTemplate to throw an HttpClientErrorException
            HttpStatus errorStatus = HttpStatus.NOT_FOUND;
            HttpClientErrorException exception = new HttpClientErrorException(errorStatus);
            Mockito.doThrow(exception).when(restTemplate).put(Mockito.anyString(), Mockito.any(GalleryRequestModel.class));

            // Create a mock GalleryRequestModel
            GalleryRequestModel galleryRequestModel = buildGalleryRequestModel();

            // Call the updateGallery method and expect an exception to be thrown
            assertThrows(NullPointerException.class, () -> {
                galleryServiceClient.updateGallery("1", galleryRequestModel);
            });
        }

        @Test
        void deleteGalleryDoesNotThrowException() {
            String galleryId = "1";

            assertDoesNotThrow(() -> galleryServiceClient.deleteGallery(galleryId));
            Mockito.verify(restTemplate).delete(GALLERY_SERVICE_BASE_URL + "/" + galleryId);
        }

        @Test
        void deleteGallery_whenRestTemplateThrowsHttpClientErrorException_ThrowException() {
            // Mock the RestTemplate to throw an HttpClientErrorException
            HttpStatus errorStatus = HttpStatus.NOT_FOUND;
            HttpClientErrorException exception = new HttpClientErrorException(errorStatus);
            Mockito.doThrow(exception).when(restTemplate).delete(Mockito.anyString());

            // Call the deleteGallery method and expect an exception to be thrown
            assertThrows(NullPointerException.class, () -> {
                galleryServiceClient.deleteGallery("1");
            });
        }

    private GalleryRequestModel buildGalleryRequestModel() {
        return GalleryRequestModel.builder()
                .name("Gallery 1")
                .openFrom("Monday")
                .openUntil("Friday")
                .streetAddress("123 Main St")
                .city("Toronto")
                .province("Ontario")
                .country("Canada")
                .postalCode("M1M1M1")
                .build();
    }
}
