package com.gallery.exhibitionservice.presentationlayer;

import com.gallery.exhibitionservice.businesslayer.ExhibitionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("api/v1/exhibitions")
@RequiredArgsConstructor
public class ExhibitionController {
    private final ExhibitionService exhibitionService;

    @GetMapping
    ResponseEntity<List<ExhibitionResponseModel>> getAllPurchaseModels(){
        return ResponseEntity.ok().body((exhibitionService.getAllExhibitions()));
    }

    @PostMapping()
    ResponseEntity<ExhibitionResponseModel> createExhibition(String galleryId, ExhibitionRequestModel exhibitionRequestModel){
        return ResponseEntity.status(HttpStatus.CREATED).body(exhibitionService.createExhibition(galleryId, exhibitionRequestModel));
    }

    /*ExhibitionService exhibitionService;

    public ExhibitionController(ExhibitionService exhibitionService) {
        this.exhibitionService = exhibitionService;
    }

    @GetMapping("/exhibitions")
    public List<ExhibitionResponseModel> getExhibitionsInGallery(@PathVariable String galleryId){
        return exhibitionService.getExhibitionsInGallery(galleryId);
    }

    @GetMapping("/exhibitions/{exhibitionId}")
    public GalleryExhibitionPaintingSculptureResponseModel getExhibitionByIdInGallery(@PathVariable String galleryId, @PathVariable String exhibitionId){
        return exhibitionService.getExhibitionByIdInGallery(galleryId, exhibitionId);
    }

    @GetMapping("/exhibition")
    public GalleryExhibitionPaintingSculptureResponseModel getExhibitionByField(@PathVariable String galleryId, @RequestParam Map<String, String> queryParams){
        return exhibitionService.getExhibitionByField(galleryId, queryParams);
    }
*/
    /*@PostMapping("/exhibitions")
    public GalleryExhibitionPaintingResponseModel addExhibitionInGallery(@PathVariable String galleryId, @RequestBody ExhibitionRequestModel exhibitionRequestModel){
        return  exhibitionService.addExhibitionToGallery(galleryId, exhibitionRequestModel);
    }*/

   /* @PutMapping("/exhibitions/{exhibitionId}")
    public GalleryExhibitionPaintingResponseModel updateExhibitionInGallery(@PathVariable String galleryId, @PathVariable String exhibitionId, @RequestBody ExhibitionRequestModel exhibitionRequestModel){
        return exhibitionService.updateExhibitionInGallery(galleryId, exhibitionId, exhibitionRequestModel);
    }*/

    /*@DeleteMapping("/exhibitions/{exhibitionId}")
    public void removeExhibitionFromGallery(@PathVariable String galleryId, @PathVariable String exhibitionId){
        exhibitionService.removeExhibitionFromGallery(galleryId, exhibitionId);
    }

    @DeleteMapping("/exhibitions")
    public void removeAllExhibitionsFromGallery(@PathVariable String galleryId){
        exhibitionService.removeAllExhibitionsFromGallery(galleryId);
    }

    @DeleteMapping("/exhibition")
    public void removeExhibitionByField(@PathVariable String galleryId, @RequestParam Map<String, String> queryParams){
        exhibitionService.removeExhibitionByField(galleryId, queryParams);
    }

    @PostMapping("/exhibitions")
    public GalleryExhibitionPaintingSculptureResponseModel addExhibitionInGallery(@PathVariable String galleryId, @RequestBody GalleryExhibitionPaintingSculptureRequestModel galleryExhibitionPaintingRequestModel){
        return exhibitionService.addExhibitionInGallery(galleryId, galleryExhibitionPaintingRequestModel);
    }

    @PutMapping("/exhibitions/{exhibitionId}")
    public GalleryExhibitionPaintingSculptureResponseModel updateExhibitionInGallery(@PathVariable String galleryId, @PathVariable String exhibitionId, @RequestBody GalleryExhibitionPaintingSculptureRequestModel galleryExhibitionPaintingRequestModel){
        return exhibitionService.updateExhibitionInGallery(galleryId, exhibitionId, galleryExhibitionPaintingRequestModel);
    }*/
}
