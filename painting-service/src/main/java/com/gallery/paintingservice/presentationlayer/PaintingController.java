package com.gallery.paintingservice.presentationlayer;

import com.gallery.paintingservice.businesslayer.PaintingPainterService;
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
    public List<PaintingResponseModel> getPaintingsInGallery(@PathVariable String galleryId){
        return paintingPainterService.getPaintingsInGallery(galleryId);
    }


    @GetMapping("/paintings/{paintingId}")
    public PaintingPainterResponseModel getPaintingByIdInGallery(@PathVariable String galleryId,
                                                                 @PathVariable String paintingId){
        return paintingPainterService.getPaintingByIdInGallery(galleryId, paintingId);
    }

    @GetMapping("/painters/{painterId}/paintings")
    public PaintingsOfPainterResponseModel getPaintingsByPainterIdInGallery(@PathVariable String galleryId,
                                                                            @PathVariable String painterId){
        return paintingPainterService.getPaintingsByPainterIdInGallery(galleryId, painterId);
    }

    @PostMapping("/paintings")
    public PaintingPainterResponseModel addPaintingToGallery(@PathVariable String galleryId,
                                                      @RequestBody PaintingRequestModel paintingRequestModel){
        return paintingPainterService.addPaintingToGallery(galleryId, paintingRequestModel);
    }

    @PutMapping("/paintings/{paintingId}")
    public PaintingPainterResponseModel updatePaintingInGallery(@PathVariable String galleryId, @PathVariable String paintingId,
                                                @RequestBody PaintingRequestModel paintingRequestModel){
        return paintingPainterService.updatePaintingInGallery(galleryId, paintingId, paintingRequestModel);
    }

    @DeleteMapping("/paintings/{paintingId}")
    public void deletePaintingByIdInGallery(@PathVariable String galleryId, @PathVariable String paintingId){
        paintingPainterService.removePaintingByIdInGallery(galleryId, paintingId);
    }

    @DeleteMapping()
    public void removeAllPaintingsIdInGallery(@PathVariable String galleryId){
        paintingPainterService.removeAllPaintingsInGallery(galleryId);
    }

    @PostMapping("/paintings/{paintingId}/painters")
    public PaintingPainterResponseModel addPainterToPaintingInGallery(@PathVariable String galleryId, @PathVariable String paintingId, @RequestBody PainterRequestModel painterRequestModel){
       return paintingPainterService.addPainterToPaintingInGallery(galleryId, paintingId, painterRequestModel);
    }

    @PutMapping("/paintings/{paintingId}/painters/{painterId}")
    public PaintingPainterResponseModel updatePainterOfPaintingInGallery(@PathVariable String galleryId, @PathVariable String paintingId, @PathVariable String painterId, @RequestBody PainterRequestModel painterRequestModel){
        return paintingPainterService.updatePainterOfPaintingInGallery(galleryId, paintingId, painterId, painterRequestModel);
    }

    @DeleteMapping("/paintings/{paintingId}/painters/{painterId}")
    public void removePainterFromPaintingInGallery(@PathVariable String galleryId, @PathVariable String paintingId, @PathVariable String painterId){
        paintingPainterService.removePainterOfPaintingInGallery(galleryId, paintingId, painterId);
    }

    /*@GetMapping("/exhibitions/{exhibitionId}/paintings")
    public List<PaintingResponseModel> getPaintingsOfExhibitionInGallery(@PathVariable String galleryId, @PathVariable String exhibitionId){
        return paintingPainterService.getPaintingsOfExhibitionInGallery(galleryId, exhibitionId);
    }*/

    /*@PutMapping("/exhibitions/{exhibitionId}/paintings/{paintingId}")
    public PaintingPainterResponseModel addPaintingToExhibitionInGallery(@PathVariable String galleryId, @PathVariable String paintingId, @PathVariable String exhibitionId){
        return paintingPainterService.addPaintingToExhibitionInGallery(galleryId, paintingId, exhibitionId);
    }


    @DeleteMapping("/exhibitions/{exhibitionId}/paintings/{paintingId}")
    public void removePaintingFromExhibitionInGallery(@PathVariable String galleryId, @PathVariable String paintingId, @PathVariable String exhibitionId) {
        paintingPainterService.removePaintingFromExhibitionInGallery(galleryId, paintingId, exhibitionId);
    }*/
}
