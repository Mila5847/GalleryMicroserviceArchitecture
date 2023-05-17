package com.gallery.apigateway.presentationlayer;

import com.gallery.apigateway.businesslayer.PaintingsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.hateoas.Link;

@Slf4j
@RestController
@RequestMapping("api/v1/galleries")
public class PaintingsController {
    private PaintingsService paintingsService;

    public PaintingsController(PaintingsService paintingsService) {
        this.paintingsService = paintingsService;
    }

    @GetMapping(
            value = "/{galleryId}/paintings",
            produces = "application/json"
    )
    ResponseEntity<PaintingResponseModel[]> getPaintingsInGallery(@PathVariable String galleryId){
        return ResponseEntity.ok().body(paintingsService.getPaintingsInGallery(galleryId));
    }

    @GetMapping(
            value = "/{galleryId}/paintings/{paintingId}",
            produces = "application/json"
    )
    ResponseEntity<PaintingPainterResponseModel> getPaintingByIdInGallery(@PathVariable String galleryId, @PathVariable String paintingId){
        return ResponseEntity.ok().body(paintingsService.getPaintingAggregateByIdInGallery(galleryId, paintingId));
    }

    @GetMapping(
            value = "/{galleryId}/painters/{painterId}/paintings",
            produces = "application/json"
    )
    ResponseEntity<PaintingsOfPainterResponseModel> getPaintingsByPainterIdInGallery(@PathVariable String galleryId, @PathVariable String painterId){
        return ResponseEntity.ok().body(paintingsService.getPaintingsByPainterIdInGallery(galleryId, painterId));
    }

    @PostMapping(
            value = "/{galleryId}/paintings",
            produces = "application/json",
            consumes = "application/json"
    )
    ResponseEntity<PaintingPainterResponseModel> addPaintingInGallery(@PathVariable String galleryId, @RequestBody PaintingRequestModel paintingRequestModel){
        log.debug("1. Received in api-gateway galleries controller addGallery");
        return ResponseEntity.status(HttpStatus.CREATED).body(paintingsService.addPaintingInGallery(galleryId, paintingRequestModel));
    }

    @PostMapping(
            value = "/{galleryId}/paintings/{paintingId}/painters",
            produces = "application/json",
            consumes = "application/json"
    )
    ResponseEntity<PaintingPainterResponseModel> addPainterToPaintingInGallery(@PathVariable String galleryId, @PathVariable String paintingId, @RequestBody PainterRequestModel painterResponseModel){
        log.debug("1. Received in api-gateway galleries controller addGallery");
        return ResponseEntity.status(HttpStatus.CREATED).body(paintingsService.addPainterToPaintingInGallery(galleryId, paintingId, painterResponseModel));
    }

    @PutMapping(
            value = "/{galleryId}/paintings/{paintingId}",
            produces = "application/json",
            consumes = "application/json"
    )
    ResponseEntity<Void> updatePaintingInGallery(@PathVariable String galleryId, @PathVariable String paintingId, @RequestBody PaintingRequestModel paintingRequestModel){
        log.debug("1. Received in api-gateway paintings controller updatePaintingInGallery with gallery id " + galleryId + "and painting id " + paintingId);
        paintingsService.updatePaintingInGallery(galleryId, paintingId, paintingRequestModel);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    @PutMapping(
            value = "/{galleryId}/paintings/{paintingId}/painters/{painterId}",
            produces = "application/json",
            consumes = "application/json"
    )
    ResponseEntity<Void> updatePainterOfPaintingInGallery(@PathVariable String galleryId, @PathVariable String paintingId, @PathVariable String painterId, @RequestBody PainterRequestModel painterRequestModel){
        log.debug("1. Received in api-gateway paintings controller updatePaintingInGallery with gallery id " + galleryId + " and painting id " + paintingId + " painter id " + painterId);
        paintingsService.updatePainterOfPaintingInGallery(galleryId, paintingId, painterId, painterRequestModel);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping(
            value = "/{galleryId}/paintings/{paintingId}",
            produces = "application/json"
    )
    ResponseEntity<Void> removePaintingByIdInGallery(@PathVariable String galleryId, @PathVariable String paintingId){
        log.debug("1. Received in api-gateway paintings controller deletePainting with galleryId :" + galleryId + "and painting id: " + paintingId);
        paintingsService.removePaintingByIdInGallery(galleryId, paintingId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    @DeleteMapping(
            value = "/{galleryId}/paintings/{paintingId}/painters/{painterId}",
            produces = "application/json"
    )
    ResponseEntity<Void> removePainterFromPaintingInGallery(@PathVariable String galleryId, @PathVariable String paintingId, @PathVariable String painterId){
        log.debug("1. Received in api-gateway paintings controller deletePainting with galleryId :" + galleryId + " and painting id: " + paintingId + " painter id " + painterId);
        paintingsService.removePainterOfPaintingInGallery(galleryId, paintingId, painterId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
