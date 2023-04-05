package com.gallery.paintingservice.presentationlayer;

import com.gallery.paintingservice.datalayer.painter.PainterRepository;
import com.gallery.paintingservice.datalayer.painting.Painting;
import com.gallery.paintingservice.datalayer.painting.PaintingRepository;
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
class PaintingControllerIntegrationTest {
    private final String BASE_URI_GALLERIES = "/api/v1/galleries";
    private final String VALID_GALLERY_ID = "ea85d3ba-d708-4ff3-bbbb-dd9c5c77b8e8";
    private final String VALID_PAINTING_ID = "3ed9654a-b773-4aa0-ae6b-b22afb636c8e";
    private final String VALID_PAINTING_TITLE = "The Starry Night";
    private final Integer VALID_PAINTING_YEAR = 1889;
    private final String VALID_PAINTER_ID = "f4c80444-5acf-4d57-8902-9f55255e9e55";

    @Autowired
    WebTestClient webTestClient;
    @Autowired
    PaintingRepository paintingRepository;
    @Autowired
    PainterRepository painterRepository;

    @AfterEach
    public void tearDown(){
        paintingRepository.deleteAll();
        painterRepository.deleteAll();
    }

    @Sql({"/data-mysql.sql"})
    @Test
    public void whenPaintingsExist_thenReturnAllPaintingsInGallery(){
        // arrange
        Integer expectedNumPaintings = 5;

        // act
        webTestClient.get()
                .uri(BASE_URI_GALLERIES+"/"+VALID_GALLERY_ID+"/paintings")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.length()").isEqualTo(expectedNumPaintings);
    }

    @Sql({"/data-mysql.sql"})
    @Test
    public void whenPaintingWithValidIdInGalleryExists_thenReturnPainting(){
        // act
        webTestClient.get()
                .uri(BASE_URI_GALLERIES+"/"+VALID_GALLERY_ID+"/paintings/"+VALID_PAINTING_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.paintingResponseModel.paintingId").isEqualTo(VALID_PAINTING_ID)
                .jsonPath("$.paintingResponseModel.title").isEqualTo(VALID_PAINTING_TITLE)
                .jsonPath("$.paintingResponseModel.yearCreated").isEqualTo(VALID_PAINTING_YEAR)
                .jsonPath("$.painterResponseModel.painterId").isEqualTo(VALID_PAINTER_ID);
    }

    @Test
    @Sql({"/data-mysql.sql"})
    public void whenSculptureIsDeleted_thenSculptureIsRemovedFromGallery() {
        // arrange
        Painting paintingToDelete = paintingRepository.findByGalleryIdentifier_GalleryIdAndPaintingIdentifier_PaintingId(VALID_GALLERY_ID, VALID_PAINTING_ID);

        // act
        webTestClient.delete()
                .uri(BASE_URI_GALLERIES+"/"+VALID_GALLERY_ID+"/paintings/"+VALID_PAINTING_ID)
                .exchange()
                .expectStatus().isOk();

        // assert
        assertTrue(paintingRepository.findByGalleryIdentifier_GalleryIdAndPaintingIdentifier_PaintingId(VALID_GALLERY_ID, VALID_PAINTING_ID) == null);
    }

    //POST
    //DELETE

}