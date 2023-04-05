package com.gallery.paintingservice.presentationlayer;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

//@Value
//@Builder
//@Data
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaintingPainterResponseModel extends RepresentationModel<PaintingPainterResponseModel> {
    PaintingResponseModel paintingResponseModel;
    PainterResponseModel painterResponseModel;
}
