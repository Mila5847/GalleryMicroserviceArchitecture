package com.gallery.apigateway.domainclientlayer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gallery.apigateway.presentationlayer.GalleryRequestModel;
import com.gallery.apigateway.presentationlayer.GalleryResponseModel;
import com.gallery.apigateway.utils.HttpErrorInfo;
import com.gallery.apigateway.utils.exceptions.InvalidInputException;
import com.gallery.apigateway.utils.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@Slf4j
@Component
public class GalleryServiceClient {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final String GALLERY_SERVICE_BASE_URL;

    public GalleryServiceClient(RestTemplate restTemplate,
                               ObjectMapper objectMapper,
                               @Value("${app.gallery-service.host}") String galleryServiceHost,
                               @Value("${app.gallery-service.port}") String galleryServicePort) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.GALLERY_SERVICE_BASE_URL = "http://" + galleryServiceHost + ":" + galleryServicePort + "/api/v1/galleries";
    }

    public GalleryResponseModel[] getAllGalleries() {
        GalleryResponseModel[] galleryResponseModels;
        try {
            String url = GALLERY_SERVICE_BASE_URL;
            galleryResponseModels = restTemplate.getForObject(url, GalleryResponseModel[].class);
            log.debug("5. Received in API-Gateway Gallery Service Client getAllGalleries");
        } catch (HttpClientErrorException ex) {
            log.debug("5. Exception caught in API-Gateway Gallery Service Client getAllGalleries");
            throw handleHttpClientException(ex);
        }
        return galleryResponseModels;
    }
    public GalleryResponseModel getGallery(String galleryId) {
        GalleryResponseModel galleryResponseModel;
        try {
            String url = GALLERY_SERVICE_BASE_URL + "/" + galleryId;
            galleryResponseModel = restTemplate
                    .getForObject(url, GalleryResponseModel.class);

            log.debug("5. Received in API-Gateway Gallery Service Client getGallery with galleryResponseModel with id: " + galleryResponseModel.getGalleryId());
        } catch (HttpClientErrorException ex) {
            log.debug("5. Caught an exception in API-Gateway Gallery Service Client getGallery with galleryResponseModel.");
            throw handleHttpClientException(ex);
        }
        return galleryResponseModel;
    }

    public GalleryResponseModel addGallery(GalleryRequestModel galleryRequestModel){
        GalleryResponseModel galleryResponseModel;
        try {
            String url = GALLERY_SERVICE_BASE_URL;
            galleryResponseModel =
                    restTemplate.postForObject(url, galleryRequestModel,
                            GalleryResponseModel.class);

            log.debug("5. Received in API-Gateway Gallery Service Client addGallery");
        } catch (HttpClientErrorException ex) {
            log.debug("5. Exception caught in in API-Gateway Gallery Service Client addGallery");
            throw handleHttpClientException(ex);
        }
        return galleryResponseModel;
    }

    public void updateGallery(String galleryId, GalleryRequestModel galleryRequestModel){
        try {
            String url = GALLERY_SERVICE_BASE_URL + "/" + galleryId;
            restTemplate.put(url, galleryRequestModel);
            log.debug("5. Received in API-Gateway Gallery Service Client updateGallery");
        } catch (HttpClientErrorException ex) {
            log.debug("5. Exception caught in API-Gateway Gallery Service Client updateGallery");
            throw handleHttpClientException(ex);
        }
    }

    public void deleteGallery(String galleryId) {
        try {
            String url = GALLERY_SERVICE_BASE_URL + "/" + galleryId;
            restTemplate.delete(url);
            log.debug("5. Received in API-Gateway Gallery Service Client deleteGallery with galleryId : " + galleryId);
        } catch (HttpClientErrorException ex) {
            log.debug("5. Exception caught in API-Gateway Gallery Service Client deleteGallery");
            throw handleHttpClientException(ex);
        }
    }

    private RuntimeException handleHttpClientException(HttpClientErrorException ex) {
        if (ex.getStatusCode() == NOT_FOUND) {
            return new NotFoundException(getErrorMessage(ex));
        }
        if (ex.getStatusCode() == UNPROCESSABLE_ENTITY) {
            return new InvalidInputException(getErrorMessage(ex));
        }
        log.warn("Got a unexpected HTTP error: {}, will rethrow it", ex.getStatusCode());
        log.warn("Error body: {}", ex.getResponseBodyAsString());
        return ex;
    }
    private String getErrorMessage(HttpClientErrorException ex) {
        try {
            return objectMapper.readValue(ex.getResponseBodyAsString(), HttpErrorInfo.class).getMessage();
        }
        catch (IOException ioex) {
            return ioex.getMessage();
        }
    }
}
