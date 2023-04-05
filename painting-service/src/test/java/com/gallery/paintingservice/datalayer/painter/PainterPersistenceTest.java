package com.gallery.paintingservice.datalayer.painter;

import com.gallery.paintingservice.datalayer.painting.Painting;
import com.gallery.paintingservice.datalayer.painting.PaintingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PainterPersistenceTest {
    private Painting preSavedPainting;
    private Painter preSavedPainter;

    @Autowired
    PaintingRepository paintingRepository;

    @Autowired
    PainterRepository painterRepository;

    @BeforeEach
    public void setup(){
        paintingRepository.deleteAll();
        preSavedPainting = paintingRepository.save(new Painting("THe Monarchy", 1998));
        preSavedPainter = painterRepository.save(new Painter("Anastasia Rano", "France", "18/12/1050", "19/03/2025"));
    }

    @Test
    public void saveNewPaintingWithPainter_shouldSucceed() {
        // arrange
        // Painting
        String expectedTitle = "The Painting of the Paintings";
        int expectedYear = 1906;
        preSavedPainting.setTitle(expectedTitle);
        preSavedPainting.setYearCreated(expectedYear);
        // Painter
        Painter painter = new Painter();
        PainterIdentifier painterIdentifier = new PainterIdentifier();
        String expectedName = "Charles Larose";
        String expectedOrigin = "France";
        String expectedBirthDate = "10/03/1940";
        String expectedDeathDate = "03/09/2022";
        painter.setName(expectedName);
        painter.setOrigin(expectedOrigin);
        painter.setBirthDate(expectedBirthDate);
        painter.setDeathDate(expectedDeathDate);
        painter.setPainterIdentifier(painterIdentifier);
        preSavedPainting.setPainterIdentifier(painterIdentifier);

        // act
        Painting savedPainting = paintingRepository.save(preSavedPainting);
        Painter savedPainter = painterRepository.save(painter);
        // assert
        assertNotNull(savedPainting);
        assertNotNull(savedPainting.getId());
        assertNotNull(savedPainting.getPaintingIdentifier().getPaintingId());
        assertEquals(expectedTitle, savedPainting.getTitle());
        assertEquals(expectedYear, savedPainting.getYearCreated());
        assertNotNull(savedPainting.getPainterIdentifier());
        assertEquals(painterIdentifier, savedPainting.getPainterIdentifier());

        assertNotNull(savedPainter);
        assertNotNull(savedPainter.getId());
        assertNotNull(savedPainter.getPainterIdentifier().getPainterId());
        assertEquals(expectedName, savedPainter.getName());
        assertEquals(expectedOrigin, savedPainter.getOrigin());
        assertEquals(expectedBirthDate, savedPainter.getBirthDate());
        assertEquals(expectedDeathDate, savedPainter.getDeathDate());
    }

    @Test
    public void updatePainterOfPainting_shouldSucceed() {
        // arrange
        String updatedName = "Charlotte Lavoie";
        String updatedOrigin = "Belgium";
        String updatedBirthDate = "11/02/1945";
        String updatedDeathDate = "20/10/2022";
        preSavedPainter.setName(updatedName);
        preSavedPainter.setOrigin(updatedOrigin);
        preSavedPainter.setBirthDate(updatedBirthDate);
        preSavedPainter.setDeathDate(updatedDeathDate);

        // act
        Painter savedPainter = painterRepository.save(preSavedPainter);

        // assert
        assertNotNull(savedPainter);
        assertThat(savedPainter, samePropertyValuesAs(preSavedPainter));
    }

    @Test
    public void deletePainterShouldNotDeletePainting_ShouldSucceed() {
        // arrange
        Painting painting = new Painting("Sunset", 2005);
        Painter painter = new Painter("John Doe", "USA", "01/01/1970", "01/01/2022");
        PainterIdentifier painterIdentifier = new PainterIdentifier();
        painter.setPainterIdentifier(painterIdentifier);
        painting.setPainterIdentifier(painter.getPainterIdentifier());
        painterRepository.save(painter);
        paintingRepository.save(painting);
        // act
        painterRepository.delete(painter);

        // assert
        assertFalse(painterRepository.existsById(painter.getId()));
        assertTrue(paintingRepository.existsById(painting.getId()));
    }

   /* @Test
    public void deletePaintingShouldDeletePainter_ShouldSucceed() {
        // arrange
        Painting painting = new Painting("Sunset", 2005);
        Painter painter = new Painter("John Doe", "USA", "01/01/1970", "01/01/2022");
        PainterIdentifier painterIdentifier = new PainterIdentifier();
        painter.setPainterIdentifier(painterIdentifier);
        painting.setPainterIdentifier(painter.getPainterIdentifier());
        painterRepository.save(painter);
        paintingRepository.save(painting);

        // act
        paintingRepository.delete(painting);

        // assert
        assertFalse(paintingRepository.existsById(painting.getId()));
        assertFalse(painterRepository.existsById(painter.getId()));
    }*/


}