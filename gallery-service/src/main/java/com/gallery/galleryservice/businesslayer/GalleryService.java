package com.gallery.galleryservice.businesslayer;

import com.gallery.galleryservice.presentationlayer.GalleryRequestModel;
import com.gallery.galleryservice.presentationlayer.GalleryResponseModel;

import java.util.List;
import java.util.Map;

public interface GalleryService {
    List<GalleryResponseModel> getGalleries();
    GalleryResponseModel getGalleryById(String galleryId);

    GalleryResponseModel getGalleryByName(Map<String, String> queryParams);
    GalleryResponseModel addGallery(GalleryRequestModel galleryRequestModel);
    GalleryResponseModel updateGallery(GalleryRequestModel galleryRequestModel, String galleryId);

    //void removeAllGalleries();
    void removeGalleryById(String galleryId);

}
