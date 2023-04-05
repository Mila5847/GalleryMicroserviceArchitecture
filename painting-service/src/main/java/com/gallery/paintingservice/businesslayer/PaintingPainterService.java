package com.gallery.paintingservice.businesslayer;

import com.gallery.paintingservice.presentationlayer.*;

import java.util.List;

public interface PaintingPainterService {
    List<PaintingResponseModel> getPaintingsInGallery(String galleryId);
    PaintingPainterResponseModel getPaintingByIdInGallery(String galleryId, String paintingId);
    PaintingsOfPainterResponseModel getPaintingsByPainterIdInGallery(String galleryId, String painterId);
   // List<PaintingResponseModel> getPaintingsOfExhibitionInGallery(String galleryId, String exhibitionId);
    PaintingPainterResponseModel addPaintingToGallery(String galleryId, PaintingRequestModel paintingRequestModel);
    PaintingPainterResponseModel updatePaintingInGallery(String galleryId, String paintingId, PaintingRequestModel paintingRequestModel);
    void removePaintingByIdInGallery(String galleryId, String paintingId);
    void removeAllPaintingsInGallery(String galleryId);
    PaintingPainterResponseModel addPainterToPaintingInGallery(String galleryId, String paintingId, PainterRequestModel painterResponseModel);
    PaintingPainterResponseModel updatePainterOfPaintingInGallery(String galleryId, String paintingId, String painterId, PainterRequestModel painterRequestModel);
    void removePainterOfPaintingInGallery(String galleryId, String paintingId, String painterId);
    /*PaintingPainterResponseModel addPaintingToExhibitionInGallery(String galleryId, String paintingId, String exhibitionId);
    void removePaintingFromExhibitionInGallery(String galleryId, String paintingId, String exhibitionId);*/
}
