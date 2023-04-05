package com.gallery.paintingservice.datamapperlayer;

import com.gallery.paintingservice.datalayer.painting.Painting;
import com.gallery.paintingservice.presentationlayer.PaintingController;
import com.gallery.paintingservice.presentationlayer.PaintingResponseModel;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.hateoas.Link;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Mapper(componentModel = "spring")
public interface PaintingResponseMapper {
    @Mapping(expression = "java(painting.getPaintingIdentifier().getPaintingId())", target = "paintingId")
    @Mapping(expression = "java(painting.getPainterIdentifier().getPainterId())", target = "painterId")
    @Mapping(expression = "java(painting.getGalleryIdentifier().getGalleryId())", target = "galleryId")
    PaintingResponseModel entityToResponseModel(Painting painting);
    List<PaintingResponseModel> entityListToResponseModelList(List<Painting> paintings);

    @AfterMapping
    default void addLinks(@MappingTarget PaintingResponseModel model, Painting painting){
        // self link
        Link selfLink = linkTo(methodOn(PaintingController.class)
                .getPaintingByIdInGallery(model.getGalleryId(), model.getPaintingId()))
                .withSelfRel();
        model.add(selfLink);

        // all paintings
        Link galleriesLink = linkTo(methodOn(PaintingController.class)
                .getPaintingsInGallery(model.getGalleryId()))
                .withRel("allPaintings");
        model.add(galleriesLink);
    }
}
