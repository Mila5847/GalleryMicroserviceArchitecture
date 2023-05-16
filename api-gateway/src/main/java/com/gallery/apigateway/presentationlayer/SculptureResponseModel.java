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
public class SculptureResponseModel extends RepresentationModel<SculptureResponseModel> {
    String sculptureId;
    String title;
    String material;
    String texture;
    String galleryId;
}
