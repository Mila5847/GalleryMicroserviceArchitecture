package com.gallery.apigateway.businesslayer;

import com.gallery.apigateway.domainclientlayer.PaintingServiceClient;
import com.gallery.apigateway.presentationlayer.*;
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

    @Override
    public PaintingPainterResponseModel getPaintingAggregateByIdInGallery(String galleryId, String paintingId) {
        return paintingServiceClient.getPaintingAggregateById(galleryId, paintingId);
    }

    @Override
    public PaintingsOfPainterResponseModel getPaintingsByPainterIdInGallery(String galleryId, String painterId) {
        return paintingServiceClient.getPaintingAggregateByPainterIdInGallery(galleryId, painterId);
    }

    @Override
    public PaintingPainterResponseModel addPaintingInGallery(String galleryId, PaintingRequestModel paintingRequestModel) {
        return paintingServiceClient.addPaintingInGallery(galleryId, paintingRequestModel);
    }

    @Override
    public PaintingPainterResponseModel addPainterToPaintingInGallery(String galleryId, String paintingId, PainterRequestModel painterResponseModel) {
        return paintingServiceClient.addPainterToPaintingInGallery(galleryId, paintingId, painterResponseModel);
    }

    @Override
    public void updatePaintingInGallery(String galleryId, String paintingId, PaintingRequestModel paintingRequestModel) {
        paintingServiceClient.updatePaintingInGallery(galleryId, paintingId, paintingRequestModel);
    }

    @Override
    public void removePaintingByIdInGallery(String galleryId, String paintingId) {
        paintingServiceClient.removePaintingByIdInGallery(galleryId, paintingId);
    }


}
