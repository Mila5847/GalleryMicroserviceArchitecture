package com.gallery.apigateway.domainclientlayer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gallery.apigateway.presentationlayer.PaintingResponseModel;
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
    private String galleryId;

    public PaintingServiceClient(RestTemplate restTemplate,
                                 ObjectMapper objectMapper,
                                 @Value("${app.painting-service.host}") String paintingServiceHost,
                                 @Value("${app.painting-service.port}") String paintingServicePort) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.PAINTING_SERVICE_BASE_URL = "http://" + paintingServiceHost + ":" + paintingServicePort + "/api/v1/galleries/" + this.galleryId + "/paintings";
    }

    public PaintingResponseModel[] getPaintingsInGallery(String galleryId) {
        PaintingResponseModel[] paintingResponseModels;
        this.galleryId = galleryId;
        try {
            String url = PAINTING_SERVICE_BASE_URL;
            paintingResponseModels = restTemplate.getForObject(url, PaintingResponseModel[].class);
            log.debug("HERE " + PAINTING_SERVICE_BASE_URL);
            log.debug("5. Received in API-Gateway Painting Service Client getPaintingsInGallery");
        } catch (HttpClientErrorException ex) {
            //log.debug("5. Exception caught in API-Gateway Gallery Service Client getAllGalleries");
            throw handleHttpClientException(ex);
        }
        return paintingResponseModels;
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
