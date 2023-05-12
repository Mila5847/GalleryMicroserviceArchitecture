package com.gallery.apigateway.presentationlayer.exhibition;

import com.gallery.apigateway.businesslayer.exhibition.ExhibitionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("api/v1/exhibitions")
@RequiredArgsConstructor
public class ExhibitionController {
    private final ExhibitionService exhibitionService;

    @GetMapping
    ResponseEntity<ExhibitionResponseModel[]> getAllExhibitions() {
        return ResponseEntity.ok().body(exhibitionService.getAllExhibitions());
    }

    @GetMapping("/{exhibitionId}")
    ResponseEntity<ExhibitionResponseModel> getExhibition(@PathVariable String exhibitionId) {
        return ResponseEntity.ok().body(exhibitionService.getExhibitionById(exhibitionId));
    }

    @PostMapping("/galleries/{galleryId}")
    ResponseEntity<ExhibitionResponseModel> createExhibition(@PathVariable String galleryId, @RequestBody ExhibitionRequestModel exhibitionRequestModel) {
        log.debug("created");
        return ResponseEntity.status(HttpStatus.CREATED).body(exhibitionService.createExhibition(galleryId, exhibitionRequestModel));
    }

    @PutMapping("/{exhibitionId}")
    ResponseEntity<Void> updateExhibition(@PathVariable String exhibitionId, @RequestBody ExhibitionRequestModel exhibitionRequestModel) {
        exhibitionService.updateExhibition(exhibitionId, exhibitionRequestModel);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{exhibitionId}")
    ResponseEntity<Void> deleteExhibition(@PathVariable String exhibitionId) {
        exhibitionService.removeExhibition(exhibitionId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
