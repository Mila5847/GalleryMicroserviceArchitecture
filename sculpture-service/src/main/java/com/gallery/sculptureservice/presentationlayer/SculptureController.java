package com.gallery.sculptureservice.presentationlayer;

import com.gallery.sculptureservice.businesslayer.SculptureService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/galleries/{galleryId}")
public class SculptureController {
    SculptureService sculptureService;

    public SculptureController(SculptureService sculptureService) {
        this.sculptureService = sculptureService;
    }

    @GetMapping("/sculptures")
    public List<SculptureResponseModel> getSculpturesInGallery(@PathVariable String galleryId){
        return sculptureService.getSculpturesInGallery(galleryId);
    }

    @GetMapping("/sculptures/{sculptureId}")
    public SculptureResponseModel getSculptureByIdInGallery(@PathVariable String galleryId, @PathVariable String sculptureId){
        return sculptureService.getSculptureByIdInGallery(galleryId, sculptureId);
    }

    /*@GetMapping("/exhibitions/{exhibitionId}/sculpture")
    public List<SculptureResponseModel> getSculpturesOfExhibitionInGallery(@PathVariable String galleryId, @PathVariable String exhibitionId){
        return sculptureService.getSculpturesOfExhibitionInGallery(galleryId, exhibitionId);
    }*/

    @PostMapping("/sculptures")
    public ResponseEntity<SculptureResponseModel> addSculptureToGallery(@PathVariable String galleryId,
                                                             @RequestBody SculptureRequestModel sculptureRequestModel){
        return ResponseEntity.status(HttpStatus.CREATED).body(sculptureService.addSculptureToGallery(galleryId, sculptureRequestModel));
    }

    @PutMapping("/sculptures/{sculptureId}")
    public ResponseEntity<SculptureResponseModel> updateSculptureInGallery(@PathVariable String galleryId, @PathVariable String sculptureId,
                                                           @RequestBody SculptureRequestModel sculptureRequestModel){
        return ResponseEntity.status(HttpStatus.OK).body(sculptureService.updateSculptureInGallery(galleryId, sculptureId, sculptureRequestModel));
    }

    @DeleteMapping("/sculptures/{sculptureId}")
    public void removeSculptureByIdInGallery(@PathVariable String galleryId, @PathVariable String sculptureId){
        sculptureService.removeSculptureByIdInGallery(galleryId, sculptureId);
    }

}
