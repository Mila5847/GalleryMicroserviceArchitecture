package com.gallery.sculptureservice.presentationlayer;

import com.gallery.sculptureservice.datalayer.GalleryIdentifier;
import com.gallery.sculptureservice.datalayer.Sculpture;
import com.gallery.sculptureservice.datalayer.SculptureIdentifier;
import com.gallery.sculptureservice.datalayer.SculptureRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Sql({"/data-mysql.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class SculptureControllerIntegrationTest {
    private final String BASE_URI_GALLERIES = "/api/v1/galleries";
    private final String VALID_GALLERY_ID = "ea85d3ba-d708-4ff3-bbbb-dd9c5c77b8e8";
    private final String VALID_SCULPTURE_ID = "acf18748-b00c-4f3a-9d0b-b1b1fdf9c240";
    private final String VALID_SCULPTURE_TITLE = "Vase";
    private final String VALID_SCULPTURE_MATERIAL = "Wood";
    private final String VALID_SCULPTURE_TEXTURE = "Rough";

    @Autowired
    WebTestClient webTestClient;
    @Autowired
    SculptureRepository sculptureRepository;


    private Sculpture preSavedSculpture;

   /* @AfterEach
    public void tearDown(){
        sculptureRepository.deleteAll();
        preSavedSculpture = sculptureRepository.save(new Sculpture("The Beginning", "Clay", "Smooth"));
    }*/

    // GET ALL
    @Test
    public void whenSculpturesExist_thenReturnAllSculpturesInGallery(){
        // arrange
        Integer expectedNumSculptures = 3;

        // act
        webTestClient.get()
                .uri(BASE_URI_GALLERIES+"/"+VALID_GALLERY_ID+"/sculptures")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.length()").isEqualTo(expectedNumSculptures);
    }

    // GET ONE
    @Test
    public void whenSculptureWithValidIdExist_thenReturnSculpture(){
        // act
        webTestClient.get()
                .uri(BASE_URI_GALLERIES+"/"+VALID_GALLERY_ID+"/sculptures/"+VALID_SCULPTURE_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.sculptureId").isEqualTo(VALID_SCULPTURE_ID)
                .jsonPath("$.title").isEqualTo(VALID_SCULPTURE_TITLE)
                .jsonPath("$.material").isEqualTo(VALID_SCULPTURE_MATERIAL)
                .jsonPath("$.texture").isEqualTo(VALID_SCULPTURE_TEXTURE);
    }

    //  GET ONE WITH INVALID ID
    @Test
    public void whenSculptureWithInvalidIdExist_thenReturnError(){
        // act
        webTestClient.get()
                .uri(BASE_URI_GALLERIES+"/"+VALID_GALLERY_ID+"/sculptures/"+VALID_SCULPTURE_ID+1)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()

                // assert
                .expectStatus().isNotFound();
    }

    // ADD
    @Test
    public void WhenAddSculptureToGalleryWithValidGalleryIAndValidSculptureId_ThenReturnNewSculpture(){
        String title = "The Titanic";
        String material = "Wood";
        String texture ="Rough";

        SculptureRequestModel sculptureRequestModel = createNewSculptureRequestModel(VALID_GALLERY_ID, title, material, texture);

        webTestClient.post()
                .uri(BASE_URI_GALLERIES+"/"+VALID_GALLERY_ID+"/sculptures")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(sculptureRequestModel)
                .exchange().expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(SculptureResponseModel.class)
                .value((dto) ->{
                    assertNotNull(dto);
                    assertEquals(dto.getGalleryId(), VALID_GALLERY_ID);
                    assertEquals(dto.getMaterial(), material);
                    assertEquals(dto.getTexture(), texture);
                });
    }

    // UPDATE
    @Test
    public void WhenUpdateSculptureInGalleryWithValidGalleryIAndValidSculptureId_ThenReturnUpdatedSculpture() {
        String expectedTitle = "Vase";
        String expectedMaterial = "Marble";
        String expectedTexture = "Smooth";

        SculptureRequestModel sculptureRequestModel = createNewSculptureRequestModel(VALID_GALLERY_ID, expectedTitle, expectedMaterial, expectedTexture);

        webTestClient.put()
                .uri(BASE_URI_GALLERIES + "/" + VALID_GALLERY_ID + "/sculptures/" + VALID_SCULPTURE_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(sculptureRequestModel)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.galleryId").isEqualTo(VALID_GALLERY_ID)
                .jsonPath("$.sculptureId").isEqualTo(VALID_SCULPTURE_ID)
                .jsonPath("$.title").isEqualTo(expectedTitle)
                .jsonPath("$.material").isEqualTo(expectedMaterial)
                .jsonPath("$.texture").isEqualTo(expectedTexture);
    }

    // DELETE
    @Test
    public void whenDeletingExistingSculpture_thenDeleteSculptureFromGallery() {
        // arrange
        Sculpture sculptureToDelete = sculptureRepository.findByGalleryIdentifier_GalleryIdAndSculptureIdentifier_SculptureId(VALID_GALLERY_ID, VALID_SCULPTURE_ID);

        // act
        webTestClient.delete()
                .uri(BASE_URI_GALLERIES + "/" + VALID_GALLERY_ID + "/sculptures/" + VALID_SCULPTURE_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNoContent();

        // assert
        assertTrue(sculptureRepository.findByGalleryIdentifier_GalleryIdAndSculptureIdentifier_SculptureId(VALID_GALLERY_ID, VALID_SCULPTURE_ID) == null);
    }

    // EXCEPTION: ADD SCULPTURE WITH DUPLICATE NAME
    @Test
    public void whenSculptureWithDuplicateNameIsAddedToGallery_thenReturnUnprocessableEntity() {
        // arrange
        SculptureRequestModel sculptureRequestModel = createNewSculptureRequestModel(VALID_GALLERY_ID, "Vase", "Marble", "Smooth");

        // act
        webTestClient.post()
                .uri(BASE_URI_GALLERIES+"/"+VALID_GALLERY_ID+"/sculptures")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(sculptureRequestModel)
                .exchange().expectStatus().isEqualTo(HttpStatusCode.valueOf(422))
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.path").isEqualTo("uri=" + BASE_URI_GALLERIES+"/"+VALID_GALLERY_ID+"/sculptures")
                .jsonPath("$.message").isEqualTo("A sculpture with the title " + sculptureRequestModel.getTitle() + " already exists.");
    }

    // EXCEPTION: UPDATE SCULPTURE WITH INVALID ID
    @Test
    public void WhenUpdateSculptureInGalleryWithValidGalleryIButInvalidSculptureId_ThenReturnNotFound() {
        String expectedTitle = "The Titanic Ship";
        String expectedMaterial = "Marble";
        String expectedTexture = "Smooth";

        SculptureRequestModel sculptureRequestModel = createNewSculptureRequestModel(VALID_GALLERY_ID, expectedTitle, expectedMaterial, expectedTexture);
        String invalidSculptureId = VALID_SCULPTURE_ID+1;

        webTestClient.put()
                .uri(BASE_URI_GALLERIES + "/" + VALID_GALLERY_ID + "/sculptures/" + invalidSculptureId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(sculptureRequestModel)
                .exchange().expectStatus().isEqualTo(HttpStatusCode.valueOf(404))
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.path").isEqualTo("uri=" + BASE_URI_GALLERIES+"/"+VALID_GALLERY_ID+"/sculptures/" + invalidSculptureId)
                .jsonPath("$.message").isEqualTo("Sculpture with id: " +  invalidSculptureId + " does not exist.");
    }

    // EXCEPTION: DELETE SCULPTURE WITH INVALID ID
    @Test
    public void WhenDeleteSculptureInGalleryWithValidGalleryIButInvalidSculptureId_ThenReturnNotFound() {
        String expectedTitle = "The Titanic Ship";
        String expectedMaterial = "Marble";
        String expectedTexture = "Smooth";

        SculptureRequestModel sculptureRequestModel = createNewSculptureRequestModel(VALID_GALLERY_ID, expectedTitle, expectedMaterial, expectedTexture);
        String invalidSculptureId = VALID_SCULPTURE_ID+1;

        webTestClient.delete()
                .uri(BASE_URI_GALLERIES + "/" + VALID_GALLERY_ID + "/sculptures/" + invalidSculptureId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isEqualTo(HttpStatusCode.valueOf(404))
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.path").isEqualTo("uri=" + BASE_URI_GALLERIES + "/" + VALID_GALLERY_ID + "/sculptures/" + invalidSculptureId)
                .jsonPath("$.message").isEqualTo("Sculpture with id: " + invalidSculptureId + " does not exist.");
    }

    // Request
    private SculptureRequestModel createNewSculptureRequestModel(String galleryId, String title, String material, String texture) {
        SculptureRequestModel sculptureRequestModel = SculptureRequestModel.builder()
                .galleryId(galleryId)
                .title(title)
                .material(material)
                .texture(texture)
                .build();
        return sculptureRequestModel;
    }

}