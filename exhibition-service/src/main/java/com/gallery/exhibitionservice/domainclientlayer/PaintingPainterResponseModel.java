package com.gallery.exhibitionservice.domainclientlayer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

//@Value
//@Builder
//@Data
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaintingPainterResponseModel extends RepresentationModel<PaintingPainterResponseModel> {
    PaintingResponseModel paintingResponseModel;
    PainterResponseModel painterResponseModel;
}
