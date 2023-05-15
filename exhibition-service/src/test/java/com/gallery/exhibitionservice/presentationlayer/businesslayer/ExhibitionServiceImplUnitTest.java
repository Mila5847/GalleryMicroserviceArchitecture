package com.gallery.exhibitionservice.presentationlayer.businesslayer;

import com.gallery.exhibitionservice.businesslayer.ExhibitionService;
import com.gallery.exhibitionservice.datalayer.Exhibition;
import com.gallery.exhibitionservice.datalayer.ExhibitionIdentifier;
import com.gallery.exhibitionservice.datalayer.ExhibitionRepository;
import com.gallery.exhibitionservice.datalayer.GalleryIdentifier;
import com.gallery.exhibitionservice.datamapperlayer.ExhibitionResponseMapper;
import com.gallery.exhibitionservice.domainclientlayer.*;
import com.gallery.exhibitionservice.presentationlayer.ExhibitionRequestModel;
import com.gallery.exhibitionservice.presentationlayer.ExhibitionResponseModel;
import com.gallery.exhibitionservice.utils.exceptions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mongodb.assertions.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestPropertySource(properties = "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration")
public class ExhibitionServiceImplUnitTest {

    @Autowired
    ExhibitionService exhibitionService;

    @MockBean
    ExhibitionRepository exhibitionRepository;

    @MockBean
    GalleryServiceClient galleryServiceClient;

    @MockBean
    PaintingServiceClient paintingServiceClient;

    @MockBean
    SculptureServiceClient sculptureServiceClient;

    @SpyBean
    ExhibitionResponseMapper exhibitionResponseMapper;

    // Get All Exhibitions
    @Test
    void whenGetAllExhibitions_ThenReturnListOfExhibitions(){
        Exhibition exhibition = buildExhibition();
        Exhibition savedExhibition = buildExhibition();
        savedExhibition.setId("0001");

        when(exhibitionRepository.save(exhibition)).thenReturn(savedExhibition);
        when(exhibitionRepository.save(any(Exhibition.class))).thenReturn(savedExhibition);

        when(exhibitionRepository.findAll()).thenReturn(Arrays.asList(savedExhibition, savedExhibition, savedExhibition));
        assertEquals(3, exhibitionService.getAllExhibitions().size());
    }

    // Get Exhibition
    @Test
    void whenValidExhibitionId_ThenGetExhibitionWithThisId(){
        Exhibition exhibition = buildExhibition();
        Exhibition savedExhibition = buildExhibition();
        savedExhibition.setId("0001");
        when(exhibitionRepository.save(exhibition)).thenReturn(savedExhibition);
        when(exhibitionRepository.save(any(Exhibition.class))).thenReturn(savedExhibition);
        when(exhibitionRepository.findByExhibitionIdentifier_ExhibitionId(any())).thenReturn(savedExhibition);

        assertNotNull(exhibitionService.getExhibitionById("0001"));
    }

    // Get Exhibition Exception Invalid Exhibition Id
    @Test
    void whenInvalidExhibitionId_ThenThrowException(){
        Exhibition exhibition = buildExhibition();
        Exhibition savedExhibition = buildExhibition();
        savedExhibition.setId("0001");
        when(exhibitionRepository.save(exhibition)).thenReturn(savedExhibition);
        when(exhibitionRepository.save(any(Exhibition.class))).thenReturn(savedExhibition);
        when(exhibitionRepository.findByExhibitionIdentifier_ExhibitionId(any())).thenReturn(null);

        String expectedMessage = "Unknown exhibition id " + savedExhibition.getId();
        Throwable exception = assertThrows(ExistingExhibitionNotFoundException.class, () -> exhibitionService.getExhibitionById("0001"));
        assertEquals(expectedMessage, exception.getMessage());
    }

    // Create Exhibition
    @Test
    void whenValidGalleryId_PaintingId_SculptureId_ThenCreateExhibition_ShouldSucceed(){
        List<PaintingResponseModel> paintingResponseModels = new ArrayList<>();
        PaintingResponseModel paintingResponseModel1 = new PaintingResponseModel();
        paintingResponseModel1.setPaintingId("3ed9654a-b773-4aa0-ae6b-b22afb636c8e");
        paintingResponseModel1.setTitle("The Starry Night");
        paintingResponseModel1.setYearCreated(1889);
        paintingResponseModel1.setPainterId("f4c80444-5acf-4d57-8902-9f55255e9e55");
        paintingResponseModel1.setGalleryId("ea85d3ba-d708-4ff3-bbbb-dd9c5c77b8e8");

        PainterResponseModel painterResponseModel = new PainterResponseModel("f4c80444-5acf-4d57-8902-9f55255e9e55", "Vincent van Gogh", "Netherlands", "1853-03-30", "1890-07-29");
        PaintingPainterResponseModel paintingPainterResponseModel = new PaintingPainterResponseModel(paintingResponseModel1, painterResponseModel);

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

        ExhibitionRequestModel exhibitionRequestModel = ExhibitionRequestModel.builder()
                .exhibitionName("Ontario's Exhibition")
                .roomNumber(203)
                .duration(60)
                .startDay("Monday")
                .endDay("Wednesday")
                .paintings(paintingResponseModels)
                .sculptures(sculptureResponseModels)
                .build();
        String galleryId = "ea85d3ba-d708-4ff3-bbbb-dd9c5c77b8e8";

        GalleryResponseModel galleryResponseModel = new GalleryResponseModel(
                "ea85d3ba-d708-4ff3-bbbb-dd9c5c77b8e8", "Art Gallery of Ontario",
                "Wednesday 10:00 AM", "Sunday 5:00 PM",
                "317 Dundas St W", "Toronto", "Ontario", "Canada",
                "M5T 1G4");

        Exhibition exhibition = buildExhibition();
        Exhibition savedExhibition = buildExhibition();
        savedExhibition.setId("0001");

        when(galleryServiceClient.getGallery(galleryId)).thenReturn(galleryResponseModel);
        when(exhibitionRepository.existsByPaintings_PaintingId(paintingResponseModel1.getPaintingId())).thenReturn(false);
        when(exhibitionRepository.existsByPaintings_PaintingId(paintingResponseModel2.getPaintingId())).thenReturn(false);
        when(exhibitionRepository.existsBySculptures_SculptureId(sculptureResponseModel1.getSculptureId())).thenReturn(false);
        when(exhibitionRepository.existsBySculptures_SculptureId(sculptureResponseModel2.getSculptureId())).thenReturn(false);
        when(exhibitionRepository.save(exhibition)).thenReturn(savedExhibition);
        when(exhibitionRepository.save(any(Exhibition.class))).thenReturn(savedExhibition);

        ExhibitionResponseModel exhibitionResponseModel = exhibitionService.createExhibition(galleryId, exhibitionRequestModel);

        assertNotNull(exhibitionResponseModel);
        assertNotNull(exhibitionResponseModel.getExhibitionId());
        assertEquals(galleryId, exhibitionResponseModel.getGalleryId());
        assertEquals(galleryResponseModel.getName(), exhibitionResponseModel.getGalleryName());
        assertEquals(exhibitionRequestModel.getExhibitionName(), exhibitionResponseModel.getExhibitionName());
        assertEquals(exhibitionRequestModel.getRoomNumber(), exhibitionResponseModel.getRoomNumber());
        assertEquals(exhibitionRequestModel.getDuration(), exhibitionResponseModel.getDuration());
        assertEquals(exhibitionRequestModel.getStartDay(), exhibitionResponseModel.getStartDay());
        assertEquals(exhibitionRequestModel.getEndDay(), exhibitionResponseModel.getEndDay());
        assertEquals(exhibitionRequestModel.getPaintings().size(), exhibitionResponseModel.getPaintings().size());
        assertEquals(exhibitionRequestModel.getSculptures().size(), exhibitionResponseModel.getSculptures().size());


        verify(exhibitionResponseMapper, times(1))
                .entityToResponseModel(savedExhibition);
    }

    // Create Exhibition Exception Non-Existing Gallery
    @Test
    void whenNonExistingGallery_createExhibition_shouldThrowException() {
        ExhibitionRequestModel exhibitionRequestModel = ExhibitionRequestModel.builder()
                .exhibitionName("Ontario's Exhibition")
                .roomNumber(203)
                .duration(60)
                .startDay("Monday")
                .endDay("Wednesday")
                .build();
        String galleryId = "ea85d3ba-d708-4ff3-bbbb-dd9c5c77b8e8";

        when(galleryServiceClient.getGallery(galleryId)).thenReturn(null);

        assertThrows(ExistingGalleryNotFoundException.class,
                () -> exhibitionService.createExhibition(galleryId, exhibitionRequestModel));

        verify(exhibitionRepository, times(0)).save(any(Exhibition.class));
    }

    // Create Exhibition Exception Painting Already in Another Exhibition
    @Test
    void whenPaintingInAnotherExhibition_shouldThrowException(){
        String galleryId = "1";
        ExhibitionRequestModel requestModel = new ExhibitionRequestModel();

        PaintingResponseModel painting = new PaintingResponseModel();
        painting.setPaintingId("1");
        painting.setGalleryId(galleryId);
        requestModel.setPaintings(List.of(painting));

        GalleryResponseModel galleryResponseModel = new GalleryResponseModel();
        galleryResponseModel.setGalleryId(galleryId);
        galleryResponseModel.setName("Test Gallery");

        Exhibition existingExhibition = new Exhibition();
        existingExhibition.setId("existingExhibitionId");
        existingExhibition.setPaintings(List.of(new PaintingResponseModel()));

        when(galleryServiceClient.getGallery(galleryId)).thenReturn(galleryResponseModel);
        when(exhibitionRepository.existsByPaintings_PaintingId(painting.getPaintingId())).thenReturn(true);

        // Act and Assert
        assertThrows(PaintingAlreadyInExhibition.class, () -> {
            exhibitionService.createExhibition(galleryId, requestModel);
        });
    }

    // Create Exhibition Exception Painting Not From Gallery Exception
    @Test
    void createExhibition_whenPaintingNotFromGallery_shouldThrowException(){
        List<PaintingResponseModel> paintingResponseModelsUpdated = new ArrayList<>();
        PaintingResponseModel paintingResponseModel1Updated = new PaintingResponseModel();
        paintingResponseModel1Updated.setPaintingId("3ed9654a-b773-4aa0-ae6b-b22afb636c8e");
        paintingResponseModel1Updated.setTitle("CLOCK");
        paintingResponseModel1Updated.setYearCreated(1889);
        paintingResponseModel1Updated.setPainterId("f4c80444-5acf-4d57-8902-9f55255e9e55");
        paintingResponseModel1Updated.setGalleryId("ea85d3ba-d708-4ff3-bbbb-dd9c5c77b8e0");

        PainterResponseModel painterResponseModelUpdated = new PainterResponseModel("f4c80444-5acf-4d57-8902-9f55255e9e55", "Vincent van Gogh", "Netherlands", "1853-03-30", "1890-07-29");
        PaintingPainterResponseModel paintingPainterResponseModelUpdated = new PaintingPainterResponseModel(paintingResponseModel1Updated, painterResponseModelUpdated);

        paintingResponseModelsUpdated.add(paintingResponseModel1Updated);

        List<SculptureResponseModel> sculptureResponseModelsUpdated= new ArrayList<>();

        SculptureResponseModel sculptureResponseModel1Updated = new SculptureResponseModel();
        sculptureResponseModel1Updated.setSculptureId("acf18748-b00c-4f3a-9d0b-b1b1fdf9c240");
        sculptureResponseModel1Updated.setTitle("Vase UPDATED");
        sculptureResponseModel1Updated.setMaterial("Wood");
        sculptureResponseModel1Updated.setTexture("Rough");
        sculptureResponseModel1Updated.setGalleryId("ea85d3ba-d708-4ff3-bbbb-dd9c5c77b8e8");


        ExhibitionRequestModel exhibitionRequestModelUpdated = ExhibitionRequestModel.builder()
                .exhibitionName("Ontario's Exhibition UPDATED")
                .roomNumber(203)
                .duration(60)
                .startDay("Monday")
                .endDay("Wednesday")
                .paintings(paintingResponseModelsUpdated)
                .sculptures(sculptureResponseModelsUpdated)
                .build();

        GalleryResponseModel galleryResponseModel = new GalleryResponseModel();
        galleryResponseModel.setGalleryId("ea85d3ba-d708-4ff3-bbbb-dd9c5c77b8e8");

        when(galleryServiceClient.getGallery("ea85d3ba-d708-4ff3-bbbb-dd9c5c77b8e8")).thenReturn(galleryResponseModel);
        when(exhibitionRepository.existsByPaintings_PaintingId(paintingPainterResponseModelUpdated.getPaintingResponseModel().getPaintingId())).thenReturn(false);

        // Act and Assert
        assertThrows(PaintingNotFromGalleryException.class, () -> {
            exhibitionService.createExhibition("ea85d3ba-d708-4ff3-bbbb-dd9c5c77b8e8", exhibitionRequestModelUpdated);
        });
    }

    // Create Exhibition Exception Sculpture Already in Another Exhibition
    @Test
    void whenSculptureInAnotherExhibition_shouldThrowException(){
        String galleryId = "1";
        GalleryResponseModel galleryResponseModel = new GalleryResponseModel();
        galleryResponseModel.setGalleryId(galleryId);
        galleryResponseModel.setName("Test Gallery");

        SculptureResponseModel sculpture = new SculptureResponseModel();
        sculpture.setSculptureId("1");
        sculpture.setGalleryId(galleryId);

        ExhibitionRequestModel requestModel = new ExhibitionRequestModel();
        requestModel.setSculptures(List.of(sculpture));

        PaintingResponseModel painting = new PaintingResponseModel();
        painting.setPaintingId("1");
        painting.setGalleryId(galleryId);
        requestModel.setPaintings(List.of(painting));

        Exhibition existingExhibition = new Exhibition();
        existingExhibition.setId("existingExhibitionId");
        existingExhibition.setPaintings(List.of(new PaintingResponseModel()));

        when(galleryServiceClient.getGallery(galleryId)).thenReturn(galleryResponseModel);
        when(exhibitionRepository.existsBySculptures_SculptureId(sculpture.getSculptureId())).thenReturn(true);

        // Act and Assert
        assertThrows(SculptureAlreadyInExhibition.class, () -> {
            exhibitionService.createExhibition(galleryId, requestModel);
        });
    }

    // Create Exhibition Exception Sculpture Not From Gallery Exception
    @Test
    void whenSculptureNotFromGallery_shouldThrowException(){
        List<SculptureResponseModel> sculptureResponseModelList = new ArrayList<>();
        SculptureResponseModel sculptureResponseModel = new SculptureResponseModel();
        sculptureResponseModel.setSculptureId("acf18748-b00c-4f3a-9d0b-b1b1fdf9c240");
        sculptureResponseModel.setTitle("Vase");
        sculptureResponseModel.setMaterial("Wood");
        sculptureResponseModel.setTexture("Rough");
        sculptureResponseModel.setGalleryId("ea85d3ba-d708-4ff3-bbbb-dd9c5c77b8e7");

        sculptureResponseModelList.add(sculptureResponseModel);

        List<PaintingResponseModel> paintingResponseModels = new ArrayList<>();

        ExhibitionRequestModel exhibitionRequestModelUpdated = ExhibitionRequestModel.builder()
                .exhibitionName("Ontario's Exhibition UPDATED")
                .roomNumber(203)
                .duration(60)
                .startDay("Monday")
                .endDay("Wednesday")
                .paintings(paintingResponseModels)
                .sculptures(sculptureResponseModelList)
                .build();

        GalleryResponseModel galleryResponseModel = new GalleryResponseModel();
        galleryResponseModel.setGalleryId("ea85d3ba-d708-4ff3-bbbb-dd9c5c77b8e8");

        when(galleryServiceClient.getGallery("ea85d3ba-d708-4ff3-bbbb-dd9c5c77b8e8")).thenReturn(galleryResponseModel);
        when(exhibitionRepository.existsBySculptures_SculptureId(sculptureResponseModel.getSculptureId())).thenReturn(false);

        // Act and Assert
        assertThrows(SculptureNotFromGalleryException.class, () -> {
            exhibitionService.createExhibition("ea85d3ba-d708-4ff3-bbbb-dd9c5c77b8e8", exhibitionRequestModelUpdated);
        });
    }

    // Update Exhibition
    @Test
    void whenValidExhibitionId_ValidPaintingsIds_ValidSculpturesIds_updateExhibition_shouldSucceed() {
        List<PaintingResponseModel> paintingResponseModelsUpdated = new ArrayList<>();
        PaintingResponseModel paintingResponseModel1Updated = new PaintingResponseModel();
        paintingResponseModel1Updated.setPaintingId("3ed9654a-b773-4aa0-ae6b-b22afb636c8e");
        paintingResponseModel1Updated.setTitle("CLOCK");
        paintingResponseModel1Updated.setYearCreated(1889);
        paintingResponseModel1Updated.setPainterId("f4c80444-5acf-4d57-8902-9f55255e9e55");
        paintingResponseModel1Updated.setGalleryId("ea85d3ba-d708-4ff3-bbbb-dd9c5c77b8e8");

        PaintingRequestModel paintingRequestModel1Updated = new PaintingRequestModel(
                "CLOCK", 1889, "f4c80444-5acf-4d57-8902-9f55255e9e55", "ea85d3ba-d708-4ff3-bbbb-dd9c5c77b8e8");


        PainterResponseModel painterResponseModelUpdated = new PainterResponseModel("f4c80444-5acf-4d57-8902-9f55255e9e55", "Vincent van Gogh", "Netherlands", "1853-03-30", "1890-07-29");
        PaintingPainterResponseModel paintingPainterResponseModelUpdated = new PaintingPainterResponseModel(paintingResponseModel1Updated, painterResponseModelUpdated);

        paintingResponseModelsUpdated.add(paintingResponseModel1Updated);

        List<SculptureResponseModel> sculptureResponseModelsUpdated= new ArrayList<>();

        SculptureResponseModel sculptureResponseModel1Updated = new SculptureResponseModel();
        sculptureResponseModel1Updated.setSculptureId("acf18748-b00c-4f3a-9d0b-b1b1fdf9c240");
        sculptureResponseModel1Updated.setTitle("Vase UPDATED");
        sculptureResponseModel1Updated.setMaterial("Wood");
        sculptureResponseModel1Updated.setTexture("Rough");
        sculptureResponseModel1Updated.setGalleryId("ea85d3ba-d708-4ff3-bbbb-dd9c5c77b8e8");

        SculptureRequestModel sculptureRequestModel1Updated = new SculptureRequestModel(
                "Vase UPDATED", "Wood", "Rough", "ea85d3ba-d708-4ff3-bbbb-dd9c5c77b8e8");
        sculptureResponseModelsUpdated.add(sculptureResponseModel1Updated);

        ExhibitionRequestModel exhibitionRequestModelUpdated = ExhibitionRequestModel.builder()
                .exhibitionName("Ontario's Exhibition UPDATED")
                .roomNumber(203)
                .duration(60)
                .startDay("Monday")
                .endDay("Wednesday")
                .paintings(paintingResponseModelsUpdated)
                .sculptures(sculptureResponseModelsUpdated)
                .build();


        Exhibition exhibition = buildExhibition();
        when(exhibitionRepository.findByExhibitionIdentifier_ExhibitionId("5ea10d1d-46fb-4374-a39c-27d643aa37e3")).thenReturn(exhibition);
        when(exhibitionRepository.existsByExhibitionIdentifier_ExhibitionIdAndPaintings_PaintingId("5ea10d1d-46fb-4374-a39c-27d643aa37e3", "3ed9654a-b773-4aa0-ae6b-b22afb636c8e")).thenReturn(true);
        paintingServiceClient.updatePaintingInGallery("ea85d3ba-d708-4ff3-bbbb-dd9c5c77b8e8", "3ed9654a-b773-4aa0-ae6b-b22afb636c8e", paintingRequestModel1Updated);
        when(exhibitionRepository.existsByExhibitionIdentifier_ExhibitionIdAndSculptures_SculptureId("5ea10d1d-46fb-4374-a39c-27d643aa37e3", "acf18748-b00c-4f3a-9d0b-b1b1fdf9c240")).thenReturn(true);
        sculptureServiceClient.updateSculptureInGallery("ea85d3ba-d708-4ff3-bbbb-dd9c5c77b8e8", "acf18748-b00c-4f3a-9d0b-b1b1fdf9c240", sculptureRequestModel1Updated);
        when(exhibitionRepository.save(exhibition)).thenReturn(exhibition);
        when(exhibitionRepository.save(any(Exhibition.class))).thenReturn(exhibition);

        ExhibitionResponseModel exhibitionResponseModel = exhibitionService.updateExhibition("5ea10d1d-46fb-4374-a39c-27d643aa37e3", exhibitionRequestModelUpdated);

        assertNotNull(exhibitionResponseModel);
        assertNotNull(exhibitionResponseModel.getExhibitionId());
        assertEquals(exhibitionRequestModelUpdated.getExhibitionName(), exhibitionResponseModel.getExhibitionName());
        assertEquals(exhibitionRequestModelUpdated.getRoomNumber(), exhibitionResponseModel.getRoomNumber());
        assertEquals(exhibitionRequestModelUpdated.getDuration(), exhibitionResponseModel.getDuration());
        assertEquals(exhibitionRequestModelUpdated.getStartDay(), exhibitionResponseModel.getStartDay());
        assertEquals(exhibitionRequestModelUpdated.getEndDay(), exhibitionResponseModel.getEndDay());
        assertEquals(exhibitionRequestModelUpdated.getPaintings().get(0).getPaintingId(), exhibitionResponseModel.getPaintings().get(0).getPaintingId());
        assertEquals(exhibitionRequestModelUpdated.getPaintings().get(0).getTitle(), exhibitionResponseModel.getPaintings().get(0).getTitle());
        assertEquals(exhibitionRequestModelUpdated.getPaintings().get(0).getYearCreated(), exhibitionResponseModel.getPaintings().get(0).getYearCreated());
        assertEquals(exhibitionRequestModelUpdated.getPaintings().get(0).getPainterId(), exhibitionResponseModel.getPaintings().get(0).getPainterId());
        assertEquals(exhibitionRequestModelUpdated.getPaintings().get(0).getGalleryId(), exhibitionResponseModel.getPaintings().get(0).getGalleryId());
        assertEquals(exhibitionRequestModelUpdated.getSculptures().get(0).getSculptureId(), exhibitionResponseModel.getSculptures().get(0).getSculptureId());
        assertEquals(exhibitionRequestModelUpdated.getSculptures().get(0).getTitle(), exhibitionResponseModel.getSculptures().get(0).getTitle());
        assertEquals(exhibitionRequestModelUpdated.getSculptures().get(0).getMaterial(), exhibitionResponseModel.getSculptures().get(0).getMaterial());
        assertEquals(exhibitionRequestModelUpdated.getSculptures().get(0).getTexture(), exhibitionResponseModel.getSculptures().get(0).getTexture());
        assertEquals(exhibitionRequestModelUpdated.getSculptures().get(0).getGalleryId(), exhibitionResponseModel.getSculptures().get(0).getGalleryId());
    }

    // Update Exhibition Exception Exhibition Not Found
    @Test
    void whenInvalidExhibitionId_updateExhibition() {
        ExhibitionRequestModel exhibitionRequestModelUpdated = ExhibitionRequestModel.builder()
                .exhibitionName("Ontario's Exhibition UPDATED")
                .roomNumber(203)
                .duration(60)
                .startDay("Monday")
                .endDay("Wednesday")
                .build();

        when(exhibitionRepository.findByExhibitionIdentifier_ExhibitionId("5ea10d1d-46fb-4374-a39c-27d643aa37e3")).thenReturn(null);
        assertThrows(ExistingExhibitionNotFoundException.class, () -> {
            exhibitionService.updateExhibition("5ea10d1d-46fb-4374-a39c-27d643aa37e3", exhibitionRequestModelUpdated);
        });
    }

    // Update Exhibition Exception Painting Not From Gallery
    @Test
    void whenInvalidPaintingId_updateExhibition() {
        List<PaintingResponseModel> paintingResponseModelsUpdated = new ArrayList<>();
        paintingResponseModelsUpdated.add(PaintingResponseModel.builder()
                .paintingId("3ed9654a-b773-4aa0-ae6b-b22afb636c8e")
                .title("Mona Lisa UPDATED")
                .yearCreated(1503)
                .painterId("ea85d3ba-d708-4ff3-bbbb-dd9c5c77b8e8")
                .galleryId("5ea10d1d-46fb-4374-a39c-27d643aa37e3")
                .build());

        List<SculptureResponseModel> sculptureResponseModelsUpdated = new ArrayList<>();
        sculptureResponseModelsUpdated.add(SculptureResponseModel.builder()
                .sculptureId("acf18748-b00c-4f3a-9d0b-b1b1fdf9c240")
                .title("David UPDATED")
                .material("Marble")
                .texture("Smooth")
                .galleryId("5ea10d1d-46fb-4374-a39c-27d643aa37e3")
                .build());

        ExhibitionRequestModel exhibitionRequestModelUpdated = ExhibitionRequestModel.builder()
                .exhibitionName("Ontario's Exhibition UPDATED")
                .roomNumber(203)
                .duration(60)
                .startDay("Monday")
                .endDay("Wednesday")
                .paintings(paintingResponseModelsUpdated)
                .sculptures(sculptureResponseModelsUpdated)
                .build();

        Exhibition exhibition = buildExhibition();
        when(exhibitionRepository.findByExhibitionIdentifier_ExhibitionId("5ea10d1d-46fb-4374-a39c-27d643aa37e3")).thenReturn(exhibition);
        when(paintingServiceClient.getPaintingsInGallery("5ea10d1d-46fb-4374-a39c-27d643aa37e3")).thenReturn(null);
        when(exhibitionRepository.existsByExhibitionIdentifier_ExhibitionIdAndPaintings_PaintingId("5ea10d1d-46fb-4374-a39c-27d643aa37e3", "3ed9654a-b773-4aa0-ae6b-b22afb636c8e")).thenReturn(false);
        assertThrows(PaintingNotFromGalleryException.class, () -> {
            exhibitionService.updateExhibition("5ea10d1d-46fb-4374-a39c-27d643aa37e3", exhibitionRequestModelUpdated);
        });
    }


    // Update Exhibition: add painting to exhibition if painting is from gallery
    @Test
    void givenPaintingNotInExhibitionButInGallery_whenUpdatingExhibition_thenPaintingAddedToExhibition() {
        // Arrange
        String galleryId = "ea85d3ba-d708-4ff3-bbbb-dd9c5c77b8e8";
        PaintingResponseModel painting2 = PaintingResponseModel.builder()
                .paintingId("painting-002")
                .title("Good Day")
                .yearCreated(1856)
                .painterId("painter-001")
                .galleryId(galleryId)
                .build();

        SculptureResponseModel sculpture = SculptureResponseModel.builder()
                .sculptureId("sculpture-001")
                .title("David")
                .material("Marble")
                .texture("Smooth")
                .galleryId(galleryId)
                .build();

        ExhibitionRequestModel exhibitionRequestModel = ExhibitionRequestModel.builder()
                .exhibitionName("Spring Exhibition")
                .roomNumber(1)
                .duration(30)
                .startDay("Monday")
                .endDay("Wednesday")
                .paintings(List.of(painting2))
                .sculptures(List.of(sculpture))
                .build();

        ExhibitionIdentifier exhibitionIdentifier = new ExhibitionIdentifier();
        exhibitionIdentifier.setExhibitionId("exhibition-001");

        Exhibition existingExhibition = buildExhibition();

        when(exhibitionRepository.findByExhibitionIdentifier_ExhibitionId(existingExhibition.getExhibitionIdentifier().getExhibitionId()))
                .thenReturn(existingExhibition);
        when(exhibitionRepository.save(any(Exhibition.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        ExhibitionResponseModel updatedExhibition = exhibitionService.updateExhibition(existingExhibition.getExhibitionIdentifier().getExhibitionId(), exhibitionRequestModel);

        // Assert
        assertNotNull(updatedExhibition);
        assertTrue(updatedExhibition.getPaintings()
                .stream()
                .anyMatch(p -> p.getPaintingId().equals(painting2.getPaintingId())));
    }



    // Update Exhibition Exception Sculpture Not From Gallery
    @Test
    void whenInvalidSculptureId_updateExhibition() {
        List<PaintingResponseModel> paintingResponseModelsUpdated = new ArrayList<>();
        paintingResponseModelsUpdated.add(PaintingResponseModel.builder()
                .paintingId("3ed9654a-b773-4aa0-ae6b-b22afb636c8e")
                .title("Mona Lisa UPDATED")
                .yearCreated(1503)
                .painterId("ea85d3ba-d708-4ff3-bbbb-dd9c5c77b8e8")
                .galleryId("5ea10d1d-46fb-4374-a39c-27d643aa37e3")
                .build());

        List<SculptureResponseModel> sculptureResponseModelsUpdated = new ArrayList<>();
        sculptureResponseModelsUpdated.add(SculptureResponseModel.builder()
                .sculptureId("acf18748-b00c-4f3a-9d0b-b1b1fdf9c240")
                .title("David UPDATED")
                .material("Marble")
                .texture("Smooth")
                .galleryId("5ea10d1d-46fb-4374-a39c-27d643aa37e3")
                .build());

        ExhibitionRequestModel exhibitionRequestModelUpdated = ExhibitionRequestModel.builder()
                .exhibitionName("Ontario's Exhibition UPDATED")
                .roomNumber(203)
                .duration(60)
                .startDay("Monday")
                .endDay("Wednesday")
                .paintings(paintingResponseModelsUpdated)
                .sculptures(sculptureResponseModelsUpdated)
                .build();

        Exhibition exhibition = buildExhibition();
        when(exhibitionRepository.findByExhibitionIdentifier_ExhibitionId("5ea10d1d-46fb-4374-a39c-27d643aa37e3")).thenReturn(exhibition);
        when(exhibitionRepository.existsByExhibitionIdentifier_ExhibitionIdAndPaintings_PaintingId("5ea10d1d-46fb-4374-a39c-27d643aa37e3", "3ed9654a-b773-4aa0-ae6b-b22afb636c8e")).thenReturn(true);
        when(sculptureServiceClient.getAllSculpturesInGallery("5ea10d1d-46fb-4374-a39c-27d643aa37e3")).thenReturn(null);
        when(exhibitionRepository.existsByExhibitionIdentifier_ExhibitionIdAndSculptures_SculptureId("5ea10d1d-46fb-4374-a39c-27d643aa37e3", "acf18748-b00c-4f3a-9d0b-b1b1fdf9c240")).thenReturn(false);
        assertThrows(SculptureNotFromGalleryException.class, () -> {
            exhibitionService.updateExhibition("5ea10d1d-46fb-4374-a39c-27d643aa37e3", exhibitionRequestModelUpdated);
        });
    }

    // Delete Exhibition
    @Test
    void whenValidExhibitionId_deleteExhibition() {
        Exhibition exhibition = buildExhibition();
        when(exhibitionRepository.findByExhibitionIdentifier_ExhibitionId("5ea10d1d-46fb-4374-a39c-27d643aa37e3")).thenReturn(exhibition);
        exhibitionService.removeExhibition("5ea10d1d-46fb-4374-a39c-27d643aa37e3");
        verify(exhibitionRepository, times(1)).delete(exhibition);
    }

    // Delete Exhibition Exception
    @Test
    void whenInvalidExhibitionId_deleteExhibition() {
        when(exhibitionRepository.findByExhibitionIdentifier_ExhibitionId("5ea10d1d-46fb-4374-a39c-27d643aa37e3")).thenReturn(null);
        assertThrows(ExistingExhibitionNotFoundException.class, () -> {
            exhibitionService.removeExhibition("5ea10d1d-46fb-4374-a39c-27d643aa37e3");
        });
    }

    // Delete All Exhibitions
    @Test
    void whenValid_deleteAllExhibitions() {
        exhibitionService.removeAllExhibitions();
        verify(exhibitionRepository, times(1)).deleteAll();

        assertEquals(0, exhibitionRepository.count());
    }


    private Exhibition buildExhibition(){
        ExhibitionIdentifier exhibitionIdentifier = new ExhibitionIdentifier();
        GalleryIdentifier galleryIdentifier = new GalleryIdentifier("ea85d3ba-d708-4ff3-bbbb-dd9c5c77b8e8");
        String galleryName = "Art Gallery of Ontario";
        String exhibitionName = "Ontario's Exhibition";
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

        var exhibition = Exhibition.builder()
                .exhibitionIdentifier(exhibitionIdentifier)
                .galleryIdentifier(galleryIdentifier)
                .galleryName(galleryName)
                .exhibitionName(exhibitionName)
                .roomNumber(roomNumber1)
                .duration(duration1)
                .startDay(startDay1)
                .endDay(endDay1)
                .paintings(paintingResponseModels)
                .sculptures(sculptureResponseModels)
                .build();

        return exhibition;
    }

}
