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

    @GetMapping("/{exhibitionId}")
    ResponseEntity<ExhibitionResponseModel> getExhibitionById(@PathVariable String exhibitionId){
        return ResponseEntity.ok().body(exhibitionService.getExhibitionById(exhibitionId));
    }

    @PostMapping("/galleries/{galleryId}")
    ResponseEntity<ExhibitionResponseModel> createExhibition(@PathVariable String galleryId, @RequestBody ExhibitionRequestModel exhibitionRequestModel){
        log.debug("created");
        return ResponseEntity.status(HttpStatus.CREATED).body(exhibitionService.createExhibition(galleryId, exhibitionRequestModel));
    }

    @PutMapping("/{exhibitionId}")
    ResponseEntity<ExhibitionResponseModel> updateExhibition(@PathVariable String exhibitionId, @RequestBody ExhibitionRequestModel exhibitionRequestModel){
        exhibitionService.updateExhibition(exhibitionId, exhibitionRequestModel);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{exhibitionId}")
    ResponseEntity<Void> deleteExhibition(@PathVariable String exhibitionId){
        exhibitionService.removeExhibition(exhibitionId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
