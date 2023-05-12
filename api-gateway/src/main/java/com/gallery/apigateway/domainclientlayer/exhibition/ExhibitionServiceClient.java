package com.gallery.apigateway.domainclientlayer.exhibition;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gallery.apigateway.presentationlayer.GalleryResponseModel;
import com.gallery.apigateway.presentationlayer.exhibition.ExhibitionRequestModel;
import com.gallery.apigateway.presentationlayer.exhibition.ExhibitionResponseModel;
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
public class ExhibitionServiceClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final String EXHIBITION_SERVICE_BASE_URL;

    public ExhibitionServiceClient(RestTemplate restTemplate,
                                ObjectMapper objectMapper,
                                @Value("${app.exhibition-service.host}") String exhibitionServiceHost,
                                @Value("${app.exhibition-service.port}") String exhibitionServicePort) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.EXHIBITION_SERVICE_BASE_URL = "http://" + exhibitionServiceHost + ":" + exhibitionServicePort + "/api/v1/exhibitions";

    }

    public ExhibitionResponseModel[] getAllExhibitions() {
        ExhibitionResponseModel[] exhibitionResponseModels;
        try {
            String url = EXHIBITION_SERVICE_BASE_URL;
            exhibitionResponseModels = restTemplate.getForObject(url, ExhibitionResponseModel[].class);
            log.debug("5. Received in API-Gateway Gallery Service Client getAllGalleries");
        } catch (HttpClientErrorException ex) {
            log.debug("5. Exception caught in API-Gateway Gallery Service Client getAllGalleries");
            throw handleHttpClientException(ex);
        }
        return exhibitionResponseModels;
    }

    public ExhibitionResponseModel getExhibition(String exhibitionId) {
        ExhibitionResponseModel exhibitionResponseModel;
        try {
            String url = EXHIBITION_SERVICE_BASE_URL + "/" + exhibitionId;
            exhibitionResponseModel = restTemplate
                    .getForObject(url, ExhibitionResponseModel.class);

            log.debug("5. Received in API-Gateway Gallery Service Client getGallery with galleryResponseModel with id: " + exhibitionResponseModel.getExhibitionId());
        } catch (HttpClientErrorException ex) {
            log.debug("5. Caught an exception in API-Gateway Gallery Service Client getGallery with galleryResponseModel.");
            throw handleHttpClientException(ex);
        }
        return exhibitionResponseModel;
    }

    // create a new exhibition
    public ExhibitionResponseModel createExhibition(String galleryId, ExhibitionRequestModel exhibitionRequestModel) {
        ExhibitionResponseModel exhibitionResponseModel1;
        try {
            String url = EXHIBITION_SERVICE_BASE_URL + "/galleries/" + galleryId;
            exhibitionResponseModel1 = restTemplate
                    .postForObject(url, exhibitionRequestModel, ExhibitionResponseModel.class);
            log.debug("5. Received in API-Gateway Gallery Service Client createGallery with galleryResponseModel with id: " + exhibitionResponseModel1.getExhibitionId());
        } catch (HttpClientErrorException ex) {
            log.debug("5. Caught an exception in API-Gateway Gallery Service Client createGallery with galleryResponseModel.");
            throw handleHttpClientException(ex);
        }
        return exhibitionResponseModel1;
    }

    // update an existing exhibition
    public ExhibitionResponseModel updateExhibition(String exhibitionId, ExhibitionRequestModel exhibitionRequestModel) {
        ExhibitionResponseModel exhibitionResponseModel1;
        try {
            String url = EXHIBITION_SERVICE_BASE_URL + "/" + exhibitionId;
            restTemplate.put(url, exhibitionRequestModel);
            exhibitionResponseModel1 = restTemplate
                    .getForObject(url, ExhibitionResponseModel.class);
            log.debug("5. Received in API-Gateway Gallery Service Client updateGallery with galleryResponseModel with id: " + exhibitionResponseModel1.getExhibitionId());
        } catch (HttpClientErrorException ex) {
            log.debug("5. Caught an exception in API-Gateway Gallery Service Client updateGallery with galleryResponseModel.");
            throw handleHttpClientException(ex);
        }
        return exhibitionResponseModel1;
    }

    // delete an existing exhibition
    public void removeExhibition(String exhibitionId) {
        try {
            String url = EXHIBITION_SERVICE_BASE_URL + "/" + exhibitionId;
            restTemplate.delete(url);
            log.debug("5. Received in API-Gateway Gallery Service Client deleteGallery with galleryResponseModel with id: " + exhibitionId);
        } catch (HttpClientErrorException ex) {
            log.debug("5. Caught an exception in API-Gateway Gallery Service Client deleteGallery with galleryResponseModel.");
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
