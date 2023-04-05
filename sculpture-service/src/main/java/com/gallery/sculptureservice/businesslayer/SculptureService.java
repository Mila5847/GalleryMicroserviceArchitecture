package com.gallery.sculptureservice.businesslayer;

import com.gallery.sculptureservice.presentationlayer.SculptureRequestModel;
import com.gallery.sculptureservice.presentationlayer.SculptureResponseModel;

import java.util.List;

public interface SculptureService {
    List<SculptureResponseModel> getSculpturesInGallery(String galleryId);
    SculptureResponseModel getSculptureByIdInGallery(String galleryId, String sculptureId);
    //List<SculptureResponseModel> getSculpturesOfExhibitionInGallery(String galleryId, String exhibitionId);
    SculptureResponseModel addSculptureToGallery(String galleryId, SculptureRequestModel sculptureRequestModel);
    SculptureResponseModel updateSculptureInGallery(String galleryId, String sculptureId, SculptureRequestModel sculptureRequestModel);
    //void removeSculptureFromExhibitionInGallery(String galleryId, String sculptureId, String exhibitionId);
    void removeSculptureByIdInGallery(String galleryId, String sculptureId);
}