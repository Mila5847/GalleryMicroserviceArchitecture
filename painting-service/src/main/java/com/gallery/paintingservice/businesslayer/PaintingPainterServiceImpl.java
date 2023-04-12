package com.gallery.paintingservice.businesslayer;

import com.gallery.paintingservice.datalayer.painter.Painter;
import com.gallery.paintingservice.datalayer.painter.PainterIdentifier;
import com.gallery.paintingservice.datalayer.painter.PainterRepository;
import com.gallery.paintingservice.datalayer.painting.GalleryIdentifier;
import com.gallery.paintingservice.datalayer.painting.Painting;
import com.gallery.paintingservice.datalayer.painting.PaintingRepository;
import com.gallery.paintingservice.datamapperlayer.*;
import com.gallery.paintingservice.presentationlayer.*;
import com.gallery.paintingservice.utils.exceptions.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaintingPainterServiceImpl implements PaintingPainterService {

    //GalleryRepository galleryRepository;
    PaintingRepository paintingRepository;
    PainterRepository painterRepository;
    //ExhibitionRepository exhibitionRepository;
    //ExhibitionRequestMapper exhibitionRequestMapper;
    PaintingResponseMapper paintingResponseMapper;
    PaintingRequestMapper paintingRequestMapper;
    PaintingPainterResponseMapper paintingPainterResponseMapper;
    PaintingsOfPainterResponseMapper paintingsOfPainterResponseMapper;
    PainterResponseMapper painterResponseMapper;
    PainterRequestMapper painterRequestMapper;

    public PaintingPainterServiceImpl(PaintingRepository paintingRepository, PainterRepository painterRepository, PaintingResponseMapper paintingResponseMapper, PaintingRequestMapper paintingRequestMapper, PaintingPainterResponseMapper paintingPainterResponseMapper, PaintingsOfPainterResponseMapper paintingsOfPainterResponseMapper, PainterResponseMapper painterResponseMapper, PainterRequestMapper painterRequestMapper) {
        this.paintingRepository = paintingRepository;
        this.painterRepository = painterRepository;
        this.paintingResponseMapper = paintingResponseMapper;
        this.paintingRequestMapper = paintingRequestMapper;
        this.paintingPainterResponseMapper = paintingPainterResponseMapper;
        this.paintingsOfPainterResponseMapper = paintingsOfPainterResponseMapper;
        this.painterResponseMapper = painterResponseMapper;
        this.painterRequestMapper = painterRequestMapper;
    }

    @Override
    public List<PaintingResponseModel> getPaintingsInGallery(String galleryId) {
       /* if (!galleryRepository.existsByGalleryIdentifier_GalleryId(galleryId)) {
            throw new ExistingGalleryNotFoundException("Gallery with id: " + galleryId + " does not exist.");
        }*/
        return paintingResponseMapper.entityListToResponseModelList(paintingRepository.findAllByGalleryIdentifier_GalleryId(galleryId));
    }

    @Override
    public PaintingPainterResponseModel getPaintingByIdInGallery(String galleryId, String paintingId) {
        /*if (!galleryRepository.existsByGalleryIdentifier_GalleryId(galleryId)) {
            throw new ExistingGalleryNotFoundException("Gallery with id: " + galleryId + " does not exist.");
        }*/

        Painting painting = paintingRepository.findByGalleryIdentifier_GalleryIdAndPaintingIdentifier_PaintingId(galleryId, paintingId);
        if(painting == null){
            throw new ExistingPaintingNotFoundException("Painting with id: " + paintingId + " does not exist.");
        }
        String painterId = painting.getPainterIdentifier().getPainterId();
        Painter painter = painterRepository.findByPainterIdentifier_PainterId(painterId);
        return paintingPainterResponseMapper.entityToResponseModel(paintingResponseMapper.entityToResponseModel(painting), painterResponseMapper.entityToResponseModel(painter));
    }

    @Override
    public PaintingsOfPainterResponseModel getPaintingsByPainterIdInGallery(String galleryId, String painterId) {
        /*if (!galleryRepository.existsByGalleryIdentifier_GalleryId(galleryId)) {
            throw new ExistingGalleryNotFoundException("Gallery with id: " + galleryId + " does not exist.");
        }*/
        Painter painter = painterRepository.findByPainterIdentifier_PainterId(painterId);
        if(painter == null){
            throw new ExistingPainterNotFoundException("Painter with id " + painterId + " does not exist.");
        }
        List<Painting> paintings = paintingRepository.findAllByGalleryIdentifier_GalleryIdAndPainterIdentifier_PainterId(galleryId, painterId);
        return paintingsOfPainterResponseMapper.entitiesToResponseModel(painter, painterResponseMapper.entityToResponseModel(painter), paintingResponseMapper.entityListToResponseModelList(paintings));
    }

    @Override
    public PaintingPainterResponseModel addPaintingToGallery(String galleryId, PaintingRequestModel paintingRequestModel) {
        /*Gallery gallery = galleryRepository.findByGalleryIdentifier_GalleryId(galleryId);
        if(gallery == null){
            throw new ExistingGalleryNotFoundException("Gallery with id: " + galleryId + " does not exist.");
        }*/
        GalleryIdentifier galleryIdentifier = new GalleryIdentifier(galleryId);
        PainterIdentifier painterIdentifier = new PainterIdentifier();
        painterIdentifier.setPainterId("");
        Painter painter = painterRepository.findByPainterIdentifier_PainterId(painterIdentifier.getPainterId());
        /*ExhibitionIdentifier exhibitionIdentifier = new ExhibitionIdentifier();
        exhibitionIdentifier.setExhibitionId("");*/
        Painting painting = paintingRequestMapper.requestModelToEntity(paintingRequestModel,painterIdentifier, galleryIdentifier);
        if(painting.getTitle().length() < 1) {
            throw new MinimumTitleLengthForPaintingNameException("The tile of the painting should be at least 1 character.");
        }
        PaintingResponseModel paintingToAdd = paintingResponseMapper.entityToResponseModel(paintingRepository.save(painting));
        PainterResponseModel painterOfPainting = painterResponseMapper.entityToResponseModel(painter);
        return paintingPainterResponseMapper.entityToResponseModel(paintingToAdd, painterOfPainting);
    }

    @Override
    public PaintingPainterResponseModel updatePaintingInGallery(String galleryId, String paintingId, PaintingRequestModel paintingRequestModel) {
        /*if (!galleryRepository.existsByGalleryIdentifier_GalleryId(galleryId)) {
            throw new ExistingGalleryNotFoundException("Gallery with id: " + galleryId + " does not exist.");
        }*/

        Painting existingPainting = paintingRepository.findByGalleryIdentifier_GalleryIdAndPaintingIdentifier_PaintingId(galleryId, paintingId);
        if(existingPainting == null){
            throw new ExistingPaintingNotFoundException("Painting with id: " + paintingId + " does not exist.");
        }
        Painting paintingToBeUpdated = paintingRequestMapper.requestModelToEntity(paintingRequestModel, existingPainting.getPainterIdentifier(), existingPainting.getGalleryIdentifier());
        paintingToBeUpdated.setId(existingPainting.getId());
        paintingToBeUpdated.setPaintingIdentifier(existingPainting.getPaintingIdentifier()); // set the original painting identifier
        paintingToBeUpdated.setPainterIdentifier(existingPainting.getPainterIdentifier());

        PaintingResponseModel updatedPainting = paintingResponseMapper.entityToResponseModel(paintingRepository.save(paintingToBeUpdated));
        Painter painter = painterRepository.findByPainterIdentifier_PainterId(updatedPainting.getPainterId());
        PainterResponseModel painterOfPainting = painterResponseMapper.entityToResponseModel(painter);
        return paintingPainterResponseMapper.entityToResponseModel(updatedPainting, painterOfPainting);
    }

    @Override
    public void removePaintingByIdInGallery(String galleryId, String paintingId) {
        /*if(!galleryRepository.existsByGalleryIdentifier_GalleryId(galleryId)){
            throw new ExistingGalleryNotFoundException("Gallery with id: " + galleryId + " does not exist.");
        }*/
        Painting painting = paintingRepository.findByPaintingIdentifier_PaintingId(paintingId);
        if(painting == null){
            throw new ExistingPaintingNotFoundException("Painting with id: " + paintingId + " does not exist.");
        }
        paintingRepository.delete(painting);
        String painterId = painting.getPainterIdentifier().getPainterId();
        List<Painting> paintings = paintingRepository.findAllByGalleryIdentifier_GalleryIdAndPainterIdentifier_PainterId(galleryId, painterId);
        if(paintings.isEmpty()){
            painterRepository.delete(painterRepository.findByPainterIdentifier_PainterId(painterId));
        }
    }

    /*@Override
    public void removeAllPaintingsInGallery(String galleryId) {
        painterRepository.deleteAll();
        paintingRepository.deleteAll();
    }*/

    @Override
    public PaintingPainterResponseModel addPainterToPaintingInGallery(String galleryId, String paintingId, PainterRequestModel painterRequestModel) {
       /* Gallery gallery = galleryRepository.findByGalleryIdentifier_GalleryId(galleryId);
        if (gallery == null) {
            throw new ExistingGalleryNotFoundException("Gallery with id: " + galleryId + " does not exist.");
        }*/
        Painting painting = paintingRepository.findByPaintingIdentifier_PaintingId(paintingId);
        if(painting == null){
            throw new ExistingPaintingNotFoundException("Painting with id: " + paintingId + " does not exist.");
        }
        PainterIdentifier painterIdentifier = new PainterIdentifier();
        Painter painter = painterRequestMapper.requestModelToEntity(painterRequestModel, painterIdentifier);
        painting.setPainterIdentifier(painter.getPainterIdentifier());
        Painter painterToBeAdded = painterRepository.save(painter);
        PaintingResponseModel paintingResponseModel = paintingResponseMapper.entityToResponseModel(painting);
        PainterResponseModel painterResponseModel = painterResponseMapper.entityToResponseModel(painterToBeAdded);
        return  paintingPainterResponseMapper.entityToResponseModel(paintingResponseModel, painterResponseModel);
    }

    @Override
    public PaintingPainterResponseModel updatePainterOfPaintingInGallery(String galleryId, String paintingId, String painterId, PainterRequestModel painterRequestModel) {
        /*Gallery gallery = galleryRepository.findByGalleryIdentifier_GalleryId(galleryId);
        if (gallery == null) {
            throw new ExistingGalleryNotFoundException("Gallery with id: " + galleryId + " does not exist.");
        }*/
        Painting existingPainting = paintingRepository.findByPaintingIdentifier_PaintingId(paintingId);
        if(existingPainting == null){
            throw new ExistingPaintingNotFoundException("Painting with id: " + paintingId + " does not exist.");
        }
        Painter existingPainter = painterRepository.findByPainterIdentifier_PainterId(painterId);
        if(existingPainter == null){
            throw new ExistingPainterNotFoundException("Painter with id " + painterId + " does not exist.");
        }

        Painter painter = painterRequestMapper.requestModelToEntity(painterRequestModel, existingPainting.getPainterIdentifier());
        painter.setId(existingPainter.getId());
        Painter painterToBeUpdated = painterRepository.save(painter);
        PainterResponseModel painterResponseModel = painterResponseMapper.entityToResponseModel(painterToBeUpdated);
        Painting painting = paintingRepository.findByPaintingIdentifier_PaintingId(paintingId);
        PaintingResponseModel paintingResponseModel = paintingResponseMapper.entityToResponseModel(painting);
        return  paintingPainterResponseMapper.entityToResponseModel(paintingResponseModel, painterResponseModel);
    }

    @Override
    public void removePainterOfPaintingInGallery(String galleryId, String paintingId, String painterId) {
        /*if (!galleryRepository.existsByGalleryIdentifier_GalleryId(galleryId)) {
            throw new ExistingGalleryNotFoundException("Gallery with id: " + galleryId + " does not exist.");
        }*/
        Painting existingPainting = paintingRepository.findByPaintingIdentifier_PaintingId(paintingId);
        if(existingPainting == null){
            throw new ExistingPaintingNotFoundException("Painting with id: " + paintingId + " does not exist.");
        }
        Painter painter = painterRepository.findByPainterIdentifier_PainterId(painterId);
        if(painter == null) {
            throw new ExistingPainterNotFoundException("Painter with id " + painterId + " does not exist.");
        }
        PainterIdentifier painterIdentifier = new PainterIdentifier();
        painterIdentifier.setPainterId("");
        existingPainting.setPainterIdentifier(painterIdentifier);
        paintingRepository.save(existingPainting);
    }

    /*@Override
    public List<PaintingResponseModel> getPaintingsOfExhibitionInGallery(String galleryId, String exhibitionId) {
       if (!galleryRepository.existsByGalleryIdentifier_GalleryId(galleryId)) {
            throw new ExistingGalleryNotFoundException("Gallery with id: " + galleryId + " does not exist.");
        }
        if(!exhibitionRepository.existsByExhibitionIdentifier_ExhibitionId(exhibitionId)){
            throw new ExistingExhibitionNotFoundException("Exhibition with id: " + exhibitionId + " does not exist");
        }
        List<Painting> paintings = paintingRepository.findAllByGalleryIdentifier_GalleryIdAndExhibitionIdentifier_ExhibitionId(galleryId, exhibitionId);
        return  paintingResponseMapper.entityListToResponseModelList(paintings);
    }*/

   /*@Override
    public PaintingPainterResponseModel addPaintingToExhibitionInGallery(String galleryId, String paintingId, String exhibitionId) {
       Gallery gallery = galleryRepository.findByGalleryIdentifier_GalleryId(galleryId);
       if (gallery == null) {
           throw new ExistingGalleryNotFoundException("Gallery with id: " + galleryId + " does not exist.");
       }
       Painting existingPainting = paintingRepository.findByPaintingIdentifier_PaintingId(paintingId);
       if(existingPainting == null){
           throw new ExistingPaintingNotFoundException("Painting with id: " + paintingId + " does not exist.");
       }
       Exhibition existingExhibition = exhibitionRepository.findByGalleryIdentifier_GalleryIdAndExhibitionIdentifier_ExhibitionId(galleryId, exhibitionId);
       if(existingExhibition == null){
           throw new ExistingExhibitionNotFoundException("Exhibition with id: " + exhibitionId + " does not exist");
       }

       existingPainting.setExhibitionIdentifier(existingExhibition.getExhibitionIdentifier());
       Painting paintingInExhibition = paintingRepository.save(existingPainting);
       PaintingResponseModel painting = paintingResponseMapper.entityToResponseModel(paintingInExhibition);
       Painter painter = painterRepository.findByPainterIdentifier_PainterId(painting.getPainterId());
       PainterResponseModel painterOfPainting = painterResponseMapper.entityToResponseModel(painter);
       return paintingPainterResponseMapper.entityToResponseModel(painting, painterOfPainting);
    }*/

    /*@Override
    public void removePaintingFromExhibitionInGallery(String galleryId, String paintingId, String exhibitionId) {
        Gallery gallery = galleryRepository.findByGalleryIdentifier_GalleryId(galleryId);
        if (gallery == null) {
            throw new ExistingGalleryNotFoundException("Gallery with id: " + galleryId + " does not exist.");
        }
        Painting painting = paintingRepository.findByPaintingIdentifier_PaintingId(paintingId);
        if(painting == null){
            throw new ExistingPaintingNotFoundException("Painting with id: " + paintingId + " does not exist.");
        }
        Exhibition exhibition = exhibitionRepository.findByGalleryIdentifier_GalleryIdAndExhibitionIdentifier_ExhibitionId(galleryId, exhibitionId);
        if(exhibition == null){
            throw new ExistingExhibitionNotFoundException("Exhibition with id: " + exhibitionId + " does not exist");
        }
        ExhibitionIdentifier exhibitionIdentifier = new ExhibitionIdentifier();
        exhibitionIdentifier.setExhibitionId("");
        painting.setExhibitionIdentifier(exhibitionIdentifier);
        paintingRepository.save(painting);
    }*/

}