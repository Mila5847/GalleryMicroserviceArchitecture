package com.gallery.paintingservice.presentationlayer;

import com.gallery.paintingservice.datalayer.painter.Painter;
import com.gallery.paintingservice.datalayer.painter.PainterRepository;
import com.gallery.paintingservice.datalayer.painting.Painting;
import com.gallery.paintingservice.datalayer.painting.PaintingRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Sql({"/data-mysql.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class PaintingControllerIntegrationTest {
    private final String BASE_URI_GALLERIES = "/api/v1/galleries";
    private final String VALID_GALLERY_ID = "ea85d3ba-d708-4ff3-bbbb-dd9c5c77b8e8";
    private final String VALID_PAINTING_ID = "3ed9654a-b773-4aa0-ae6b-b22afb636c8e";
    private final String VALID_PAINTING_TITLE = "The Starry Night";
    private final Integer VALID_PAINTING_YEAR = 1889;
    private final String VALID_PAINTING_WITHOUT_PAINTER = "f32e5cb2-1890-4572-812b-fdc671537b45";
    private final String VALID_PAINTER_ID = "f4c80444-5acf-4d57-8902-9f55255e9e55";

    @Autowired
    WebTestClient webTestClient;
    @Autowired
    PaintingRepository paintingRepository;
    @Autowired
    PainterRepository painterRepository;

    /*@AfterEach
    public void tearDown(){
        paintingRepository.deleteAll();
        painterRepository.deleteAll();
    }*/

    // GET ALL
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

    // GET ONE
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
                .jsonPath("$.paintingResponseModel.painterId").isEqualTo(VALID_PAINTER_ID);
    }

    // GET ONE WITH INVALID ID
    @Test
    public void whenPaintingWithInvalidIdExist_thenReturnError(){
        // act
        webTestClient.get()
                .uri(BASE_URI_GALLERIES+"/"+VALID_GALLERY_ID+"/paintings/"+VALID_PAINTING_ID+1)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()

                // assert
                .expectStatus().isNotFound();
    }

    // EXCEPTION: GET BY INVALID ID
    @Test
    public void whenPaintingWithInvalidIdExist_thenReturnNotFound(){
        String invalidPaintingId = VALID_PAINTING_ID+1;

        webTestClient.get()
                .uri(BASE_URI_GALLERIES + "/" + VALID_GALLERY_ID + "/paintings/" + invalidPaintingId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatusCode.valueOf(404))
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.path").isEqualTo("uri=" + BASE_URI_GALLERIES + "/" + VALID_GALLERY_ID + "/paintings/" + invalidPaintingId)
                .jsonPath("$.message").isEqualTo("Painting with id: " + invalidPaintingId + " does not exist.");
    }

    // GET BY PAINTER ID
    @Test
    public void whenPaintingWithValidPainterIdInGalleryExists_thenReturnPaintingsOfPainter(){
        // assert
        int expectedPaintingsForPainter = 2;
        // act
        webTestClient.get()
                .uri(BASE_URI_GALLERIES+"/"+VALID_GALLERY_ID+"/painters/" + VALID_PAINTER_ID + "/paintings")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(PaintingsOfPainterResponseModel.class)
                .value((dto) -> {
                    assertNotNull(dto);
                    assertEquals(dto.getPaintings().size(), expectedPaintingsForPainter);
        });

    }

    // GET PAINTINGS BY INVALID PAINTER ID
    @Test
    public void whenPainterWithInvalidIdExist_thenReturnNotFound(){
        String invalidPainterId = VALID_PAINTER_ID+1;

        webTestClient.get()
                .uri(BASE_URI_GALLERIES+"/"+VALID_GALLERY_ID+"/painters/" + invalidPainterId + "/paintings")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatusCode.valueOf(404))
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.path").isEqualTo("uri=" + BASE_URI_GALLERIES+"/"+VALID_GALLERY_ID+"/painters/" + invalidPainterId + "/paintings")
                .jsonPath("$.message").isEqualTo("Painter with id " + invalidPainterId + " does not exist.");
    }

    // ADD
    @Test
    public void WhenAddPaintingToGalleryWithValidGalleryIAndValidPaintingId_ThenReturnNewPainting() {
        String title = "World's News";
        int yearCreated = 1999;

        PaintingRequestModel paintingRequestModel = createNewPaintingRequestModel(title, yearCreated);

        webTestClient.post()
                .uri(BASE_URI_GALLERIES + "/" + VALID_GALLERY_ID + "/paintings")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(paintingRequestModel)
                .exchange().expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(PaintingPainterResponseModel.class)
                .value((dto) -> {
                    assertNotNull(dto);
                    assertEquals(dto.paintingResponseModel.getTitle(), title);
                    assertEquals(dto.paintingResponseModel.getYearCreated(), yearCreated);
                    assertEquals(dto.paintingResponseModel.getGalleryId(), VALID_GALLERY_ID);
                });
    }

    // ADD EXCEPTION TITLE MUST CONTAIN AT LEAST 1 CHARACTER
    @Test
    public void WhenAddPaintingWithInvalidTitleToGallery_ThenReturnUnprocessableEntity() {
        String title = "";
        int yearCreated = 1999;

        PaintingRequestModel paintingRequestModel = createNewPaintingRequestModel(title, yearCreated);

        webTestClient.post()
                .uri(BASE_URI_GALLERIES + "/" + VALID_GALLERY_ID + "/paintings")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(paintingRequestModel)
                .exchange().expectStatus().isEqualTo(HttpStatusCode.valueOf(422))
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.path").isEqualTo("uri=" + BASE_URI_GALLERIES + "/" + VALID_GALLERY_ID + "/paintings")
                .jsonPath("$.message").isEqualTo("The tile of the painting should be at least 1 character.");
    }

    // ADD EXCEPTION TITLE MUST CONTAIN AT LEAST 1 CHARACTER
    @Test
    public void WhenAddPaintingWithAlreadyExistingTitleToGallery_ThenReturnUnprocessableEntity() {
        String title = "The Starry Night";
        int yearCreated = 1999;

        PaintingRequestModel paintingRequestModel = createNewPaintingRequestModel(title, yearCreated);

        webTestClient.post()
                .uri(BASE_URI_GALLERIES + "/" + VALID_GALLERY_ID + "/paintings")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(paintingRequestModel)
                .exchange().expectStatus().isEqualTo(HttpStatusCode.valueOf(422))
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.path").isEqualTo("uri=" + BASE_URI_GALLERIES + "/" + VALID_GALLERY_ID + "/paintings")
                .jsonPath("$.message").isEqualTo("Painting with title: " + title + " already exists.");
    }


    // UPDATE
    @Test
    public void WhenUpdatePaintingInGalleryWithValidGalleryIAndValidPaintingId_ThenReturnUpdatedPainting() {
        String title = "The Middle of the Night";
        int year = 1999;

        PaintingRequestModel paintingRequestModel = createNewPaintingRequestModel(title, year);
        webTestClient.put()
                .uri(BASE_URI_GALLERIES + "/" + VALID_GALLERY_ID + "/paintings/" + VALID_PAINTING_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(paintingRequestModel)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.paintingResponseModel.title").isEqualTo(title)
                .jsonPath("$.paintingResponseModel.yearCreated").isEqualTo(year)
                .jsonPath("$.paintingResponseModel.galleryId").isEqualTo(VALID_GALLERY_ID);
    }

    // UPDATE WITH INVALID ID
    @Test
    public void WhenUpdatePaintingInGalleryWithValidGalleryIButInvalidPaintingId_ThenReturnNotFound() {
        // arrange
        String invalidPaintingId = VALID_PAINTING_ID+1;
        String title = "The Middle of the Night";
        int year = 1999;
        PaintingRequestModel paintingRequestModel = createNewPaintingRequestModel(title, year);

        // act
        webTestClient.put()
                .uri(BASE_URI_GALLERIES + "/" + VALID_GALLERY_ID + "/paintings/" + invalidPaintingId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(paintingRequestModel)
                .accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isEqualTo(HttpStatusCode.valueOf(404))
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.path").isEqualTo("uri=" + BASE_URI_GALLERIES + "/" + VALID_GALLERY_ID + "/paintings/" + invalidPaintingId)
                .jsonPath("$.message").isEqualTo("Painting with id: " +  invalidPaintingId + " does not exist.");
    }

    // DELETE PAINTING
    @Test
    public void whenPaintingIsDeleted_thenPaintingIsRemovedFromGallery() {
        // act
        webTestClient.delete()
                .uri(BASE_URI_GALLERIES+"/"+VALID_GALLERY_ID+"/paintings/"+VALID_PAINTING_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNoContent();

        // assert
        assertNull(paintingRepository.findByGalleryIdentifier_GalleryIdAndPaintingIdentifier_PaintingId(VALID_GALLERY_ID, VALID_PAINTING_ID));
    }

    // WHEN DELETE PAINTING, IF PAINTER DOES NOT HAVE ANY MORE PAINTINGS, DELETE PAINTER
    @Test
    public void whenPaintingIsDeleted_thenPaintingIsRemovedFromGalleryAndPainterIsDeleted() {
        // arrange
        String paintingIdWithPainterWhoOnlyHasThisPainting = "6ae9eaf7-5ec4-45da-a85d-45a317e711a2";
        String idOfPainterWithOnlyOnePainting = "0e1482bb-67a8-4620-842b-3f7bfb7ee175";
        // act
        webTestClient.delete()
                .uri(BASE_URI_GALLERIES+"/"+VALID_GALLERY_ID+"/paintings/"+paintingIdWithPainterWhoOnlyHasThisPainting)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNoContent();

        // assert
        assertNull(paintingRepository.findByGalleryIdentifier_GalleryIdAndPaintingIdentifier_PaintingId(VALID_GALLERY_ID, paintingIdWithPainterWhoOnlyHasThisPainting));
        assertFalse(painterRepository.existsByPainterIdentifier_PainterId(idOfPainterWithOnlyOnePainting));
    }


    // DELETE PAINTING WITH INVALID ID
    @Test
    public void whenPaintingWithInvalidIdIsDeleted_thenReturnNotFound() {
        // arrange
        String invalidPaintingId = VALID_PAINTING_ID+1;
        Painting paintingToDelete = paintingRepository.findByGalleryIdentifier_GalleryIdAndPaintingIdentifier_PaintingId(VALID_GALLERY_ID, VALID_PAINTING_ID);

        // act
        webTestClient.delete()
                .uri(BASE_URI_GALLERIES+"/"+VALID_GALLERY_ID+"/paintings/"+invalidPaintingId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isEqualTo(HttpStatusCode.valueOf(404))
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.path").isEqualTo("uri=" + BASE_URI_GALLERIES+"/"+VALID_GALLERY_ID+"/paintings/"+invalidPaintingId)
                .jsonPath("$.message").isEqualTo("Painting with id: " + invalidPaintingId + " does not exist.");
    }

    // ADD PAINTER TO PAINTING IN GALLERY
    @Test
    public void WhenAddPainterToExistingPaintingInGallery_ThenReturnPaintingPainter() {
        String name = "John Smith";
        String origin = "Canada";
        String birthDate = "19/08/1990";
        String deathDate = "Unknown";

        PainterRequestModel painterRequestModel = createNewPainterRequestModel(name, origin, birthDate, deathDate);

        webTestClient.post()
                .uri(BASE_URI_GALLERIES + "/" + VALID_GALLERY_ID + "/paintings/" + VALID_PAINTING_WITHOUT_PAINTER + "/painters")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(painterRequestModel)
                .exchange().expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(PaintingPainterResponseModel.class)
                .value((dto) -> {
                    assertNotNull(dto);
                    assertEquals(dto.painterResponseModel.name, name);
                    assertEquals(dto.painterResponseModel.origin, origin);
                    assertEquals(dto.painterResponseModel.birthDate, birthDate);
                    assertEquals(dto.painterResponseModel.deathDate, deathDate);
                });
    }

    // EXCEPTION: ADD PAINTER TO NON-EXISTENT PAINTING
    @Test
    public void WhenAddPainterToNonExistentPaintingInGallery_ThenReturnPaintingPainter() {
        String invalidPaintingId = VALID_PAINTING_WITHOUT_PAINTER+1;
        String name = "John Smith";
        String origin = "Canada";
        String birthDate = "19/08/1990";
        String deathDate = "Unknown";

        PainterRequestModel painterRequestModel = createNewPainterRequestModel(name, origin, birthDate, deathDate);

        webTestClient.post()
                .uri(BASE_URI_GALLERIES + "/" + VALID_GALLERY_ID + "/paintings/" + invalidPaintingId + "/painters")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(painterRequestModel)
                .exchange().expectStatus().isEqualTo(HttpStatusCode.valueOf(404))
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.path").isEqualTo("uri=" + BASE_URI_GALLERIES + "/" + VALID_GALLERY_ID + "/paintings/" + invalidPaintingId + "/painters")
                .jsonPath("$.message").isEqualTo("Painting with id: " + invalidPaintingId + " does not exist.");
    }

    // UPDATE PAINTER OF PAINTING
    @Test
    public void WhenUpdatePainterToExistingPaintingInGallery_ThenReturnPaintingPainter() {
        String name = "John Smith";
        String origin = "Canada";
        String birthDate = "19/08/1990";
        String deathDate = "Unknown";

        PainterRequestModel painterRequestModel = createNewPainterRequestModel(name, origin, birthDate, deathDate);

        webTestClient.put()
                .uri(BASE_URI_GALLERIES + "/" + VALID_GALLERY_ID + "/paintings/" + VALID_PAINTING_WITHOUT_PAINTER + "/painters/" + VALID_PAINTER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(painterRequestModel)
                .exchange().expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(PaintingPainterResponseModel.class)
                .value((dto) -> {
                    assertNotNull(dto);
                    assertEquals(dto.painterResponseModel.name, name);
                    assertEquals(dto.painterResponseModel.origin, origin);
                    assertEquals(dto.painterResponseModel.birthDate, birthDate);
                    assertEquals(dto.painterResponseModel.deathDate, deathDate);
                });
    }

    // EXCEPTION UPDATE PAINTER OF PAINTING WITH INVALID PAINTING ID
    @Test
    public void WhenUpdatePainterOfNonExistentPaintingInGallery_ThenReturnNotFound() {
        String invalidPaintingId = VALID_PAINTING_WITHOUT_PAINTER+1;
        String name = "John Smith";
        String origin = "Canada";
        String birthDate = "19/08/1990";
        String deathDate = "Unknown";

        PainterRequestModel painterRequestModel = createNewPainterRequestModel(name, origin, birthDate, deathDate);

        webTestClient.put()
                .uri(BASE_URI_GALLERIES + "/" + VALID_GALLERY_ID + "/paintings/" + invalidPaintingId + "/painters/" + VALID_PAINTER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(painterRequestModel)
                .exchange().expectStatus().isEqualTo(HttpStatusCode.valueOf(404))
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.path").isEqualTo("uri=" + BASE_URI_GALLERIES + "/" + VALID_GALLERY_ID + "/paintings/" + invalidPaintingId + "/painters/" + VALID_PAINTER_ID)
                .jsonPath("$.message").isEqualTo("Painting with id: " + invalidPaintingId + " does not exist.");
    }

    // EXCEPTION UPDATE PAINTER OF PAINTING WITH INVALID PAINTer ID
    @Test
    public void WhenUpdatePainterWithInvalidPainterIdInGallery_ThenReturnNotFound() {
        String invalidPainterId = VALID_PAINTER_ID+1;
        String name = "John Smith";
        String origin = "Canada";
        String birthDate = "19/08/1990";
        String deathDate = "Unknown";

        PainterRequestModel painterRequestModel = createNewPainterRequestModel(name, origin, birthDate, deathDate);

        webTestClient.put()
                .uri(BASE_URI_GALLERIES + "/" + VALID_GALLERY_ID + "/paintings/" + VALID_PAINTING_ID + "/painters/" + invalidPainterId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(painterRequestModel)
                .exchange().expectStatus().isEqualTo(HttpStatusCode.valueOf(404))
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.path").isEqualTo("uri=" + BASE_URI_GALLERIES + "/" + VALID_GALLERY_ID + "/paintings/" + VALID_PAINTING_ID + "/painters/" + invalidPainterId)
                .jsonPath("$.message").isEqualTo("Painter with id " + invalidPainterId + " does not exist.");
    }

    // REMOVE PAINTER OF PAINTING
    @Test
    public void whenDeletingExistingPainterOfPainting_thenDeletePainterFromPainting() {
        // arrange
        Painter painter = painterRepository.findByPainterIdentifier_PainterId(VALID_PAINTER_ID);
        Painting painting = paintingRepository.findByGalleryIdentifier_GalleryIdAndPaintingIdentifier_PaintingId(VALID_GALLERY_ID,  VALID_PAINTING_ID);
        // act
        webTestClient.delete()
                .uri(BASE_URI_GALLERIES + "/" + VALID_GALLERY_ID + "/paintings/" + VALID_PAINTING_ID + "/painters/" + VALID_PAINTER_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNoContent();
        // assert
        Painting updatedPainting = paintingRepository.findByGalleryIdentifier_GalleryIdAndPaintingIdentifier_PaintingId(VALID_GALLERY_ID,  VALID_PAINTING_ID);
        assertEquals(updatedPainting.getPainterIdentifier(), null);
    }

    // EXCEPTION: REMOVE PAINTER WITH INVALID ID OF PAINTING
    @Test
    public void whenPainterWithInvalidIdIsDeleted_thenReturnNotFound() {
        // arrange
        String invalidPainterId = VALID_PAINTER_ID+1;

        // act
        webTestClient.delete()
                .uri(BASE_URI_GALLERIES+"/"+VALID_GALLERY_ID+"/paintings/"+ VALID_PAINTING_ID + "/painters/" + invalidPainterId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isEqualTo(HttpStatusCode.valueOf(404))
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.path").isEqualTo("uri=" + BASE_URI_GALLERIES+"/"+VALID_GALLERY_ID+"/paintings/"+ VALID_PAINTING_ID + "/painters/" + invalidPainterId)
                .jsonPath("$.message").isEqualTo("Painter with id " + invalidPainterId + " does not exist.");
    }

    // EXCEPTION: REMOVE PAINTER OF PAINTING WITH INVALID PAINTING ID
    @Test
    public void whenPainterOfNonExistentPaintingIsDeleted_thenReturnNotFound() {
        // arrange
        String invalidPaintingId = VALID_PAINTING_ID+1;

        // act
        webTestClient.delete()
                .uri(BASE_URI_GALLERIES+"/"+VALID_GALLERY_ID+"/paintings/"+ invalidPaintingId + "/painters/" + VALID_PAINTER_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isEqualTo(HttpStatusCode.valueOf(404))
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.path").isEqualTo("uri=" + BASE_URI_GALLERIES+"/"+VALID_GALLERY_ID+"/paintings/"+ invalidPaintingId + "/painters/" + VALID_PAINTER_ID)
                .jsonPath("$.message").isEqualTo("Painting with id: " + invalidPaintingId + " does not exist.");
    }


    // Request painting
    private PaintingRequestModel createNewPaintingRequestModel(String title, int yearCreated) {
        PaintingRequestModel paintingRequestModel = PaintingRequestModel.builder()
                .title(title)
                .yearCreated(yearCreated)
                .build();
        return paintingRequestModel;
    }

    //Request painter
    private PainterRequestModel createNewPainterRequestModel(String name, String origin, String birthDate, String deathDate) {
        PainterRequestModel painterRequestModel = PainterRequestModel.builder()
                .name(name)
                .origin(origin)
                .birthDate(birthDate)
                .deathDate(deathDate)
                .build();
        return painterRequestModel;
    }
}