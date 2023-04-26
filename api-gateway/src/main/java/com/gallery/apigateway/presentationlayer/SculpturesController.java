package com.gallery.apigateway.presentationlayer;

import com.gallery.apigateway.businesslayer.GalleriesService;
import com.gallery.apigateway.businesslayer.SculptureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("api/v1/galleries")
public class SculpturesController {

    private SculptureService sculptureService;

    public SculpturesController(SculptureService sculptureService) {
        this.sculptureService = sculptureService;
    }

    @GetMapping(
            value = "/{galleryId}/sculptures/{sculptureId}",
            produces = "application/json"
    )
    ResponseEntity<SculptureResponseModel> getSculptureInGallery(@PathVariable String galleryId, @PathVariable String sculptureId){
        log.debug("1. Received in api-gateway galleries controller getSculptureInGallery with galleryId: " + galleryId + " and sculpture id " + sculptureId);
        return ResponseEntity.ok().body(sculptureService.getSculptureByIdInGallery(galleryId, sculptureId));
    }

    @GetMapping(
            value = "/{galleryId}/sculptures",
            produces = "application/json"
    )
    ResponseEntity<SculptureResponseModel[]> getSculpturesInGallery(@PathVariable String galleryId){
        return ResponseEntity.ok().body(sculptureService.getAllSculpturesInGallery(galleryId));
    }

    @PostMapping(
            value = "/{galleryId}/sculptures",
            produces = "application/json",
            consumes = "application/json"
    )
    ResponseEntity<SculptureResponseModel> addSculptureInGallery(@PathVariable String galleryId, @RequestBody SculptureRequestModel sculptureRequestModel){
        log.debug("1, Received in api-gateway sculptures controller addSculptureInGallery");
        return ResponseEntity.status(HttpStatus.CREATED).body(sculptureService.addSculptureToGallery(galleryId, sculptureRequestModel));
    }

    @PutMapping(
            value = "/{galleryId}/sculptures/{sculptureId}",
            produces = "application/json",
            consumes = "application/json"
    )
    ResponseEntity<Void> updateSculptureInGallery(@PathVariable String galleryId, @PathVariable String sculptureId, @RequestBody SculptureRequestModel sculptureRequestModel){
        log.debug("1. Received in api-gateway sculptures controller addSculptureInGallery with gallery id " + galleryId + " and sculpture id " + sculptureId);
        sculptureService.updateSculptureInGallery(galleryId, sculptureId, sculptureRequestModel);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping(
            value = "/{galleryId}/sculptures/{sculptureId}",
            produces = "application/json"
    )
    ResponseEntity<Void> removeSculptureFromGallery(@PathVariable String galleryId, @PathVariable String sculptureId){
        log.debug("1. Received in api-gateway sculptures controller removeSculptureFromGallery with galleryId: " + galleryId + " sculpture id " + sculptureId);
        sculptureService.deleteSculptureInGallery(galleryId, sculptureId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
