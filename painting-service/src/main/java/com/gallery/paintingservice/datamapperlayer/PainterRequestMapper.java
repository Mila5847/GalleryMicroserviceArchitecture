package com.gallery.paintingservice.datamapperlayer;

import com.gallery.paintingservice.datalayer.painter.Painter;
import com.gallery.paintingservice.datalayer.painter.PainterIdentifier;
import com.gallery.paintingservice.presentationlayer.PainterRequestModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PainterRequestMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(expression = "java(painterIdentifier)", target = "painterIdentifier")
    Painter requestModelToEntity(PainterRequestModel painterRequestModel, PainterIdentifier painterIdentifier);
}
