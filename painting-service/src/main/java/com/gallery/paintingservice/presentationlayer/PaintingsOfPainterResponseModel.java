package com.gallery.paintingservice.presentationlayer;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

//@Value
//@Builder
//@Data
//@AllArgsConstructor
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaintingsOfPainterResponseModel extends RepresentationModel<PaintingsOfPainterResponseModel> {
    String painterId;
    PainterResponseModel painterResponseModel;
    List<PaintingResponseModel> paintings;
}
