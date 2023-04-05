package com.gallery.galleryservice.datalayer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class GalleryPersistenceTest {
    private Gallery preSavedGallery;

    @Autowired
    GalleryRepository galleryRepository;

    @BeforeEach
    public void setup(){
        galleryRepository.deleteAll();
        preSavedGallery = galleryRepository.save(new Gallery("Quebec's Gallery", "Wednesday 12:00 AM", "Sunday 6:00 PM", "797 rue Perrier", "Brossard", "Quebec", "Canada", "J4W 1W6"));
    }

    @Test
    public void findByGalleryIdentifier_GalleryId_ShouldSucceed(){
        //act
        Gallery galleryFound = galleryRepository.findByGalleryIdentifier_GalleryId(preSavedGallery.getGalleryIdentifier().getGalleryId());

        //assert
        assertNotNull(galleryFound);
        assertThat(preSavedGallery, samePropertyValuesAs(galleryFound));

    }

    // find by sculpture id
    @Test
    public void findByInvalidGalleryIdentifier_GalleryId_ShouldReturnNull(){
        //act
        Gallery galleryFound = galleryRepository.findByGalleryIdentifier_GalleryId(preSavedGallery.getGalleryIdentifier().getGalleryId()+1);

        //assert
        assertNull(galleryFound);
    }

    @Test
    public void existsGalleryIdentifier_GalleryId_ShouldReturnTrue(){
        // act
        Boolean galleryFound = galleryRepository.existsByGalleryIdentifier_GalleryId(preSavedGallery.getGalleryIdentifier().getGalleryId());
        // assert
        assertTrue(galleryFound);
    }

    @Test
    public void existsInvalidGalleryIdentifier_GalleryId_ShouldReturnFalse(){
        // act
        Boolean galleryFound = galleryRepository.existsByGalleryIdentifier_GalleryId(preSavedGallery.getGalleryIdentifier().getGalleryId()+1);
        // assert
        assertFalse(galleryFound);
    }

    @Test
    public void saveNewGallery_shouldSucceed(){
        // arrange
        String expectedName = "Gallery of Sofia";
        String expectedOpenFrom = "Wednesday 10:00 AM";
        String expectedOpenUntil = "Friday 9:00 PM";
        String expectedStreetAddress = "808 rue Larmond";
        String expectedCity = "Vancouver";
        String expectedProvince = "British Columbia";
        String expectedCountry = "Canada";
        String expectedPostalCode = "J5E 1W2";
        Gallery newGallery = new Gallery(expectedName, expectedOpenFrom, expectedOpenUntil, expectedStreetAddress, expectedCity, expectedProvince, expectedCountry, expectedPostalCode);

        // act
        Gallery savedGallery = galleryRepository.save(newGallery);

        // assert
        assertNotNull(savedGallery);
        assertNotNull(savedGallery.getId());
        assertNotNull(savedGallery.getGalleryIdentifier().getGalleryId());

        assertEquals(expectedName, savedGallery.getName());
        assertEquals(expectedOpenFrom, savedGallery.getOpenFrom());
        assertEquals(expectedOpenUntil, savedGallery.getOpenUntil());
        assertEquals(expectedStreetAddress, savedGallery.getAddress().getStreetAddress());
        assertEquals(expectedCity, savedGallery.getAddress().getCity());
        assertEquals(expectedProvince, savedGallery.getAddress().getProvince());
        assertEquals(expectedCountry, savedGallery.getAddress().getCountry());
        assertEquals(expectedPostalCode, savedGallery.getAddress().getPostalCode());
    }

    @Test
    public void updateSculpture_shouldSucceed(){
        // arrange
        String expectedName = "Gallery of Istanbul";
        String expectedOpenFrom = "Monday 10:00 AM";
        String expectedOpenUntil = "Friday 9:00 PM";
        String expectedStreetAddress = "803 rue Lavoie";
        String expectedCity = "Toronto";
        String expectedProvince = "Ontario";
        String expectedCountry = "Canada";
        String expectedPostalCode = "J5R 1W3";
        preSavedGallery.setName(expectedName);
        preSavedGallery.setOpenFrom(expectedOpenFrom);
        preSavedGallery.setOpenUntil(expectedOpenUntil);
        Address updatedAddress = new Address(expectedStreetAddress, expectedCity, expectedProvince, expectedCountry,expectedPostalCode);
        preSavedGallery.setAddress(updatedAddress);

        // act
        Gallery updatedGallery = galleryRepository.save(preSavedGallery);

        // assert
        assertNotNull(updatedGallery);
        assertThat(updatedGallery, samePropertyValuesAs(preSavedGallery));
    }

    @Test
    public void deleteSculpture_shouldSucceed(){
        //act
        galleryRepository.delete(preSavedGallery);

        //assert
        assertFalse(galleryRepository.existsByGalleryIdentifier_GalleryId(preSavedGallery.getGalleryIdentifier().getGalleryId()));
    }

}