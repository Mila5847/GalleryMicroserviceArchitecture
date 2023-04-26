package com.gallery.apigateway.businesslayer;

import com.gallery.apigateway.domainclientlayer.SculpturesServiceClient;
import com.gallery.apigateway.presentationlayer.SculptureRequestModel;
import com.gallery.apigateway.presentationlayer.SculptureResponseModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SculptureServiceImpl implements SculptureService{

    private SculpturesServiceClient sculpturesServiceClient;

    public SculptureServiceImpl(SculpturesServiceClient sculpturesServiceClient) {
        this.sculpturesServiceClient = sculpturesServiceClient;
    }

    @Override
    public SculptureResponseModel getSculptureByIdInGallery(String galleryId, String sculptureId) {
        return sculpturesServiceClient.getSculptureById(galleryId, sculptureId);
    }

    @Override
    public SculptureResponseModel[] getAllSculpturesInGallery(String galleryId) {
        return sculpturesServiceClient.getAllSculpturesInGallery(galleryId);
    }

    @Override
    public SculptureResponseModel addSculptureToGallery(String galleryId, SculptureRequestModel sculptureRequestModel) {
        return sculpturesServiceClient.addSculptureInGallery(galleryId, sculptureRequestModel);
    }

    @Override
    public void updateSculptureInGallery(String galleryId, String sculptureId, SculptureRequestModel sculptureRequestModel) {
        sculpturesServiceClient.updateSculptureInGallery(galleryId, sculptureId, sculptureRequestModel);
    }

    @Override
    public void deleteSculptureInGallery(String galleryId, String sculptureId) {
        sculpturesServiceClient.deleteSculpture(galleryId, sculptureId);
    }
}
