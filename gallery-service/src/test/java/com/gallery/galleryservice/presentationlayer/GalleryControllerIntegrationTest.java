package com.gallery.galleryservice.presentationlayer;

import com.gallery.galleryservice.datalayer.Address;
import com.gallery.galleryservice.datalayer.Gallery;
import com.gallery.galleryservice.datalayer.GalleryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Sql({"/data-mysql.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
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

    //  GET ONE WITH INVALID ID
    @Test
    public void whenGalleryWithInvalidIdExist_thenReturnError(){
        // act
        webTestClient.get()
                .uri(BASE_URI_GALLERIES+VALID_GALLERY_ID+1)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                // assert
                .expectStatus().isNotFound();
    }

    // GET GALLERY BY NAME
    @Test
    public void WhenGetGalleryByNameWithValidName_ThenReturnStatusOk(){
        // arrange
        String galleryName = "Art Gallery of Ontario";

        // act
        webTestClient.get()
                .uri(BASE_URI_GALLERIES+"/gallery?name="+galleryName)
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

    // EXCEPTION: GET GALLERY WITH INVALID NAME
    @Test
    public void WhenGetGalleryByNameWithInvalidName_ThenReturnStatusNotFound() {
        // arrange
        String galleryName = "Art Galleryy of Ontario";

        // act
        webTestClient.get()
                .uri(BASE_URI_GALLERIES + "/gallery?name=" + galleryName)
                .accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isEqualTo(HttpStatusCode.valueOf(404))
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.path").isEqualTo("uri=" + BASE_URI_GALLERIES + "/gallery")
                .jsonPath("$.message").isEqualTo("Gallery with name " + galleryName + " does not exist.");
    }

    // EXCEPTION: MISSING NAME WHEN GET BY NAME
    @Test
    public void WhenGetGalleryByNameWithMissingName_ThenReturnStatusBadRequest() {
        // arrange
        String galleryName = "";

        // act
        webTestClient.get()
                .uri(BASE_URI_GALLERIES + "/gallery?name=" + galleryName)
                .accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isEqualTo(HttpStatusCode.valueOf(400))
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.path").isEqualTo("uri=" + BASE_URI_GALLERIES + "/gallery")
                .jsonPath("$.message").isEqualTo("Name param missing from query param");
    }

    // ADD
    @Test
    public void WhenAddGalleryWithValidGalleryId_ThenReturnNewGallery(){
        String name = "Art Museum of France";
        String openFrom = "Monday 10:00 AM";
        String openUntil = "Sunday 6:00 PM";
        String streetAddress = "1380 St-Charles";
        String city = "Montreal";
        String province = "Quebec";
        String country = "Canada";
        String postalCode = "H9G 1J6";

        GalleryRequestModel galleryRequestModel = createNewGalleryRequestModel(name, openFrom, openUntil, streetAddress, city, province, country, postalCode);
        webTestClient.post()
                .uri(BASE_URI_GALLERIES)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(galleryRequestModel)
                .exchange().expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(GalleryRequestModel.class)
                .value((dto) ->{
                    assertNotNull(dto);
                    assertEquals(dto.getName(), name);
                    assertEquals(dto.getOpenFrom(), openFrom);
                    assertEquals(dto.getOpenUntil(), openUntil);
                    assertEquals(dto.getStreetAddress(), streetAddress);
                    assertEquals(dto.getCity(), city);
                    assertEquals(dto.getProvince(), province);
                    assertEquals(dto.getCountry(), country);
                    assertEquals(dto.getPostalCode(), postalCode);
                });
    }

    // UPDATE
    @Test
    public void WhenUpdateGalleryWithValidGalleryId_ThenReturnUpdatedGallery(){
        String name = "Art Gallery of Ontario";
        String openFrom = "Tuesday 10:00 AM";
        String openUntil = "Saturday 6:00 PM";
        String streetAddress = "1383 St-Charles";
        String city = "Toronto";
        String province = "Ontario";
        String country = "Canada";
        String postalCode = "J9K 1J6";

        GalleryRequestModel galleryRequestModel = createNewGalleryRequestModel(name, openFrom, openUntil, streetAddress, city, province, country, postalCode);

        webTestClient.put()
                .uri(BASE_URI_GALLERIES + "/" + VALID_GALLERY_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(galleryRequestModel)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(GalleryRequestModel.class)
                .value((dto) ->{
                    assertNotNull(dto);
                    assertEquals(dto.getName(), name);
                    assertEquals(dto.getOpenFrom(), openFrom);
                    assertEquals(dto.getOpenUntil(), openUntil);
                    assertEquals(dto.getStreetAddress(), streetAddress);
                    assertEquals(dto.getCity(), city);
                    assertEquals(dto.getProvince(), province);
                    assertEquals(dto.getCountry(), country);
                    assertEquals(dto.getPostalCode(), postalCode);
                });
    }

    // DELETE
    @Test
    public void whenDeletingExistingGallery_thenDeleteGallery() {
        // arrange
        Gallery galleryToDelete = galleryRepository.findByGalleryIdentifier_GalleryId(VALID_GALLERY_ID);

        // act
        webTestClient.delete()
                .uri(BASE_URI_GALLERIES + "/" + VALID_GALLERY_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNoContent();

        // assert
        assertFalse(galleryRepository.existsByGalleryIdentifier_GalleryId(VALID_GALLERY_ID));
    }

    // EXCEPTION: GET GALLERY WITH INVALID ID
    @Test
    public void whenGalleryWithInvalidIdExist_thenReturnNotFound(){
        // act
        webTestClient.get()
                .uri(BASE_URI_GALLERIES+ "/" +VALID_GALLERY_ID+1)
                .accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isEqualTo(HttpStatusCode.valueOf(404))
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.path").isEqualTo("uri=" + BASE_URI_GALLERIES + "/" + VALID_GALLERY_ID+1)
                .jsonPath("$.message").isEqualTo("Gallery with id " + VALID_GALLERY_ID+1 + " not found.");
    }

    // EXCEPTION: ADD GALLERY WITH DUPLICATE ADDRESS
    @Test
    public void whenGalleryWithDuplicateAddressIsAdded_thenReturnUnprocessableEntity() {
        // arrange
        String name = "Art Is Everywhere";
        String openFrom = "Tuesday 10:00 AM";
        String openUntil = "Saturday 6:00 PM";
        String streetAddress = "317 Dundas St W";
        String city = "Toronto";
        String province = "Ontario";
        String country = "Canada";
        String postalCode = "M5T 1G4";
        GalleryRequestModel galleryRequestModel = createNewGalleryRequestModel(name, openFrom, openUntil, streetAddress, city, province, country, postalCode);

        // act
        webTestClient.post()
                .uri(BASE_URI_GALLERIES)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(galleryRequestModel)
                .exchange().expectStatus().isEqualTo(HttpStatusCode.valueOf(422))
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.path").isEqualTo("uri=" + BASE_URI_GALLERIES)
                .jsonPath("$.message").isEqualTo("There is already a gallery with this address");
    }

    // EXCEPTION: UPDATE GALLERY WITH INVALID ID
    @Test
    public void WhenUpdateGalleryWithInvalidGalleryId_ThenReturnNotFound(){
        String name = "Art Gallery of Ontario";
        String openFrom = "Tuesday 10:00 AM";
        String openUntil = "Saturday 6:00 PM";
        String streetAddress = "1383 St-Charles";
        String city = "Toronto";
        String province = "Ontario";
        String country = "Canada";
        String postalCode = "J9K 1J6";

        GalleryRequestModel galleryRequestModel = createNewGalleryRequestModel(name, openFrom, openUntil, streetAddress, city, province, country, postalCode);
        String invalidGalleryId = VALID_GALLERY_ID+1;
        webTestClient.put()
                .uri(BASE_URI_GALLERIES + "/" + invalidGalleryId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(galleryRequestModel)
                .exchange().expectStatus().isEqualTo(HttpStatusCode.valueOf(404))
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.path").isEqualTo("uri=" + BASE_URI_GALLERIES + "/" + VALID_GALLERY_ID+1)
                .jsonPath("$.message").isEqualTo("Gallery with id: " + invalidGalleryId + " does not exist.");
    }

    // DELETE GALLERY WITH INVALID ID
    @Test
    public void whenDeletingGalleryWithInvalidId_thenReturnNotFound() {
        // arrange
        Gallery galleryToDelete = galleryRepository.findByGalleryIdentifier_GalleryId(VALID_GALLERY_ID);
        String invalidGalleryId = VALID_GALLERY_ID+1;
        // act
        webTestClient.delete()
                .uri(BASE_URI_GALLERIES + "/" + invalidGalleryId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isEqualTo(HttpStatusCode.valueOf(404))
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.path").isEqualTo("uri=" + BASE_URI_GALLERIES + "/" + invalidGalleryId)
                .jsonPath("$.message").isEqualTo("Gallery with id: " + invalidGalleryId + " does not exist.");
    }

    // Request
    private GalleryRequestModel createNewGalleryRequestModel(String name, String openFrom, String openUntil, String streetAddress, String city, String province, String country, String postalCode) {
        GalleryRequestModel galleryRequestModel = GalleryRequestModel.builder()
                .name(name)
                .openFrom(openFrom)
                .openUntil(openUntil)
                .streetAddress(streetAddress)
                .city(city)
                .province(province)
                .country(country)
                .postalCode(postalCode)
                .build();
        return galleryRequestModel;
    }

}