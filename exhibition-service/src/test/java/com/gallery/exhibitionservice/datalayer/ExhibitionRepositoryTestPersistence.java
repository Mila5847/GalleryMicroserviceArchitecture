package com.gallery.exhibitionservice.datalayer;

import com.gallery.exhibitionservice.domainclientlayer.PaintingResponseModel;
import com.gallery.exhibitionservice.domainclientlayer.SculptureResponseModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@AutoConfigureDataMongo
class ExhibitionRepositoryTestPersistence {

    @Autowired
    private ExhibitionRepository exhibitionRepository;

    private Exhibition exhibition;

    @BeforeEach
    public void setUp() {
        exhibitionRepository.deleteAll();
        ExhibitionIdentifier exhibitionIdentifier1 = new ExhibitionIdentifier();
        exhibitionIdentifier1.setExhibitionId("5ea10d1d-46fb-4374-a39c-27d643aa37e3");
        GalleryIdentifier galleryIdentifier1 = new GalleryIdentifier("ea85d3ba-d708-4ff3-bbbb-dd9c5c77b8e8");
        String galleryName1 = "Art Gallery of Ontario";
        String exhibitionName1 = "Ontario's Exhibition";
        int roomNumber1 = 203;
        int duration1 = 60;
        String startDay1 = "Monday";
        String endDay1 = "Wednesday";

        List<PaintingResponseModel> paintingResponseModels = new ArrayList<>();
        PaintingResponseModel paintingResponseModel1 = new PaintingResponseModel();
        paintingResponseModel1.setPaintingId("3ed9654a-b773-4aa0-ae6b-b22afb636c8e");
        paintingResponseModel1.setTitle("The Starry Night");
        paintingResponseModel1.setYearCreated(1889);
        paintingResponseModel1.setPainterId("f4c80444-5acf-4d57-8902-9f55255e9e55");
        paintingResponseModel1.setGalleryId("ea85d3ba-d708-4ff3-bbbb-dd9c5c77b8e8");

        PaintingResponseModel paintingResponseModel2 = new PaintingResponseModel();
        paintingResponseModel2.setPaintingId("b5fff508-79d2-4fdc-aecf-5a7b504f9fcc");
        paintingResponseModel2.setTitle("Sunflowers");
        paintingResponseModel2.setYearCreated(1888);
        paintingResponseModel2.setPainterId("f4c80444-5acf-4d57-8902-9f55255e9e55");
        paintingResponseModel2.setGalleryId("ea85d3ba-d708-4ff3-bbbb-dd9c5c77b8e8");

        paintingResponseModels.add(paintingResponseModel1);
        paintingResponseModels.add(paintingResponseModel2);

        List<SculptureResponseModel> sculptureResponseModels= new ArrayList<>();

        SculptureResponseModel sculptureResponseModel1 = new SculptureResponseModel();
        sculptureResponseModel1.setSculptureId("acf18748-b00c-4f3a-9d0b-b1b1fdf9c240");
        sculptureResponseModel1.setTitle("Vase");
        sculptureResponseModel1.setMaterial("Wood");
        sculptureResponseModel1.setTexture("Rough");
        sculptureResponseModel1.setGalleryId("ea85d3ba-d708-4ff3-bbbb-dd9c5c77b8e8");

        SculptureResponseModel sculptureResponseModel2 = new SculptureResponseModel();
        sculptureResponseModel2.setSculptureId("04875097-6dab-451c-b08d-b1e48da9ded8");
        sculptureResponseModel2.setTitle("Face");
        sculptureResponseModel2.setMaterial("Stone");
        sculptureResponseModel2.setTexture("Smooth");
        sculptureResponseModel2.setGalleryId("ea85d3ba-d708-4ff3-bbbb-dd9c5c77b8e8");

        sculptureResponseModels.add(sculptureResponseModel1);
        sculptureResponseModels.add(sculptureResponseModel2);

        Exhibition exhibition1 = new Exhibition().builder()
                .exhibitionIdentifier(exhibitionIdentifier1)
                .galleryIdentifier(galleryIdentifier1)
                .galleryName(galleryName1)
                .exhibitionName(exhibitionName1)
                .roomNumber(roomNumber1)
                .duration(duration1)
                .startDay(startDay1)
                .endDay(endDay1)
                .paintings(paintingResponseModels)
                .sculptures(sculptureResponseModels)
                .build();

        exhibition = exhibitionRepository.save(exhibition1);
    }

    @Test
    public void testFindByExhibitionIdentifier_ExhibitionId() {
        exhibitionRepository.save(exhibition);
        Exhibition foundExhibition = exhibitionRepository.findByExhibitionIdentifier_ExhibitionId(exhibition.getExhibitionIdentifier().getExhibitionId());
        assertEquals(exhibition.getExhibitionIdentifier().getExhibitionId(), foundExhibition.getExhibitionIdentifier().getExhibitionId());
    }

    @Test
    public void testExistsByExhibitionIdentifier_ExhibitionId() {
        exhibitionRepository.save(exhibition);
        boolean exists = exhibitionRepository.existsByExhibitionIdentifier_ExhibitionId(exhibition.getExhibitionIdentifier().getExhibitionId());
        assertTrue(exists);
    }

    @Test
    public void existsByExhibitionName() {
        exhibitionRepository.save(exhibition);
        boolean exists = exhibitionRepository.existsByExhibitionName(exhibition.getExhibitionName());
        assertTrue(exists);
    }

    @Test
    public void existsByExhibitionIdentifier_ExhibitionIdentifierAndPaintings_PaintingId() {
        exhibitionRepository.save(exhibition);
        boolean exists = exhibitionRepository.existsByExhibitionIdentifier_ExhibitionIdAndPaintings_PaintingId(exhibition.getExhibitionIdentifier().getExhibitionId(), exhibition.getPaintings().get(0).getPaintingId());
        assertTrue(exists);
    }

    @Test
    public void testExistsByExhibitionIdentifier_ExhibitionIdAndSculptures_SculptureId() {
        exhibitionRepository.save(exhibition);
        boolean exists = exhibitionRepository.existsByExhibitionIdentifier_ExhibitionIdAndSculptures_SculptureId(exhibition.getExhibitionIdentifier().getExhibitionId(), exhibition.getSculptures().get(0).getSculptureId());
        assertTrue(exists);
    }

    @Test
    public void testExistsByPaintings_PaintingId() {
        exhibitionRepository.save(exhibition);
        boolean exists = exhibitionRepository.existsByPaintings_PaintingId(exhibition.getPaintings().get(0).getPaintingId());
        assertTrue(exists);
    }

    @Test
    public void testExistsBySculptures_SculptureId() {
        exhibitionRepository.save(exhibition);
        boolean exists = exhibitionRepository.existsBySculptures_SculptureId(exhibition.getSculptures().get(0).getSculptureId());
        assertTrue(exists);
    }

}