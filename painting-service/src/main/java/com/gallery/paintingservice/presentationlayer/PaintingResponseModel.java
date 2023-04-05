package com.gallery.paintingservice.presentationlayer;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

//@Value
//@Builder
//@AllArgsConstructor
//@Data
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaintingResponseModel extends RepresentationModel<PaintingResponseModel> {
    String paintingId;
    String title;
    int yearCreated;
    String painterId;
    String galleryId;
}
