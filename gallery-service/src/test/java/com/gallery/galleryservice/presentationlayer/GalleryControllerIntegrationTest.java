package com.gallery.galleryservice.presentationlayer;

import com.gallery.galleryservice.datalayer.Address;
import com.gallery.galleryservice.datalayer.GalleryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class GalleryControllerIntegrationTest {
    private final String BASE_URI_GALLERIES = "/api/v1/galleries";
    private final String VALID_GALLERY_ID = "ea85d3ba-d708-4ff3-bbbb-dd9c5c77b8e8";
    private final String VALID_GALLERY_NAME = "Art Gallery of Ontario";
    private final String VALID_GALLERY_OPEN_FROM = "Wednesday 10:00 AM";
    private final String VALID_GALLERY_OPEN_UNTIL = "Sunday 5:00 PM";
    private final Address VALID_GALLERY_ADDRESS = new Address("317 Dundas St W", "Toronto", "Ontario", "Canada", "M5T 1G4");

    @Autowired
    WebTestClient webTestClient;
    @Autowired
    GalleryRepository galleryRepository;

    @AfterEach
    public void tearDown(){
        galleryRepository.deleteAll();
    }

    @Sql({"/data-mysql.sql"})
    @Test
    public void whenGalleriesExist_thenReturnAllGalleries(){
        // arrange
        Integer expectedNumberOfGalleries = 3;

        // act
        webTestClient.get()
                .uri(BASE_URI_GALLERIES)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.length()").isEqualTo(expectedNumberOfGalleries);
    }

    // ADD
    // UPDATE

    @Sql({"/data-mysql.sql"})
    @Test
    public void whenGalleryWithValidIdExist_thenReturnGallery(){
        // act
        webTestClient.get()
                .uri(BASE_URI_GALLERIES+"/"+VALID_GALLERY_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.galleryId").isEqualTo(VALID_GALLERY_ID)
                .jsonPath("$.name").isEqualTo(VALID_GALLERY_NAME)
                .jsonPath("$.openFrom").isEqualTo(VALID_GALLERY_OPEN_FROM)
                .jsonPath("$.openUntil").isEqualTo(VALID_GALLERY_OPEN_UNTIL)
                .jsonPath("$.streetAddress").isEqualTo(VALID_GALLERY_ADDRESS.getStreetAddress())
                .jsonPath("$.city").isEqualTo(VALID_GALLERY_ADDRESS.getCity())
                .jsonPath("$.province").isEqualTo(VALID_GALLERY_ADDRESS.getProvince())
                .jsonPath("$.country").isEqualTo(VALID_GALLERY_ADDRESS.getCountry())
                .jsonPath("$.postalCode").isEqualTo(VALID_GALLERY_ADDRESS.getPostalCode());

    }

}