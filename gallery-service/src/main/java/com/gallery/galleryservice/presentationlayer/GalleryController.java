package com.gallery.galleryservice.presentationlayer;

import com.gallery.galleryservice.businesslayer.GalleryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/galleries")
public class GalleryController {
    GalleryService galleryService;

    public GalleryController(GalleryService galleryService) {
        this.galleryService = galleryService;
    }

    @GetMapping
    public List<GalleryResponseModel> getGalleries(){
        return galleryService.getGalleries();
    }

    @GetMapping("/{galleryId}")
    public GalleryResponseModel getGalleryById(@PathVariable String galleryId){
        return galleryService.getGalleryById(galleryId);
    }

    @GetMapping("/gallery")
    public GalleryResponseModel getGalleryByName(@RequestParam Map<String, String> queryParams){
        return galleryService.getGalleryByName(queryParams);
    }

    @PostMapping
    public ResponseEntity<GalleryResponseModel> addGallery(@RequestBody GalleryRequestModel galleryRequestModel){
        return ResponseEntity.status(HttpStatus.CREATED).body(galleryService.addGallery(galleryRequestModel));
    }

    @PutMapping("/{galleryId}")
    public ResponseEntity<GalleryResponseModel> updateGallery(@RequestBody GalleryRequestModel galleryRequestModel, @PathVariable String galleryId){
        return ResponseEntity.status(HttpStatus.OK).body(galleryService.updateGallery(galleryRequestModel, galleryId));
    }

    @DeleteMapping("{galleryId}")
    public ResponseEntity<Void> removeGalleryById(@PathVariable String galleryId){
        galleryService.removeGalleryById(galleryId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

   /* @DeleteMapping
    public void removeAllGalleries(){
        galleryService.removeAllGalleries();
    }*/
}
