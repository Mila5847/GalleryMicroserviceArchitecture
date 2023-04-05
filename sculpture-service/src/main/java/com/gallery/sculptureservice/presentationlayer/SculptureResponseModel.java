package com.gallery.sculptureservice.presentationlayer;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

//@Value
//@Builder
//@Data
//@AllArgsConstructor
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SculptureResponseModel extends RepresentationModel<SculptureResponseModel> {
    String sculptureId;
    String title;
    String material;
    String texture;
    String galleryId;
}
