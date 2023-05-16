package com.gallery.apigateway.presentationlayer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaintingResponseModel extends RepresentationModel<PaintingResponseModel> {
    String paintingId;
    String title;
    int yearCreated;
    String painterId;
    String galleryId;
}
