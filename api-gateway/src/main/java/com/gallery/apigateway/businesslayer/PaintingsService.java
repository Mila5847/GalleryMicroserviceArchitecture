package com.gallery.apigateway.businesslayer;

import com.gallery.apigateway.presentationlayer.*;

public interface PaintingsService {
    PaintingResponseModel[] getPaintingsInGallery(String galleryId);
    PaintingPainterResponseModel getPaintingAggregateByIdInGallery(String galleryId, String paintingId);
    PaintingsOfPainterResponseModel getPaintingsByPainterIdInGallery(String galleryId, String painterId);
    PaintingPainterResponseModel addPaintingInGallery(String galleryId, PaintingRequestModel paintingRequestModel);
    PaintingPainterResponseModel addPainterToPaintingInGallery(String galleryId, String paintingId, PainterRequestModel painterResponseModel);
    void updatePaintingInGallery(String galleryId, String paintingId, PaintingRequestModel paintingRequestModel);
    void removePaintingByIdInGallery(String galleryId, String paintingId);



}
