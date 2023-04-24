package com.gallery.apigateway.presentationlayer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaintingsOfPainterResponseModel extends RepresentationModel<PaintingsOfPainterResponseModel> {
    String painterId;
    PainterResponseModel painterResponseModel;
    List<PaintingResponseModel> paintings;
}
