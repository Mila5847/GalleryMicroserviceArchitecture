package com.gallery.apigateway.domainclientlayer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gallery.apigateway.presentationlayer.*;
import com.gallery.apigateway.utils.HttpErrorInfo;
import com.gallery.apigateway.utils.exceptions.InvalidInputException;
import com.gallery.apigateway.utils.exceptions.NotFoundException;
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
public class PaintingServiceClient {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final String PAINTING_SERVICE_BASE_URL;
    public PaintingServiceClient(RestTemplate restTemplate,
                                 ObjectMapper objectMapper,
                                 @Value("${app.painting-service.host}") String paintingServiceHost,
                                 @Value("${app.painting-service.port}") String paintingServicePort) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.PAINTING_SERVICE_BASE_URL = "http://" + paintingServiceHost + ":" + paintingServicePort + "/api/v1/galleries";
    }

    public PaintingResponseModel[] getPaintingsInGallery(String galleryId) {
        PaintingResponseModel[] paintingResponseModels;
        try {
            String url = PAINTING_SERVICE_BASE_URL + "/" + galleryId + "/paintings";
            paintingResponseModels = restTemplate.getForObject(url, PaintingResponseModel[].class);
            log.debug("HERE " + PAINTING_SERVICE_BASE_URL);
            log.debug("5. Received in API-Gateway Painting Service Client getPaintingsInGallery");
        } catch (HttpClientErrorException ex) {
            log.debug("5. Exception caught in API-Gateway Painting Service Client getAllGalleries");
            throw handleHttpClientException(ex);
        }
        return paintingResponseModels;
    }

    public PaintingPainterResponseModel getPaintingAggregateById(String galleryId, String paintingId) {
        PaintingPainterResponseModel paintingPainterResponseModel;
        try {
            String url = PAINTING_SERVICE_BASE_URL + "/" + galleryId + "/paintings/" + paintingId;
            paintingPainterResponseModel = restTemplate
                    .getForObject(url, PaintingPainterResponseModel.class);
            log.debug("5. Received in API-Gateway Painting Service Client getPaintingAggregateById with paintingResponseModel with id: " + paintingPainterResponseModel.getPaintingResponseModel().getPaintingId());
        } catch (HttpClientErrorException ex) {
            log.debug("5. Caught an exception in API-Gateway Painting Service Client.");
            throw handleHttpClientException(ex);
        }
        return paintingPainterResponseModel;
    }

    public PaintingsOfPainterResponseModel getPaintingAggregateByPainterIdInGallery(String galleryId, String painterId) {
        PaintingsOfPainterResponseModel paintingsOfPainterResponseModel;
        try {
            String url = PAINTING_SERVICE_BASE_URL + "/" + galleryId + "/painters/" + painterId + "/paintings";
            paintingsOfPainterResponseModel = restTemplate
                    .getForObject(url, PaintingsOfPainterResponseModel.class);
            log.debug("5. Received in API-Gateway Painting Service Client getPaintingAggregateByPainterIdInGallery with painter id" + paintingsOfPainterResponseModel.getPainterId());
        } catch (HttpClientErrorException ex) {
            log.debug("5. Caught an exception in API-Gateway Painting Service Client getPaintingAggregateByPainterIdInGallery");
            throw handleHttpClientException(ex);
        }
        return paintingsOfPainterResponseModel;
    }

    public PaintingPainterResponseModel addPaintingInGallery(String galleryId, PaintingRequestModel paintingRequestModel){
        PaintingPainterResponseModel paintingPainterResponseModel;
        try {
            String url = PAINTING_SERVICE_BASE_URL + "/" + galleryId + "/paintings";
            paintingPainterResponseModel =
                    restTemplate.postForObject(url, paintingRequestModel,
                            PaintingPainterResponseModel.class);
            log.debug("5. Received in API-Gateway Painting Service Client addPaintingInGallery");
        } catch (HttpClientErrorException ex) {
            log.debug("5. Exception caught in API-Gateway Painting Service Client addPaintingInGallery");
            throw handleHttpClientException(ex);
        }
        return paintingPainterResponseModel;
    }

    public  PaintingPainterResponseModel  addPainterToPaintingInGallery(String galleryId, String paintingId, PainterRequestModel painterResponseModel){
        PaintingPainterResponseModel paintingPainterResponseModel;
        try {
            String url = PAINTING_SERVICE_BASE_URL + "/" + galleryId + "/paintings/" + paintingId + "/painters";
            paintingPainterResponseModel =
                    restTemplate.postForObject(url, painterResponseModel,
                            PaintingPainterResponseModel.class);
            log.debug("5. Received in API-Gateway Painting Service Client addPainterToPaintingInGallery");
        } catch (HttpClientErrorException ex) {
            log.debug("5. Exception caught in API-Gateway Painting Service Client addPainterToPaintingInGallery");
            throw handleHttpClientException(ex);
        }
        return paintingPainterResponseModel;
    }

    public void updatePaintingInGallery(String galleryId, String paintingId, PaintingRequestModel paintingRequestModel){
        try {
            String url = PAINTING_SERVICE_BASE_URL + "/" + galleryId + "/paintings/" + paintingId;
            restTemplate.put(url, paintingRequestModel);
            log.debug("5. Received in API-Gateway Paintings Service Client updatePainting");
        } catch (HttpClientErrorException ex) {
            log.debug("5. Exception caught in API-Gateway Paintings Service Client updatePainting");
            throw handleHttpClientException(ex);
        }
    }

    public void updatePainTerOfPaintingInGallery(String galleryId, String paintingId, String painterId, PainterRequestModel painterRequestModel){
        try {
            String url = PAINTING_SERVICE_BASE_URL + "/" + galleryId + "/paintings/" + paintingId + "/painters/" + painterId;
            restTemplate.put(url, painterRequestModel);
            log.debug("5. Received in API-Gateway Paintings Service Client updatePainTerOfPaintingInGallery");
        } catch (HttpClientErrorException ex) {
            log.debug("5. Exception caught in API-Gateway Paintings Service Client updatePainTerOfPaintingInGallery");
            throw handleHttpClientException(ex);
        }
    }

    public void removePaintingByIdInGallery(String galleryId, String paintingId) {
        try {
            String url = PAINTING_SERVICE_BASE_URL + "/" + galleryId + "/paintings/" + paintingId;
            restTemplate.delete(url);
            log.debug("5. Received in API-Gateway Painting Service Client deletePaintingByIdInGallery with galleryId : " + galleryId + " and painting id " + paintingId);
        } catch (HttpClientErrorException ex) {
            log.debug("5. Exception caught in API-Gateway Painting Service Client deletePaintingByIdInGallery");
            throw handleHttpClientException(ex);
        }
    }

    public void removePainterOfPaintingInGallery(String galleryId, String paintingId, String painterId) {
        try {
            String url = PAINTING_SERVICE_BASE_URL + "/" + galleryId + "/paintings/" + paintingId + "/painters/" + painterId;
            restTemplate.delete(url);
            log.debug("5. Received in API-Gateway Painting Service Client removePainterOfPaintingInGallery with galleryId : " + galleryId + " and painting id " + paintingId + "painter id " + painterId);
        } catch (HttpClientErrorException ex) {
            log.debug("5. Exception caught in API-Gateway Painting Service Client removePainterOfPaintingInGallery");
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
