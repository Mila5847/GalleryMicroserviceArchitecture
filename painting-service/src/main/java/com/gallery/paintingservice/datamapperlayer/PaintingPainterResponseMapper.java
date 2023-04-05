package com.gallery.paintingservice.datamapperlayer;

import com.gallery.paintingservice.datalayer.painting.Painting;
import com.gallery.paintingservice.presentationlayer.PainterResponseModel;
import com.gallery.paintingservice.presentationlayer.PaintingController;
import com.gallery.paintingservice.presentationlayer.PaintingPainterResponseModel;
import com.gallery.paintingservice.presentationlayer.PaintingResponseModel;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Mapper(componentModel = "spring")
public interface PaintingPainterResponseMapper {
    PaintingPainterResponseModel entityToResponseModel(PaintingResponseModel paintingResponseModel, PainterResponseModel painterResponseModel);

    @AfterMapping
    default void addLinks(@MappingTarget PaintingPainterResponseModel model, Painting painting){
        // self link
        Link selfLink = linkTo(methodOn(PaintingController.class)
                .getPaintingByIdInGallery(model.getPaintingResponseModel().getGalleryId(), model.getPaintingResponseModel().getPaintingId()))
                .withSelfRel();
        model.add(selfLink);

        // get paintings by painter id
        Link paintingsLink = linkTo(methodOn(PaintingController.class)
                .getPaintingsByPainterIdInGallery(model.getPaintingResponseModel().getGalleryId(), model.getPaintingResponseModel().getPainterId()))
                .withRel("paintings");
        model.add(paintingsLink);
    }
}
