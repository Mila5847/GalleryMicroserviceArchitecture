package com.gallery.sculptureservice.datamapperlayer;

import com.gallery.sculptureservice.datalayer.Sculpture;
import com.gallery.sculptureservice.presentationlayer.SculptureController;
import com.gallery.sculptureservice.presentationlayer.SculptureResponseModel;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.hateoas.Link;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Mapper(componentModel = "spring")
public interface SculptureResponseMapper {
    @Mapping(expression = "java(sculpture.getSculptureIdentifier().getSculptureId())", target = "sculptureId")
    @Mapping(expression = "java(sculpture.getGalleryIdentifier().getGalleryId())", target = "galleryId")
    SculptureResponseModel entityToResponseModel(Sculpture sculpture);
    List<SculptureResponseModel> entityListToResponseModelList(List<Sculpture> sculptures);

    @AfterMapping
    default void addLinks(@MappingTarget SculptureResponseModel model, Sculpture sculpture){
        // self link
        Link selfLink = linkTo(methodOn(SculptureController.class)
                .getSculptureByIdInGallery(model.getGalleryId(), model.getSculptureId()))
                .withSelfRel();
        model.add(selfLink);

        // all sculptures
        Link sculpturesLink = linkTo(methodOn(SculptureController.class)
                .getSculpturesInGallery(model.getGalleryId()))
                .withRel("allSculptures");
        model.add(sculpturesLink);

    }
}
