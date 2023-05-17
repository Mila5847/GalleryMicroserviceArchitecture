package com.gallery.apigateway.businesslayer.exhibition;

import com.gallery.apigateway.domainclientlayer.exhibition.ExhibitionServiceClient;
import com.gallery.apigateway.presentationlayer.exhibition.ExhibitionRequestModel;
import com.gallery.apigateway.presentationlayer.exhibition.ExhibitionResponseModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ExhibitionServiceImpl implements ExhibitionService{

    ExhibitionServiceClient exhibitionServiceClient;

    public ExhibitionServiceImpl(ExhibitionServiceClient exhibitionServiceClient) {
        this.exhibitionServiceClient = exhibitionServiceClient;
    }

    @Override
    public ExhibitionResponseModel[] getAllExhibitions() {
        return exhibitionServiceClient.getAllExhibitions();
    }

    @Override
    public ExhibitionResponseModel getExhibitionById(String exhibitionId) {
        return exhibitionServiceClient.getExhibition(exhibitionId);
    }

    @Override
    public ExhibitionResponseModel createExhibition(String galleryId, ExhibitionRequestModel exhibitionRequestModel) {
        return exhibitionServiceClient.createExhibition(galleryId, exhibitionRequestModel);
    }

    @Override
    public void updateExhibition(String exhibitionId, ExhibitionRequestModel exhibitionRequestModel) {
       exhibitionServiceClient.updateExhibition(exhibitionId, exhibitionRequestModel);
    }

    @Override
    public void removeExhibition(String exhibitionId) {
        exhibitionServiceClient.removeExhibition(exhibitionId);
    }

    @Override
    public void removeAllExhibitions() {
        exhibitionServiceClient.removeAllExhibitions();
    }
}
