package com.gallery.exhibitionservice.datamapperlayer;

/*import com.gallery.exhibitionservice.presentationlayer.ExhibitionController;
import com.gallery.exhibitionservice.presentationlayer.ExhibitionResponseModel;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.hateoas.Link;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;*/

/*@Mapper(componentModel = "spring")
public interface ExhibitionResponseMapper {
    @Mapping(expression = "java(exhibition.getExhibitionIdentifier().getExhibitionId())",  target = "exhibitionId")
    @Mapping(expression = "java(exhibition.getGalleryIdentifier().getGalleryId())",  target = "galleryId")
    ExhibitionResponseModel entityToResponseModel(Exhibition exhibition);

    List<ExhibitionResponseModel> entityListToResponseModelList(List<Exhibition> exhibitions);

    @AfterMapping
    default void addLinks(@MappingTarget ExhibitionResponseModel model, Exhibition exhibition){
        // self link
        Link selfLink = linkTo(methodOn(ExhibitionController.class)
                .getExhibitionByIdInGallery(model.getGalleryId(), model.getExhibitionId()))
                .withSelfRel();
        model.add(selfLink);

        // all exhibitions
        Link exhibitionsLink = linkTo(methodOn(ExhibitionController.class)
                .getExhibitionsInGallery(model.getGalleryId()))
                .withRel("allExhibitions");
        model.add(exhibitionsLink);
    }
}*/
