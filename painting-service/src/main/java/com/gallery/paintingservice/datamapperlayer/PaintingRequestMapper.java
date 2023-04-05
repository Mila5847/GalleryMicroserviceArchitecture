package com.gallery.paintingservice.datamapperlayer;

import com.gallery.paintingservice.datalayer.painter.PainterIdentifier;
import com.gallery.paintingservice.datalayer.painting.GalleryIdentifier;
import com.gallery.paintingservice.datalayer.painting.Painting;
import com.gallery.paintingservice.presentationlayer.PaintingRequestModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel ="spring")
public interface PaintingRequestMapper {
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "paintingIdentifier", ignore = true),
            @Mapping(expression = "java(painterIdentifier)", target = "painterIdentifier"),
            @Mapping(expression = "java(galleryIdentifier)", target = "galleryIdentifier")
    })
    Painting requestModelToEntity(PaintingRequestModel paintingRequestModel, PainterIdentifier painterIdentifier, GalleryIdentifier galleryIdentifier);


}
