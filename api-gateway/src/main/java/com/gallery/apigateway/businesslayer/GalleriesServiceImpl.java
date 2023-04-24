package com.gallery.apigateway.businesslayer;

import com.gallery.apigateway.domainclientlayer.GalleryServiceClient;
import com.gallery.apigateway.presentationlayer.GalleryRequestModel;
import com.gallery.apigateway.presentationlayer.GalleryResponseModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GalleriesServiceImpl implements GalleriesService{

    private GalleryServiceClient galleryServiceClient;

    public GalleriesServiceImpl(GalleryServiceClient galleryServiceClient) {
        this.galleryServiceClient = galleryServiceClient;
    }

    @Override
    public GalleryResponseModel getGallery(String galleryId) {
        return galleryServiceClient.getGallery(galleryId);
    }

    @Override
    public GalleryResponseModel[] getAllGalleries() {
        return galleryServiceClient.getAllGalleries();
    }

    @Override
    public GalleryResponseModel addGallery(GalleryRequestModel galleryRequestModel) {
        return galleryServiceClient.addGallery(galleryRequestModel);
    }

    @Override
    public void updateGallery(String galleryId, GalleryRequestModel galleryRequestModel) {
        galleryServiceClient.updateGallery(galleryId, galleryRequestModel);
    }

    @Override
    public void deleteGallery(String galleryId) {
        galleryServiceClient.deleteGallery(galleryId);
    }
}
