package com.gallery.sculptureservice.businesslayer;

import com.gallery.sculptureservice.datalayer.GalleryIdentifier;
import com.gallery.sculptureservice.datalayer.Sculpture;
import com.gallery.sculptureservice.datalayer.SculptureRepository;
import com.gallery.sculptureservice.datamapperlayer.SculptureRequestMapper;
import com.gallery.sculptureservice.datamapperlayer.SculptureResponseMapper;
import com.gallery.sculptureservice.presentationlayer.SculptureRequestModel;
import com.gallery.sculptureservice.presentationlayer.SculptureResponseModel;
import com.gallery.sculptureservice.utils.exceptions.ExistingSculptureNotFoundException;
import com.gallery.sculptureservice.utils.exceptions.DuplicateTitleSculptureException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SculptureServiceImpl implements SculptureService {

    SculptureResponseMapper sculptureResponseMapper;
    SculptureRequestMapper sculptureRequestMapper;
    SculptureRepository sculptureRepository;

    public SculptureServiceImpl(SculptureResponseMapper sculptureResponseMapper, SculptureRequestMapper sculptureRequestMapper, SculptureRepository sculptureRepository) {
        this.sculptureResponseMapper = sculptureResponseMapper;
        this.sculptureRequestMapper = sculptureRequestMapper;
        this.sculptureRepository = sculptureRepository;
    }

    @Override
    public List<SculptureResponseModel> getSculpturesInGallery(String galleryId) {
        return sculptureResponseMapper.entityListToResponseModelList(sculptureRepository.findAllByGalleryIdentifier_GalleryId(galleryId));
    }

    @Override
    public SculptureResponseModel getSculptureByIdInGallery(String galleryId, String sculptureId) {
        Sculpture sculpture = sculptureRepository.findByGalleryIdentifier_GalleryIdAndSculptureIdentifier_SculptureId(galleryId, sculptureId);
        if(!sculptureRepository.existsBySculptureIdentifier_SculptureId(sculptureId)){
            throw new ExistingSculptureNotFoundException("The sculpture with id " + sculptureId + " does not exist.");
        }
        return sculptureResponseMapper.entityToResponseModel(sculpture);
    }

    /*@Override
    public List<SculptureResponseModel> getSculpturesOfExhibitionInGallery(String galleryId, String exhibitionId) {
        List<Sculpture> sculptures = sculptureRepository.findAllByGalleryIdentifier_GalleryIdAndExhibitionIdentifier_ExhibitionId(galleryId, exhibitionId);
        return sculptureResponseMapper.entityListToResponseModelList(sculptures);
    }*/

    @Override
    public SculptureResponseModel addSculptureToGallery(String galleryId, SculptureRequestModel sculptureRequestModel) {
        GalleryIdentifier galleryIdentifier = new GalleryIdentifier(galleryId);
        Sculpture sculpture = sculptureRequestMapper.requestModelToEntity(sculptureRequestModel, galleryIdentifier);
        if(sculptureRepository.findByTitle(sculpture.getTitle()) != null){
            throw new DuplicateTitleSculptureException("A sculpture with the title " + sculpture.getTitle() + " already exists.");
        }
        return sculptureResponseMapper.entityToResponseModel(sculptureRepository.save(sculpture));
    }

    @Override
    public SculptureResponseModel updateSculptureInGallery(String galleryId, String sculptureId, SculptureRequestModel sculptureRequestModel) {
        Sculpture existingSculpture = sculptureRepository.findByGalleryIdentifier_GalleryIdAndSculptureIdentifier_SculptureId(galleryId, sculptureId);
        if(existingSculpture == null){
            throw new ExistingSculptureNotFoundException("Sculpture with id: " + sculptureId + " does not exist.");
        }
        Sculpture sculptureToBeUpdated = sculptureRequestMapper.requestModelToEntity(sculptureRequestModel, existingSculpture.getGalleryIdentifier());
        sculptureToBeUpdated.setId(existingSculpture.getId());
        sculptureToBeUpdated.setSculptureIdentifier(existingSculpture.getSculptureIdentifier());
        return sculptureResponseMapper.entityToResponseModel(sculptureRepository.save(sculptureToBeUpdated));
    }

    @Override
    public void removeSculptureByIdInGallery(String galleryId, String sculptureId) {
        Sculpture sculpture = sculptureRepository.findByGalleryIdentifier_GalleryIdAndSculptureIdentifier_SculptureId(galleryId, sculptureId);
        if(sculpture == null){
            throw new ExistingSculptureNotFoundException("Sculpture with id: " + sculptureId + " does not exist.");
        }
        sculptureRepository.delete(sculpture);
    }

    /*@Override
    public void removeSculptureFromExhibitionInGallery(String galleryId, String sculptureId, String exhibitionId) {

    }*/
}
