package com.gallery.exhibitionservice.businesslayer;

import com.gallery.exhibitionservice.datalayer.Exhibition;
import com.gallery.exhibitionservice.datalayer.ExhibitionIdentifier;
import com.gallery.exhibitionservice.datalayer.ExhibitionRepository;
import com.gallery.exhibitionservice.datalayer.GalleryIdentifier;
import com.gallery.exhibitionservice.datamapperlayer.ExhibitionResponseMapper;

import com.gallery.exhibitionservice.domainclientlayer.*;
import com.gallery.exhibitionservice.presentationlayer.ExhibitionRequestModel;
import com.gallery.exhibitionservice.presentationlayer.ExhibitionResponseModel;

import com.gallery.exhibitionservice.domainclientlayer.PaintingResponseModel;
import com.gallery.exhibitionservice.domainclientlayer.SculptureResponseModel;
import com.gallery.exhibitionservice.utils.exceptions.BadRequestException;
import com.gallery.exhibitionservice.utils.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExhibitionServiceImpl implements ExhibitionService {

    private final ExhibitionRepository exhibitionRepository;
    private final ExhibitionResponseMapper exhibitionResponseMapper;

    private final GalleryServiceClient galleryServiceClient;

    private final PaintingServiceClient paintingServiceClient;

    private final SculptureServiceClient sculptureServiceClient;

    @Override
    public List<ExhibitionResponseModel> getAllExhibitions() {
       // check if gallery exists
        List<Exhibition> exhibitions = exhibitionRepository.findAll();
        return exhibitionResponseMapper.entityListToResponseModelList(exhibitions);
    }

    @Override
    public ExhibitionResponseModel createExhibition(String galleryId, ExhibitionRequestModel exhibitionRequestModel) {
        // orchestration pattern

        //log.debug("PAINTINGS SIZE " + exhibitionRequestModel.getPaintings().size());
        //log.debug("SCULPTURES SIZE " + exhibitionRequestModel.getSculptures().size());
        // validate gallery id by getting its data from gallery-service
        /*GalleryResponseModel galleryResponseModel = galleryServiceClient.getGallery(galleryId);
        if(galleryResponseModel == null){
            throw new NotFoundException("Unknown gallery id " + galleryId);
        }*/

        for (PaintingResponseModel painting: exhibitionRequestModel.getPaintings()) {
            if(paintingServiceClient.getPaintingAggregateById(galleryId, painting.getPaintingId()) == null ){
                throw new BadRequestException("The painting should come from the gallery with id " + galleryId);
            }
        }

        for (SculptureResponseModel sculpture: exhibitionRequestModel.getSculptures()) {
            if(sculptureServiceClient.getSculptureById(galleryId, sculpture.getSculptureId()) == null){
                throw new BadRequestException("The sculpture should come from the gallery with id " + galleryId);
            }
        }

        Exhibition exhibition = new Exhibition().builder()
                .exhibitionIdentifier(new ExhibitionIdentifier())
                .galleryIdentifier(new GalleryIdentifier(galleryId))
                .galleryName("SOME GALLERY")
                .exhibitionName(exhibitionRequestModel.getExhibitionName())
                .roomNumber(exhibitionRequestModel.getRoomNumber())
                .duration(exhibitionRequestModel.getDuration())
                .startDay(exhibitionRequestModel.getStartDay())
                .endDay(exhibitionRequestModel.getEndDay())
                .paintings(exhibitionRequestModel.getPaintings())
                .sculptures(exhibitionRequestModel.getSculptures())
                .build();

        Exhibition saved = exhibitionRepository.save(exhibition);
        return exhibitionResponseMapper.entityToResponseModel(saved);

    }

    /*ExhibitionRepository exhibitionRepository;
    ExhibitionResponseMapper exhibitionResponseMapper;
    ExhibitionRequestMapper exhibitionRequestMapper;

    GalleryExhibitionPaintingSculptureResponseMapper galleryExhibitionPaintingPainterResponseMapper;
    GalleryRepository galleryRepository;
    GalleryRequestMapper galleryRequestMapper;
    GalleryResponseMapper galleryResponseMapper;
    PaintingRepository paintingRepository;
    PainterRepository painterRepository;
    PaintingResponseMapper paintingResponseMapper;
    PaintingRequestMapper paintingRequestMapper;
    PainterResponseMapper painterResponseMapper;
    PaintingPainterResponseMapper paintingPainterResponseMapper;
    SculptureResponseMapper sculptureResponseMapper;
    SculptureRepository sculptureRepository;
    SculptureRequestMapper sculptureRequestMapper;

    public ExhibitionServiceImpl(ExhibitionRepository exhibitionRepository, ExhibitionResponseMapper exhibitionResponseMapper, ExhibitionRequestMapper exhibitionRequestMapper, GalleryExhibitionPaintingSculptureResponseMapper galleryExhibitionPaintingPainterResponseMapper, GalleryRepository galleryRepository, GalleryRequestMapper galleryRequestMapper, GalleryResponseMapper galleryResponseMapper, PaintingRepository paintingRepository, PainterRepository painterRepository, PaintingResponseMapper paintingResponseMapper, PaintingRequestMapper paintingRequestMapper, PainterResponseMapper painterResponseMapper, PaintingPainterResponseMapper paintingPainterResponseMapper, SculptureResponseMapper sculptureResponseMapper, SculptureRepository sculptureRepository, SculptureRequestMapper sculptureRequestMapper) {
        this.exhibitionRepository = exhibitionRepository;
        this.exhibitionResponseMapper = exhibitionResponseMapper;
        this.exhibitionRequestMapper = exhibitionRequestMapper;
        this.galleryExhibitionPaintingPainterResponseMapper = galleryExhibitionPaintingPainterResponseMapper;
        this.galleryRepository = galleryRepository;
        this.galleryRequestMapper = galleryRequestMapper;
        this.galleryResponseMapper = galleryResponseMapper;
        this.paintingRepository = paintingRepository;
        this.painterRepository = painterRepository;
        this.paintingResponseMapper = paintingResponseMapper;
        this.paintingRequestMapper = paintingRequestMapper;
        this.painterResponseMapper = painterResponseMapper;
        this.paintingPainterResponseMapper = paintingPainterResponseMapper;
        this.sculptureResponseMapper = sculptureResponseMapper;
        this.sculptureRepository = sculptureRepository;
        this.sculptureRequestMapper = sculptureRequestMapper;
    }

    @Override
    public List<ExhibitionResponseModel> getExhibitionsInGallery(String galleryId) {
        if(!galleryRepository.existsByGalleryIdentifier_GalleryId(galleryId)){
            throw new ExistingGalleryNotFoundException("Gallery with id: " + galleryId + " does not exist");
        }

        return exhibitionResponseMapper.entityListToResponseModelList(exhibitionRepository.findAllByGalleryIdentifier_GalleryId(galleryId));
    }

    @Override
    public GalleryExhibitionPaintingSculptureResponseModel getExhibitionByIdInGallery(String galleryId, String exhibitionId) {
        Gallery gallery = galleryRepository.findByGalleryIdentifier_GalleryId(galleryId);
        if(gallery == null){
            throw new ExistingGalleryNotFoundException("Gallery with id: " + galleryId + " does not exist");
        }
        List<PaintingResponseModel> paintingResponseModels = paintingResponseMapper.entityListToResponseModelList(paintingRepository.findAllByGalleryIdentifier_GalleryIdAndExhibitionIdentifier_ExhibitionId(galleryId, exhibitionId));
        ExhibitionResponseModel exhibition = exhibitionResponseMapper.entityToResponseModel(exhibitionRepository.findByGalleryIdentifier_GalleryIdAndExhibitionIdentifier_ExhibitionId(galleryId, exhibitionId));
        List<SculptureResponseModel> sculptureResponseModels = sculptureResponseMapper.entityListToResponseModelList(sculptureRepository.findAllByGalleryIdentifier_GalleryIdAndExhibitionIdentifier_ExhibitionId(galleryId, exhibitionId));
        return galleryExhibitionPaintingPainterResponseMapper.entityToResponseModel(gallery, paintingResponseModels, exhibition, sculptureResponseModels);
    }

    @Override
    public GalleryExhibitionPaintingSculptureResponseModel getExhibitionByField(String galleryId, Map<String, String> queryParams) {
        Gallery gallery = galleryRepository.findByGalleryIdentifier_GalleryId(galleryId);
        if(gallery == null){
            throw new ExistingGalleryNotFoundException("Gallery with id: " + galleryId + " does not exist");
        }
        String name = queryParams.get("name");
        if(name == null) {
            throw new NameMissingFromQueryParamsException("Name param missing from query param");
        }
        Exhibition exhibition = exhibitionRepository.findByGalleryIdentifier_GalleryIdAndNameIgnoreCase(galleryId, name);
        if(exhibition == null){
            throw new ExistingExhibitionNotFoundException("Exhibition with name " + name + " does not exist.");
        }
        List<PaintingResponseModel> paintingResponseModels = paintingResponseMapper.entityListToResponseModelList(paintingRepository.findAllByGalleryIdentifier_GalleryIdAndExhibitionIdentifier_ExhibitionId(galleryId, exhibition.getExhibitionIdentifier().getExhibitionId()));
        ExhibitionResponseModel exhibitionToReturn = exhibitionResponseMapper.entityToResponseModel(exhibition);
        List<SculptureResponseModel> sculptureResponseModels = sculptureResponseMapper.entityListToResponseModelList(sculptureRepository.findAllByGalleryIdentifier_GalleryIdAndExhibitionIdentifier_ExhibitionId(galleryId, exhibition.getExhibitionIdentifier().getExhibitionId()));
        return galleryExhibitionPaintingPainterResponseMapper.entityToResponseModel(gallery, paintingResponseModels, exhibitionToReturn, sculptureResponseModels);
    }*/

    /*@Override
    public GalleryExhibitionPaintingResponseModel addExhibitionToGallery(String galleryId, ExhibitionRequestModel exhibitionRequestModel) {
        Gallery gallery = galleryRepository.findByGalleryIdentifier_GalleryId(galleryId);
        if(!galleryRepository.existsByGalleryIdentifier_GalleryId(galleryId)){
            throw new ExistingGalleryNotFoundException("Gallery with id: " + galleryId + " does not exist");
        }

        Exhibition exhibition = exhibitionRequestMapper.requestModelToEntity(exhibitionRequestModel, gallery.getGalleryIdentifier());
        ExhibitionResponseModel exhibitionToBeAdded = exhibitionResponseMapper.entityToResponseModel(exhibitionRepository.save(exhibition));
        List<PaintingResponseModel> paintingResponseModels = paintingResponseMapper.entityListToResponseModelList(paintingRepository.findAllByGalleryIdentifier_GalleryIdAndExhibitionIdentifier_ExhibitionId(galleryId, exhibitionToBeAdded.getExhibitionId()));
        return galleryExhibitionPaintingPainterResponseMapper.entityToResponseModel(gallery, paintingResponseModels, exhibitionToBeAdded);
    }

    @Override
    public GalleryExhibitionPaintingResponseModel updateExhibitionInGallery(String galleryId, String exhibitionId, ExhibitionRequestModel exhibitionRequestModel) {
        Gallery gallery = galleryRepository.findByGalleryIdentifier_GalleryId(galleryId);
        if(!galleryRepository.existsByGalleryIdentifier_GalleryId(galleryId)){
            throw new ExistingGalleryNotFoundException("Gallery with id: " + galleryId + " does not exist");
        }
        Exhibition existingExhibition = exhibitionRepository.findByGalleryIdentifier_GalleryIdAndExhibitionIdentifier_ExhibitionId(galleryId, exhibitionId);
        if(existingExhibition == null){
            throw new ExistingExhibitionNotFoundException("Exhibition with id: " + exhibitionId + " does not exist");
        }
        Exhibition updatedExhibition = exhibitionRequestMapper.requestModelToEntity(exhibitionRequestModel, gallery.getGalleryIdentifier());
        updatedExhibition.setId(existingExhibition.getId());
        updatedExhibition.setExhibitionIdentifier(existingExhibition.getExhibitionIdentifier());
        ExhibitionResponseModel exhibitionToUpdate = exhibitionResponseMapper.entityToResponseModel(exhibitionRepository.save(updatedExhibition));
        List<PaintingResponseModel> paintingResponseModels = paintingResponseMapper.entityListToResponseModelList(paintingRepository.findAllByGalleryIdentifier_GalleryIdAndExhibitionIdentifier_ExhibitionId(galleryId, exhibitionToUpdate.getExhibitionId()));
        return galleryExhibitionPaintingPainterResponseMapper.entityToResponseModel(gallery, paintingResponseModels, exhibitionToUpdate);
    }*/

    /*@Override
    public void removeExhibitionFromGallery(String galleryId, String exhibitionId) {
        if(!galleryRepository.existsByGalleryIdentifier_GalleryId(galleryId)){
            throw new ExistingGalleryNotFoundException("Gallery with id: " + galleryId + " does not exist");
        }
        Exhibition exhibition = exhibitionRepository.findByGalleryIdentifier_GalleryIdAndExhibitionIdentifier_ExhibitionId(galleryId, exhibitionId);
        if(exhibition == null){
            throw new ExistingExhibitionNotFoundException("Exhibition with id: " + exhibitionId + " does not exist");
        }
        exhibitionRepository.delete(exhibition);
    }

    @Override
    public void removeAllExhibitionsFromGallery(String galleryId) {
        if(!galleryRepository.existsByGalleryIdentifier_GalleryId(galleryId)){
            throw new ExistingGalleryNotFoundException("Gallery with id: " + galleryId + " does not exist");
        }
        List<Exhibition> exhibitions = exhibitionRepository.findAllByGalleryIdentifier_GalleryId(galleryId);
        exhibitions.forEach(exhibition -> exhibitionRepository.delete(exhibition));
    }

    @Override
    public void removeExhibitionByField(String galleryId, Map<String, String> queryParams){
        if(!galleryRepository.existsByGalleryIdentifier_GalleryId(galleryId)){
            throw new ExistingGalleryNotFoundException("Gallery with id: " + galleryId + " does not exist");
        }
        String name = queryParams.get("name");

        if(name != null){
            Exhibition exhibition = exhibitionRepository.findByGalleryIdentifier_GalleryIdAndNameIgnoreCase(galleryId, name);
            exhibitionRepository.delete(exhibition);
        }
    }

    @Override
    public GalleryExhibitionPaintingSculptureResponseModel addExhibitionInGallery(String galleryId, GalleryExhibitionPaintingSculptureRequestModel galleryExhibitionPaintingRequestModel) {
        // Check if the gallery exists
        Gallery gallery = galleryRepository.findByGalleryIdentifier_GalleryId(galleryId);
        if (gallery == null) {
            throw new ExistingGalleryNotFoundException("Gallery with id: " + galleryId + " does not exist");
        }

        // Check if the exhibition exists in the gallery
        String exhibitionId = galleryExhibitionPaintingRequestModel.getExhibition().getName();
        Exhibition exhibition = exhibitionRepository.findByGalleryIdentifier_GalleryIdAndNameIgnoreCase(galleryId, exhibitionId);
        if (exhibition == null) {
            // Create the exhibition in the gallery
            ExhibitionRequestModel exhibitionRequestModel = galleryExhibitionPaintingRequestModel.getExhibition();
            Exhibition newExhibition = exhibitionRequestMapper.requestModelToEntity(exhibitionRequestModel, gallery.getGalleryIdentifier());
            // Set exhibitionIdentifier based on the exhibitionId
            exhibitionId = UUID.randomUUID().toString();
            newExhibition.getExhibitionIdentifier().setExhibitionId(exhibitionId);
            exhibition = exhibitionRepository.save(newExhibition);
        }

        // Add the paintings to the exhibition if they are not already in the gallery or another exhibition
        List<PaintingRequestModel> paintings = galleryExhibitionPaintingRequestModel.getPaintings();
        List<PaintingResponseModel> paintingResponseModels = new ArrayList<>();
        for (PaintingRequestModel painting : paintings) {
            if (!paintingRepository.existsByGalleryIdentifier_GalleryIdAndExhibitionIdentifier_ExhibitionIdAndTitle(galleryId, exhibitionId, painting.getTitle())) {
                // Create the painting and add it to the exhibition
                PainterIdentifier painterIdentifier = new PainterIdentifier();
                painterIdentifier.setPainterId("");
                Painter painter = painterRepository.findByPainterIdentifier_PainterId(painterIdentifier.getPainterId());
                ExhibitionIdentifier exhibitionIdentifier = new ExhibitionIdentifier();
                exhibitionIdentifier.setExhibitionId(exhibitionId);
                Painting newPainting = paintingRequestMapper.requestModelToEntity(painting, painterIdentifier, exhibitionIdentifier, gallery.getGalleryIdentifier());
                PaintingResponseModel paintingToAdd = paintingResponseMapper.entityToResponseModel(paintingRepository.save(newPainting));
                paintingResponseModels.add(paintingToAdd);
            }
        }

        // Add the sculptures to the exhibition if they are not already in the gallery or another exhibition
        List<SculptureRequestModel> sculptures = galleryExhibitionPaintingRequestModel.getSculptures();
        List<SculptureResponseModel> sculptureResponseModels = new ArrayList<>();
        for(SculptureRequestModel sculpture: sculptures){
            if(!sculptureRepository.existsByGalleryIdentifier_GalleryIdAndExhibitionIdentifier_ExhibitionIdAndTitle(galleryId, exhibitionId, sculpture.getTitle())){
                // Create the sculpture and add it to the exhibition
                ExhibitionIdentifier exhibitionIdentifier = new ExhibitionIdentifier();
                exhibitionIdentifier.setExhibitionId(exhibitionId);
                Sculpture newSculpture = sculptureRequestMapper.requestModelToEntity(sculpture, exhibitionIdentifier, gallery.getGalleryIdentifier());
                SculptureResponseModel sculptureToAdd = sculptureResponseMapper.entityToResponseModel(sculptureRepository.save(newSculpture));
                sculptureResponseModels.add(sculptureToAdd);
            }
        }

        ExhibitionResponseModel exhibitionResponseModel = exhibitionResponseMapper.entityToResponseModel(exhibition);
        GalleryExhibitionPaintingSculptureResponseModel galleryExhibitionPaintingSculptureResponseModel = galleryExhibitionPaintingPainterResponseMapper.entityToResponseModel(gallery, paintingResponseModels, exhibitionResponseModel, sculptureResponseModels);
        return galleryExhibitionPaintingSculptureResponseModel;
    }

    @Override
    public GalleryExhibitionPaintingSculptureResponseModel updateExhibitionInGallery(String galleryId, String exhibitionId, GalleryExhibitionPaintingSculptureRequestModel galleryExhibitionPaintingRequestModel) {
        // Check if the gallery exists
        Gallery gallery = galleryRepository.findByGalleryIdentifier_GalleryId(galleryId);
        if (gallery == null) {
            throw new ExistingGalleryNotFoundException("Gallery with id: " + galleryId + " does not exist");
        }

        // Update the exhibition details
        GalleryRequestModel galleryRequestModel = galleryExhibitionPaintingRequestModel.getGallery();
        gallery.setName(galleryRequestModel.getName());
        gallery.setOpenUntil(galleryRequestModel.getOpenUntil());
        gallery.setOpenFrom(galleryRequestModel.getOpenFrom());
        galleryRepository.save(gallery);

        // Check if the exhibition exists in the gallery
        Exhibition exhibition = exhibitionRepository.findByGalleryIdentifier_GalleryIdAndExhibitionIdentifier_ExhibitionId(galleryId, exhibitionId);
        if (exhibition == null) {
            throw new ExistingExhibitionNotFoundException("Exhibition with id: " + exhibitionId + " does not exist in gallery with id: " + galleryId);
        }

        // Update the exhibition details
        ExhibitionRequestModel exhibitionRequestModel = galleryExhibitionPaintingRequestModel.getExhibition();
        exhibition.setName(exhibitionRequestModel.getName());
        exhibition.setRoomNumber(exhibitionRequestModel.getRoomNumber());
        exhibition.setDuration(exhibitionRequestModel.getDuration());
        exhibition.setStartDay(exhibitionRequestModel.getStartDay());
        exhibition.setEndDay(exhibitionRequestModel.getEndDay());
        exhibition.setName(exhibitionRequestModel.getName());
        Exhibition updatedExhibition = exhibitionRepository.save(exhibition);

        // Update the paintings in the exhibition
        List<PaintingRequestModel> paintings = galleryExhibitionPaintingRequestModel.getPaintings();
        List<PaintingResponseModel> paintingResponseModels = new ArrayList<>();
        for (PaintingRequestModel painting : paintings) {
            // Check if the painting exists in the exhibition
            Painting existingPainting = paintingRepository.findByGalleryIdentifier_GalleryIdAndExhibitionIdentifier_ExhibitionIdAndTitle(galleryId, exhibitionId, painting.getTitle());
            if (existingPainting != null) {
                // Update the painting details
                existingPainting.setTitle(painting.getTitle());
                existingPainting.setYear(painting.getYear());
                Painting updatedPainting = paintingRepository.save(existingPainting);
                PaintingResponseModel paintingToUpdate = paintingResponseMapper.entityToResponseModel(updatedPainting);
                paintingResponseModels.add(paintingToUpdate);
            } else {
                // Check if the painting exists in the gallery
                Painting existingPaintingInGallery = paintingRepository.findByGalleryIdentifier_GalleryIdAndTitle(galleryId, painting.getTitle());
                if (existingPaintingInGallery != null) {
                    // Add the painting to the exhibition
                    ExhibitionIdentifier exhibitionIdentifier = new ExhibitionIdentifier();
                    exhibitionIdentifier.setExhibitionId(exhibitionId);
                    GalleryIdentifier galleryIdentifier = galleryRepository.findByGalleryIdentifier_GalleryId(galleryId).getGalleryIdentifier();
                    existingPaintingInGallery.setExhibitionIdentifier(exhibitionIdentifier);
                    existingPaintingInGallery.setGalleryIdentifier(galleryIdentifier);
                    Painting updatedPainting = paintingRepository.save(existingPaintingInGallery);
                    PaintingResponseModel paintingToAdd = paintingResponseMapper.entityToResponseModel(updatedPainting);
                    paintingResponseModels.add(paintingToAdd);
                } else {
                    // Create painting and put it in the exhibition in the gallery
                    PainterIdentifier painterIdentifier = new PainterIdentifier();
                    painterIdentifier.setPainterId("");
                    Painter painter = painterRepository.findByPainterIdentifier_PainterId(painterIdentifier.getPainterId());
                    ExhibitionIdentifier exhibitionIdentifier = new ExhibitionIdentifier();
                    exhibitionIdentifier.setExhibitionId(exhibitionId);
                    GalleryIdentifier galleryIdentifier = galleryRepository.findByGalleryIdentifier_GalleryId(galleryId).getGalleryIdentifier();
                    Painting newPainting = paintingRequestMapper.requestModelToEntity(painting, painterIdentifier, exhibitionIdentifier, galleryIdentifier);

                    newPainting.setExhibitionIdentifier(exhibitionIdentifier);
                    PaintingResponseModel paintingToAdd = paintingResponseMapper.entityToResponseModel(paintingRepository.save(newPainting));
                    paintingResponseModels.add(paintingToAdd);
                    PainterResponseModel painterOfPainting = painterResponseMapper.entityToResponseModel(painter);
                    paintingPainterResponseMapper.entityToResponseModel(paintingToAdd, painterOfPainting);
                }

            }
        }

        // Update the sculptures in the exhibition
        List<SculptureRequestModel> sculptures = galleryExhibitionPaintingRequestModel.getSculptures();
        List<SculptureResponseModel> sculptureResponseModels = new ArrayList<>();
        for(SculptureRequestModel sculpture: sculptures){
            // Check if the sculpture exists in the exhibition
            Sculpture existingSculpture = sculptureRepository.findByGalleryIdentifier_GalleryIdAndExhibitionIdentifier_ExhibitionIdAndTitle(galleryId, exhibitionId, sculpture.getTitle());
            if(existingSculpture != null){
                // Update the sculpture details
                existingSculpture.setTitle(sculpture.getTitle());
                existingSculpture.setMaterial(sculpture.getMaterial());
                existingSculpture.setTexture(sculpture.getTexture());
                Sculpture updatedSculpture = sculptureRepository.save(existingSculpture);
                SculptureResponseModel sculptureToBeUpdated = sculptureResponseMapper.entityToResponseModel(updatedSculpture);
                sculptureResponseModels.add(sculptureToBeUpdated);
            }else {
                // Check if the sculpture exists in the gallery
                Sculpture existingSculptureInGallery = sculptureRepository.findByGalleryIdentifier_GalleryIdAndSculptureIdentifier_SculptureId(galleryId, sculpture.getGalleryId());
                if (existingSculptureInGallery != null) {
                    // Add the sculpture to the exhibition
                    ExhibitionIdentifier exhibitionIdentifier = new ExhibitionIdentifier();
                    exhibitionIdentifier.setExhibitionId(exhibitionId);
                    GalleryIdentifier galleryIdentifier = galleryRepository.findByGalleryIdentifier_GalleryId(galleryId).getGalleryIdentifier();
                    existingSculptureInGallery.setExhibitionIdentifier(exhibitionIdentifier);
                    existingSculptureInGallery.setGalleryIdentifier(galleryIdentifier);
                    Sculpture updatedSculpture = sculptureRepository.save(existingSculptureInGallery);
                    SculptureResponseModel sculptureToAdd = sculptureResponseMapper.entityToResponseModel(updatedSculpture);
                    sculptureResponseModels.add(sculptureToAdd);
                } else {
                    // Create sculpture and put it in the exhibition in the gallery
                    ExhibitionIdentifier exhibitionIdentifier = new ExhibitionIdentifier();
                    exhibitionIdentifier.setExhibitionId(exhibitionId);
                    GalleryIdentifier galleryIdentifier = galleryRepository.findByGalleryIdentifier_GalleryId(galleryId).getGalleryIdentifier();
                    Sculpture newSculpture = sculptureRequestMapper.requestModelToEntity(sculpture, exhibitionIdentifier, galleryIdentifier);
                    newSculpture.setExhibitionIdentifier(exhibitionIdentifier);
                    SculptureResponseModel sculptureToAdd = sculptureResponseMapper.entityToResponseModel(sculptureRepository.save(newSculpture));
                    sculptureResponseModels.add(sculptureToAdd);
                }
            }
        }

        ExhibitionResponseModel exhibitionResponseModel = exhibitionResponseMapper.entityToResponseModel(updatedExhibition);
        GalleryExhibitionPaintingSculptureResponseModel galleryExhibitionPaintingSculptureResponseModel = galleryExhibitionPaintingPainterResponseMapper.entityToResponseModel(gallery, paintingResponseModels, exhibitionResponseModel, sculptureResponseModels);
        return galleryExhibitionPaintingSculptureResponseModel;
    }*/

}
