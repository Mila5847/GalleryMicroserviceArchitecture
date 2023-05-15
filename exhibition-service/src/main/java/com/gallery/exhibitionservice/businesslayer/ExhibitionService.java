package com.gallery.exhibitionservice.businesslayer;

import com.gallery.exhibitionservice.presentationlayer.ExhibitionRequestModel;
import com.gallery.exhibitionservice.presentationlayer.ExhibitionResponseModel;

import java.util.List;

public interface ExhibitionService {

    List<ExhibitionResponseModel> getAllExhibitions();

    ExhibitionResponseModel getExhibitionById(String exhibitionId);
    ExhibitionResponseModel createExhibition(String galleryId, ExhibitionRequestModel exhibitionRequestModel);
    ExhibitionResponseModel updateExhibition(String exhibitionId, ExhibitionRequestModel exhibitionRequestModel);
    void removeExhibition(String exhibitionId);

    void removeAllExhibitions();

    /*GalleryExhibitionPaintingSculptureResponseModel getExhibitionByIdInGallery(String galleryId, String exhibitionId);
    GalleryExhibitionPaintingSculptureResponseModel getExhibitionByField(String galleryId, Map<String, String> queryParams);
   // GalleryExhibitionPaintingResponseModel addExhibitionToGallery(String galleryId, ExhibitionRequestModel exhibitionRequestModel);
   // GalleryExhibitionPaintingResponseModel updateExhibitionInGallery(String galleryId, String exhibitionId, ExhibitionRequestModel exhibitionRequestModel);
    void removeExhibitionFromGallery(String galleryId, String exhibitionId);
    void removeAllExhibitionsFromGallery(String galleryId);
    void removeExhibitionByField(String galleryId, Map<String, String> queryParams);

    GalleryExhibitionPaintingSculptureResponseModel addExhibitionInGallery(String galleryId, GalleryExhibitionPaintingSculptureRequestModel galleryExhibitionPaintingRequestModel);

    GalleryExhibitionPaintingSculptureResponseModel updateExhibitionInGallery(String galleryId, String exhibitionId, GalleryExhibitionPaintingSculptureRequestModel galleryExhibitionPaintingRequestModel);*/
}
