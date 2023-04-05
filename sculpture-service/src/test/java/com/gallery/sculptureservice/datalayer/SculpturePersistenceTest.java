package com.gallery.sculptureservice.datalayer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class SculpturePersistenceTest {
    private Sculpture preSavedSculpture;

    @Autowired
    SculptureRepository sculptureRepository;

    @BeforeEach
    public void setup(){
        sculptureRepository.deleteAll();
        preSavedSculpture = sculptureRepository.save(new Sculpture("The Beginning", "Clay", "Smooth"));
    }

    // FIND BY ID
    @Test
    public void findBySculptureIdentifier_SculptureId_ShouldSucceed(){
        //act
        Sculpture sculptureFound = sculptureRepository.findBySculptureIdentifier_SculptureId(preSavedSculpture.getSculptureIdentifier().getSculptureId());

        //assert
        assertNotNull(sculptureFound);
        assertThat(preSavedSculpture, samePropertyValuesAs(sculptureFound));

    }

    // INVALID ID
    @Test
    public void findByInvalidSculptureIdentifier_SculptureId_ShouldReturnNull(){
        //act
        Sculpture sculptureFound = sculptureRepository.findBySculptureIdentifier_SculptureId(preSavedSculpture.getSculptureIdentifier().getSculptureId()+1);
        //assert
        assertNull(sculptureFound);
    }

    // EXISTS
    @Test
    public void existsSculptureIdentifier_SculptureId_ShouldReturnTrue(){
        // act
        Boolean sculptureFound = sculptureRepository.existsBySculptureIdentifier_SculptureId(SculpturePersistenceTest.this.preSavedSculpture.getSculptureIdentifier().getSculptureId());

        // assert
        assertTrue(sculptureFound);
    }

    @Test
    public void existsInvalidSculptureIdentifier_SculptureId_ShouldReturnFalse(){
        // act
        Boolean sculptureFound = sculptureRepository.existsBySculptureIdentifier_SculptureId(SculpturePersistenceTest.this.preSavedSculpture.getSculptureIdentifier().getSculptureId()+1);

        // assert
        assertFalse(sculptureFound);
    }

    //POST
    @Test
    public void saveNewSculpture_shouldSucceed(){
        // arrange
        String expectedTitle = "The Beginning";
        String expectedMaterial = "Clay";
        String expectedTexture = "Smooth";
        Sculpture newSculpture = new Sculpture(expectedTitle, expectedMaterial, expectedTexture);

        // act
        Sculpture savedSculpture = sculptureRepository.save(newSculpture);

        // assert
        assertNotNull(savedSculpture);
        assertNotNull(savedSculpture.getId());
        assertNotNull(savedSculpture.getSculptureIdentifier().getSculptureId());

        assertEquals(expectedTitle, savedSculpture.getTitle());
        assertEquals(expectedMaterial, savedSculpture.getMaterial());
        assertEquals(expectedTexture, savedSculpture.getTexture());

    }

    // UPDATE
    @Test
    public void updateSculpture_shouldSucceed(){
        // arrange
        String updatedTitle = "The New Beginning";
        String updatedMaterial = "Wood";
        String updatedTexture = "Rough";
        SculpturePersistenceTest.this.preSavedSculpture.setTitle(updatedTitle);
        SculpturePersistenceTest.this.preSavedSculpture.setMaterial(updatedMaterial);
        SculpturePersistenceTest.this.preSavedSculpture.setTexture(updatedTexture);

        // act
        Sculpture updatedSculpture = sculptureRepository.save(SculpturePersistenceTest.this.preSavedSculpture);

        // assert
        assertNotNull(updatedSculpture);
        assertThat(updatedSculpture, samePropertyValuesAs(SculpturePersistenceTest.this.preSavedSculpture));
    }

    // DELETE ONE
    @Test
    public void deleteSculpture_shouldSucceed(){
        //act
        sculptureRepository.delete(SculpturePersistenceTest.this.preSavedSculpture);

        //assert
        assertFalse(sculptureRepository.existsBySculptureIdentifier_SculptureId(SculpturePersistenceTest.this.preSavedSculpture.getSculptureIdentifier().getSculptureId()));
    }
}