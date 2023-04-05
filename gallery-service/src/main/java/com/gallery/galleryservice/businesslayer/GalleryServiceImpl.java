package com.gallery.galleryservice.businesslayer;

import com.gallery.galleryservice.datalayer.Address;
import com.gallery.galleryservice.datalayer.Gallery;
import com.gallery.galleryservice.datalayer.GalleryRepository;
import com.gallery.galleryservice.datamapperlayer.GalleryRequestMapper;
import com.gallery.galleryservice.datamapperlayer.GalleryResponseMapper;
import com.gallery.galleryservice.presentationlayer.GalleryRequestModel;
import com.gallery.galleryservice.presentationlayer.GalleryResponseModel;
import com.gallery.galleryservice.utils.exceptions.DuplicateGalleryAddressException;
import com.gallery.galleryservice.utils.exceptions.ExistingGalleryNotFoundException;
import com.gallery.galleryservice.utils.exceptions.InvalidInputException;
import com.gallery.galleryservice.utils.exceptions.NameMissingFromQueryParamsException;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class GalleryServiceImpl implements GalleryService{

    private GalleryRepository galleryRepository;
    private GalleryResponseMapper galleryResponseMapper;
    private GalleryRequestMapper galleryRequestMapper;
   /* private PaintingRepository paintingRepository;

    private ExhibitionRepository exhibitionRepository;*/

    public GalleryServiceImpl(GalleryRepository galleryRepository, GalleryResponseMapper galleryResponseMapper, GalleryRequestMapper galleryRequestMapper) {
        this.galleryRepository = galleryRepository;
        this.galleryResponseMapper = galleryResponseMapper;
        this.galleryRequestMapper = galleryRequestMapper;
    }

    @Override
    public List<GalleryResponseModel> getGalleries() {
        return galleryResponseMapper.entityListToResponseModelList(galleryRepository.findAll());
    }

    @Override
    public GalleryResponseModel getGalleryById(String galleryId) {
        Gallery gallery = galleryRepository.findByGalleryIdentifier_GalleryId(galleryId);
        if(gallery == null){
            throw new ExistingGalleryNotFoundException("Gallery with id " + galleryId + " not found.");
        }
        return galleryResponseMapper.entityToResponseModel(gallery);
    }

    @Override
    public GalleryResponseModel getGalleryByName(Map<String, String> queryParams) {
        String galleryName = queryParams.get("name");
        if(galleryName == null) {
            throw new NameMissingFromQueryParamsException("Name param missing from query param");
        }
        Gallery gallery = galleryRepository.findByName(galleryName);
        if(gallery == null){
            throw new ExistingGalleryNotFoundException("Gallery with name " + galleryName + " does not exist.");
        }
        return galleryResponseMapper.entityToResponseModel(gallery);
    }

    @Override
    public GalleryResponseModel addGallery(GalleryRequestModel galleryRequestModel) {
        Gallery gallery = galleryRequestMapper.requestModelToEntity(galleryRequestModel);
        Address address = new Address(galleryRequestModel.getStreetAddress(), galleryRequestModel.getCity(),
                galleryRequestModel.getProvince(), galleryRequestModel.getCountry(), galleryRequestModel.getPostalCode());
        gallery.setAddress(address);
        if(galleryRepository.findByAddress(address) != null){
            throw new DuplicateGalleryAddressException("There is already a gallery with this address");
        }
        Gallery galleryToBeAdded = galleryRepository.save(gallery);
        GalleryResponseModel galleryResponse = galleryResponseMapper.entityToResponseModel(galleryToBeAdded);
        return galleryResponse;
    }

    @Override
    public GalleryResponseModel updateGallery(GalleryRequestModel galleryRequestModel, String galleryId) {
        Gallery gallery = galleryRequestMapper.requestModelToEntity(galleryRequestModel);
        Gallery existingGallery = galleryRepository.findByGalleryIdentifier_GalleryId(galleryId);
        if(existingGallery == null){
            throw new ExistingGalleryNotFoundException("Gallery with id: " + galleryId + " does not exist.");
        }
        gallery.setId(existingGallery.getId());
        gallery.setGalleryIdentifier(existingGallery.getGalleryIdentifier());
        Address address = new Address(galleryRequestModel.getStreetAddress(), galleryRequestModel.getCity(),
                galleryRequestModel.getProvince(), galleryRequestModel.getCountry(), galleryRequestModel.getPostalCode());
        gallery.setAddress(address);
        try{
            Gallery galleryToBeUpdated = galleryRepository.save(gallery);
            GalleryResponseModel galleryResponse = galleryResponseMapper.entityToResponseModel(galleryToBeUpdated);
            return galleryResponse;
        }catch(DataAccessException ex){
            throw new InvalidInputException("Could not update gallery.");
        }
    }

    @Override
    public void removeAllGalleries() {
        galleryRepository.deleteAll();
    }

    @Override
    public void removeGalleryById(String galleryId) {
        Gallery existingGallery = galleryRepository.findByGalleryIdentifier_GalleryId(galleryId);
        if(existingGallery == null){
            throw new ExistingGalleryNotFoundException("Gallery with id: " + galleryId + " does not exist.");
        }

        /*List<Painting> paintings = paintingRepository.findAllByGalleryIdentifier_GalleryId(galleryId);

        List<Exhibition> exhibitions = exhibitionRepository.findAllByGalleryIdentifier_GalleryId(galleryId);
        exhibitions.forEach(exhibition -> exhibitionRepository.delete(exhibition));*/

        galleryRepository.delete(existingGallery);
    }
}
