package com.gallery.apigateway.businesslayer;

import com.gallery.apigateway.presentationlayer.GalleryRequestModel;
import com.gallery.apigateway.presentationlayer.GalleryResponseModel;

public interface GalleriesService {
    GalleryResponseModel getGallery(String galleryId);
    GalleryResponseModel[] getAllGalleries();

    GalleryResponseModel addGallery(GalleryRequestModel galleryRequestModel);
    void updateGallery(String galleryId, GalleryRequestModel galleryRequestModel);
    void deleteGallery(String galleryId);

}
