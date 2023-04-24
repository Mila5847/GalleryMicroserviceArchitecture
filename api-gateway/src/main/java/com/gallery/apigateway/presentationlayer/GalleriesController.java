package com.gallery.apigateway.presentationlayer;

import com.gallery.apigateway.businesslayer.GalleriesService;
import com.gallery.apigateway.utils.exceptions.InvalidInputException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("api/v1/galleries")
public class GalleriesController {

    private GalleriesService galleriesService;

    public GalleriesController(GalleriesService galleriesService) {
        this.galleriesService = galleriesService;
    }

    @GetMapping(
            value = "/{galleryId}",
            produces = "application/json"
    )
    ResponseEntity<GalleryResponseModel> getGallery(@PathVariable String galleryId){
        log.debug("1. Received in api-gateway galleries controller getGalleryAggregate with galleryId: " + galleryId);
        return ResponseEntity.ok().body(galleriesService.getGallery(galleryId));
    }

    @GetMapping(
            value = "",
            produces = "application/json"
    )
    ResponseEntity<GalleryResponseModel[]> getGalleries(){
        return ResponseEntity.ok().body(galleriesService.getAllGalleries());
    }

    @PostMapping(
            value = "",
            produces = "application/json",
            consumes = "application/json"
    )
    ResponseEntity<GalleryResponseModel> addGallery(@RequestBody GalleryRequestModel galleryRequestModel){
        log.debug("1, Received in api-gateway galleries controller addGallery");
        return ResponseEntity.status(HttpStatus.CREATED).body(galleriesService.addGallery(galleryRequestModel));
    }

    @PutMapping(
            value = "/{galleryId}",
            produces = "application/json",
            consumes = "application/json"
    )
    ResponseEntity<Void> updateGallery(@PathVariable String galleryId, @RequestBody GalleryRequestModel galleryRequestModel){
        log.debug("1. Received in api-gateway galleries controller updateGallery with id " + galleryId);
        galleriesService.updateGallery(galleryId, galleryRequestModel);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    @DeleteMapping(
            value = "/{galleryId}",
            produces = "application/json"
    )
    ResponseEntity<Void> deleteGallery(@PathVariable String galleryId){
        log.debug("1. Received in api-gateway galleries controller deleteGallery with galleryId: " + galleryId);
        galleriesService.deleteGallery(galleryId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
