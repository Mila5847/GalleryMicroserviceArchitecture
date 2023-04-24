package com.gallery.apigateway.businesslayer;

import com.gallery.apigateway.domainclientlayer.PaintingServiceClient;
import com.gallery.apigateway.presentationlayer.PaintingPainterResponseModel;
import com.gallery.apigateway.presentationlayer.PaintingResponseModel;
import com.gallery.apigateway.presentationlayer.PaintingsOfPainterResponseModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PaintingsServiceImpl implements PaintingsService{

    private PaintingServiceClient paintingServiceClient;

    public PaintingsServiceImpl(PaintingServiceClient paintingServiceClient) {
        this.paintingServiceClient = paintingServiceClient;
    }

    @Override
    public PaintingResponseModel[] getPaintingsInGallery(String galleryId) {
        return paintingServiceClient.getPaintingsInGallery(galleryId);
    }

    /*@Override
    public PaintingPainterResponseModel getPaintingByIdInGallery(String galleryId, String paintingId) {
        return null;
    }

    @Override
    public PaintingsOfPainterResponseModel getPaintingsByPainterIdInGallery(String galleryId, String painterId) {
        return null;
    }*/
}
