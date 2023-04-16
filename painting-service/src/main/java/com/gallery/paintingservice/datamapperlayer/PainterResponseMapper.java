package com.gallery.paintingservice.datamapperlayer;


import com.gallery.paintingservice.datalayer.painter.Painter;
import com.gallery.paintingservice.presentationlayer.PainterResponseModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;


@Mapper(componentModel ="spring")
public interface PainterResponseMapper {
    @Mapping(expression = "java(painter.getPainterIdentifier().getPainterId())", target = "painterId")
    PainterResponseModel entityToResponseModel(Painter painter);
}
