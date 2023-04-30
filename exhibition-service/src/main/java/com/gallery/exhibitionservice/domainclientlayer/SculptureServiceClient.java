package com.gallery.exhibitionservice.domainclientlayer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gallery.exhibitionservice.utils.HttpErrorInfo;
import com.gallery.exhibitionservice.utils.exceptions.InvalidInputException;
import com.gallery.exhibitionservice.utils.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@Slf4j
@Component
public class  SculptureServiceClient {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final String SCULPTURE_SERVICE_BASE_URL;
    public SculptureServiceClient(RestTemplate restTemplate,
                                  ObjectMapper objectMapper,
                                  @Value("${app.sculpture-service.host}") String sculptureServiceHost,
                                  @Value("${app.sculpture-service.port}") String sculptureServicePort) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.SCULPTURE_SERVICE_BASE_URL = "http://" + sculptureServiceHost + ":" + sculptureServicePort + "/api/v1/galleries";
    }

    public SculptureResponseModel[] getAllSculpturesInGallery(String galleryId) {
        SculptureResponseModel[] sculptureResponseModels;
        try {
            String url = SCULPTURE_SERVICE_BASE_URL + "/" + galleryId + "/sculptures";
            sculptureResponseModels = restTemplate.getForObject(url, SculptureResponseModel[].class);
            log.debug("5. Received in API-Gateway Sculpture Service Client getAllSculpturesInGallery");
        } catch (HttpClientErrorException ex) {
            log.debug("5. Exception caught in API-Gateway Sculpture Service Client getAllSculpturesInGallery");
            throw handleHttpClientException(ex);
        }
        return sculptureResponseModels;
    }
    public SculptureResponseModel getSculptureById(String galleryId, String sculptureId) {
        SculptureResponseModel sculptureResponseModel;
        try {
            String url = SCULPTURE_SERVICE_BASE_URL + "/" + galleryId + "/sculptures/" + sculptureId;
            sculptureResponseModel = restTemplate
                    .getForObject(url, SculptureResponseModel.class);
            log.debug("5. Received in API-Gateway Sculpture Service Client getSculptureById with gallery id " + galleryId + " sculpture id " + sculptureId);
        } catch (HttpClientErrorException ex) {
            log.debug("5. Caught an exception in API-Gateway Sculpture Service Client getSculptureById");
            throw handleHttpClientException(ex);
        }
        return sculptureResponseModel;
    }

    public SculptureResponseModel addSculptureInGallery(String galleryId, SculptureRequestModel sculptureRequestModel){
        SculptureResponseModel sculptureResponseModel;
        try {
            String url = SCULPTURE_SERVICE_BASE_URL + "/" + galleryId + "/sculptures";
            sculptureResponseModel =
                    restTemplate.postForObject(url, sculptureRequestModel,
                            SculptureResponseModel.class);

            log.debug("5. Received in API-Gateway Gallery Service Client addSculptureInGallery");
        } catch (HttpClientErrorException ex) {
            log.debug("5. Exception caught in in API-Gateway Gallery Service Client addSculptureInGallery");
            throw handleHttpClientException(ex);
        }
        return sculptureResponseModel;
    }

    public void updateSculptureInGallery(String galleryId, String sculptureId, SculptureRequestModel sculptureRequestModel){
        try {
            String url = SCULPTURE_SERVICE_BASE_URL + "/" + galleryId + "/sculptures/" + sculptureId;
            restTemplate.put(url, sculptureRequestModel);
            log.debug("5. Received in API-Gateway Gallery Service Client updateSculptureInGallery sculpture with id " + sculptureId + " in gallery with id " + galleryId);
        } catch (HttpClientErrorException ex) {
            log.debug("5. Exception caught in API-Gateway Gallery Service Client updateSculptureInGallery");
            throw handleHttpClientException(ex);
        }
    }

    public void deleteSculpture(String galleryId, String sculptureId) {
        try {
            String url = SCULPTURE_SERVICE_BASE_URL + "/" + galleryId + "/sculptures/" + sculptureId;
            restTemplate.delete(url);
            log.debug("5. Received in API-Gateway Sculpture Service Client deleteSculpture with galleryId : " + galleryId + " and sculpture id " + sculptureId);
        } catch (HttpClientErrorException ex) {
            log.debug("5. Exception caught in API-Gateway Sculpture Service Client deleteSculpture");
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
