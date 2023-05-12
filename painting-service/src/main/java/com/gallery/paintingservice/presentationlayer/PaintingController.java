package com.gallery.paintingservice.presentationlayer;

import com.gallery.paintingservice.businesslayer.PaintingPainterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/galleries/{galleryId}")
public class PaintingController {

    PaintingPainterService paintingPainterService;

    public PaintingController(PaintingPainterService paintingPainterService) {
        this.paintingPainterService = paintingPainterService;
    }

    @GetMapping("/paintings")
    public ResponseEntity<List<PaintingResponseModel>> getPaintingsInGallery(@PathVariable String galleryId){
        return ResponseEntity.status(HttpStatus.OK).body(paintingPainterService.getPaintingsInGallery(galleryId));
    }


    @GetMapping("/paintings/{paintingId}")
    public ResponseEntity<PaintingPainterResponseModel> getPaintingByIdInGallery(@PathVariable String galleryId,
                                                                 @PathVariable String paintingId){
        return ResponseEntity.status(HttpStatus.OK).body(paintingPainterService.getPaintingByIdInGallery(galleryId, paintingId));
    }

    @GetMapping("/painters/{painterId}/paintings")
    public ResponseEntity<PaintingsOfPainterResponseModel> getPaintingsByPainterIdInGallery(@PathVariable String galleryId,
                                                                            @PathVariable String painterId){
        return ResponseEntity.status(HttpStatus.OK).body(paintingPainterService.getPaintingsByPainterIdInGallery(galleryId, painterId));
    }

    @PostMapping("/paintings")
    public ResponseEntity<PaintingPainterResponseModel> addPaintingToGallery(@PathVariable String galleryId,
                                                                            @RequestBody PaintingRequestModel paintingRequestModel){
        return ResponseEntity.status(HttpStatus.CREATED).body(paintingPainterService.addPaintingToGallery(galleryId, paintingRequestModel));
    }

    @PutMapping("/paintings/{paintingId}")
    public ResponseEntity<PaintingPainterResponseModel> updatePaintingInGallery(@PathVariable String galleryId, @PathVariable String paintingId,
                                                @RequestBody PaintingRequestModel paintingRequestModel){
        return ResponseEntity.ok().body(paintingPainterService.updatePaintingInGallery(galleryId, paintingId, paintingRequestModel));
    }

    @DeleteMapping("/paintings/{paintingId}")
    public ResponseEntity<Void> deletePaintingByIdInGallery(@PathVariable String galleryId, @PathVariable String paintingId){
        paintingPainterService.removePaintingByIdInGallery(galleryId, paintingId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/paintings/{paintingId}/painters")
    public ResponseEntity<PaintingPainterResponseModel> addPainterToPaintingInGallery(@PathVariable String galleryId, @PathVariable String paintingId, @RequestBody PainterRequestModel painterRequestModel){
       return ResponseEntity.status(HttpStatus.CREATED).body(paintingPainterService.addPainterToPaintingInGallery(galleryId, paintingId, painterRequestModel));
    }

    @PutMapping("/paintings/{paintingId}/painters/{painterId}")
    public ResponseEntity<PaintingPainterResponseModel> updatePainterOfPaintingInGallery(@PathVariable String galleryId, @PathVariable String paintingId, @PathVariable String painterId, @RequestBody PainterRequestModel painterRequestModel){
        return ResponseEntity.ok().body(paintingPainterService.updatePainterOfPaintingInGallery(galleryId, paintingId, painterId, painterRequestModel));
    }

    @DeleteMapping("/paintings/{paintingId}/painters/{painterId}")
    public ResponseEntity<Void> removePainterFromPaintingInGallery(@PathVariable String galleryId, @PathVariable String paintingId, @PathVariable String painterId){
        paintingPainterService.removePainterOfPaintingInGallery(galleryId, paintingId, painterId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
