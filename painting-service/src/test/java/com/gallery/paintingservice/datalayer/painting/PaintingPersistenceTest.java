package com.gallery.paintingservice.datalayer.painting;

import com.gallery.paintingservice.datalayer.painter.Painter;
import com.gallery.paintingservice.datalayer.painter.PainterIdentifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PaintingPersistenceTest {
    private Painting preSavedPainting;

    @Autowired
    PaintingRepository paintingRepository;

    @BeforeEach
    public void setup(){
        paintingRepository.deleteAll();
        preSavedPainting = paintingRepository.save(new Painting("THe Monarchy", 1998));
    }

    @Test
    public void findByPaintingIdentifier_PaintingId_ShouldSucceed(){
        //act
        Painting paintingFound = paintingRepository.findByPaintingIdentifier_PaintingId(preSavedPainting.getPaintingIdentifier().getPaintingId());

        //assert
        assertNotNull(paintingFound);
        assertThat(preSavedPainting, samePropertyValuesAs(paintingFound));
    }

    @Test
    public void findByInvalidPaintingIdentifier_PaintingId_ShouldReturnNull(){
        //act
        Painting paintingFound = paintingRepository.findByPaintingIdentifier_PaintingId(preSavedPainting.getPaintingIdentifier().getPaintingId()+1);
        //assert
        assertNull(paintingFound);
    }

    @Test
    public void existsPaintingIdentifier_PaintingId_ShouldReturnTrue(){
        // act
        Boolean paintingFound = paintingRepository.existsByPaintingIdentifier_PaintingId(preSavedPainting.getPaintingIdentifier().getPaintingId());

        // assert
        assertTrue(paintingFound);
    }

    @Test
    public void existsInvalidSculptureIdentifier_SculptureId_ShouldReturnFalse(){
        // act
        Boolean paintingFound = paintingRepository.existsByPaintingIdentifier_PaintingId(preSavedPainting.getPaintingIdentifier().getPaintingId()+1);

        // assert
        assertFalse(paintingFound);
    }

    @Test
    public void saveNewPainting_shouldSucceed(){
        // arrange
        String expectedTitle = "The Painting of the Paintings";
        int expectedYear = 1906;
        Painting newPainting = new Painting(expectedTitle, expectedYear);

        // act
        Painting savedPainting = paintingRepository.save(newPainting);

        // assert
        assertNotNull(savedPainting);
        assertNotNull(savedPainting.getId());
        assertNotNull(savedPainting.getPaintingIdentifier().getPaintingId());

        assertEquals(expectedTitle, savedPainting.getTitle());
        assertEquals(expectedYear,savedPainting.getYearCreated());

    }

    @Test
    public void updateSculpture_shouldSucceed(){
        // arrange
        String updatedTitle = "The New World";
        int updatedYear = 2000;
        preSavedPainting.setTitle(updatedTitle);
        preSavedPainting.setYearCreated(updatedYear);

        // act
        Painting savedPainting = paintingRepository.save(preSavedPainting);

        // assert
        assertNotNull(savedPainting);
        assertThat(savedPainting, samePropertyValuesAs(preSavedPainting));
    }

    @Test
    public void deletePainting_shouldSucceed(){
        //act
        paintingRepository.delete(preSavedPainting);

        //assert
        assertFalse(paintingRepository.existsByPaintingIdentifier_PaintingId(preSavedPainting.getPaintingIdentifier().getPaintingId()));
    }

}