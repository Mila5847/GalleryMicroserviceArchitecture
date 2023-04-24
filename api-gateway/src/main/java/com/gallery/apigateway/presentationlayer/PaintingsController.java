package com.gallery.apigateway.presentationlayer;

import com.gallery.apigateway.businesslayer.PaintingsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
