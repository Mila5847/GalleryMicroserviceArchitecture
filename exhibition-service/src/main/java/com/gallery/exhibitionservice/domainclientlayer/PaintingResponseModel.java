package com.gallery.exhibitionservice.domainclientlayer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

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
