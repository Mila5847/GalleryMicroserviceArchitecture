package com.gallery.apigateway.businesslayer;

import com.gallery.apigateway.presentationlayer.PaintingPainterResponseModel;
import com.gallery.apigateway.presentationlayer.PaintingResponseModel;
import com.gallery.apigateway.presentationlayer.PaintingsOfPainterResponseModel;

public interface PaintingsService {
    PaintingResponseModel[] getPaintingsInGallery(String galleryId);
    //PaintingPainterResponseModel getPaintingByIdInGallery(String galleryId, String paintingId);
    //PaintingsOfPainterResponseModel getPaintingsByPainterIdInGallery(String galleryId, String painterId);
}
