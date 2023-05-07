package com.gallery.exhibitionservice.domainclientlayer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SculptureResponseModel {
    String sculptureId;
    String title;
    String material;
    String texture;
    String galleryId;
}
