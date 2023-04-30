package com.gallery.apigateway.businesslayer;

import com.gallery.apigateway.domainclientlayer.SculptureServiceClient;
import com.gallery.apigateway.presentationlayer.SculptureRequestModel;
import com.gallery.apigateway.presentationlayer.SculptureResponseModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SculptureServiceImpl implements SculptureService{

    private SculptureServiceClient sculptureServiceClient;

    public SculptureServiceImpl(SculptureServiceClient sculptureServiceClient) {
        this.sculptureServiceClient = sculptureServiceClient;
    }

    @Override
    public SculptureResponseModel getSculptureByIdInGallery(String galleryId, String sculptureId) {
        return sculptureServiceClient.getSculptureById(galleryId, sculptureId);
    }

    @Override
    public SculptureResponseModel[] getAllSculpturesInGallery(String galleryId) {
        return sculptureServiceClient.getAllSculpturesInGallery(galleryId);
    }

    @Override
    public SculptureResponseModel addSculptureToGallery(String galleryId, SculptureRequestModel sculptureRequestModel) {
        return sculptureServiceClient.addSculptureInGallery(galleryId, sculptureRequestModel);
    }

    @Override
    public void updateSculptureInGallery(String galleryId, String sculptureId, SculptureRequestModel sculptureRequestModel) {
        sculptureServiceClient.updateSculptureInGallery(galleryId, sculptureId, sculptureRequestModel);
    }

    @Override
    public void deleteSculptureInGallery(String galleryId, String sculptureId) {
        sculptureServiceClient.deleteSculpture(galleryId, sculptureId);
    }
}
