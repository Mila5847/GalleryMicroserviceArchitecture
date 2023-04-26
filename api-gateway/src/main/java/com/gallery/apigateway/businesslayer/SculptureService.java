package com.gallery.apigateway.businesslayer;

import com.gallery.apigateway.presentationlayer.GalleryRequestModel;
import com.gallery.apigateway.presentationlayer.GalleryResponseModel;
import com.gallery.apigateway.presentationlayer.SculptureRequestModel;
import com.gallery.apigateway.presentationlayer.SculptureResponseModel;

public interface SculptureService {

    SculptureResponseModel getSculptureByIdInGallery(String galleryId, String sculptureId);
    SculptureResponseModel[] getAllSculpturesInGallery(String galleryId);
    SculptureResponseModel addSculptureToGallery(String galleryId, SculptureRequestModel sculptureRequestModel);
    void updateSculptureInGallery(String galleryId, String sculptureId, SculptureRequestModel sculptureRequestModel);
    void deleteSculptureInGallery(String galleryId, String sculptureId);
}
