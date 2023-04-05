package com.gallery.paintingservice.datamapperlayer;

import com.gallery.paintingservice.datalayer.painter.Painter;
import com.gallery.paintingservice.datalayer.painting.Painting;
import com.gallery.paintingservice.presentationlayer.PainterResponseModel;
import com.gallery.paintingservice.presentationlayer.PaintingController;
import com.gallery.paintingservice.presentationlayer.PaintingResponseModel;
import com.gallery.paintingservice.presentationlayer.PaintingsOfPainterResponseModel;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.hateoas.Link;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@Mapper(componentModel = "spring")
public interface PaintingsOfPainterResponseMapper {
    @Mapping(expression = "java(painter.getPainterIdentifier().getPainterId())", target = "painterId")

    @Mapping(expression = "java(paintings)", target = "paintings")
    @Mapping(expression = "java(painterResponseModel)", target = "painterResponseModel")
    PaintingsOfPainterResponseModel entitiesToResponseModel(Painter painter, PainterResponseModel painterResponseModel, List<PaintingResponseModel> paintings);

    @AfterMapping
    default void addLinks(@MappingTarget PaintingsOfPainterResponseModel model, Painting painting){
        // self link
        Link selfLink = linkTo(methodOn(PaintingController.class)
                .getPaintingByIdInGallery(painting.getGalleryIdentifier().getGalleryId(), painting.getPainterIdentifier().getPainterId()))
                .withSelfRel();
        model.add(selfLink);
        // get paintings by painterId
        Link paintingsLink = linkTo(methodOn(PaintingController.class)
                .getPaintingsByPainterIdInGallery(painting.getGalleryIdentifier().getGalleryId(), model.getPainterId()))
                .withRel("paintings");
        model.add(paintingsLink);
    }
}
