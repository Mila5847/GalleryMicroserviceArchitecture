package com.gallery.exhibitionservice.datamapperlayer;

/*import com.gallery.exhibitionservice.presentationlayer.ExhibitionController;
import com.gallery.exhibitionservice.presentationlayer.GalleryExhibitionPaintingSculptureResponseModel;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.hateoas.Link;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;*/

/*@Mapper(componentModel = "spring")
public interface GalleryExhibitionPaintingSculptureResponseMapper {
    @Mapping(expression = "java(gallery.getGalleryIdentifier().getGalleryId())",  target = "galleryId")
    @Mapping(expression="java(gallery.getName())", target ="galleryName")
    @Mapping(expression="java(paintingResponseModels)", target ="paintings")
    @Mapping(expression="java(exhibitionResponseModel)", target ="exhibition")
    @Mapping(expression="java(sculptureResponseModels)", target ="sculptures")
    GalleryExhibitionPaintingSculptureResponseModel entityToResponseModel(Gallery gallery, List<PaintingResponseModel> paintingResponseModels, ExhibitionResponseModel exhibitionResponseModel, List<SculptureResponseModel> sculptureResponseModels);

    @AfterMapping
    default void addLinks(@MappingTarget GalleryExhibitionPaintingSculptureResponseModel model){
        // self link
        Link selfLink = linkTo(methodOn(ExhibitionController.class)
                .getExhibitionByIdInGallery(model.getGalleryId(), model.getExhibition().getExhibitionId()))
                .withSelfRel();
        model.add(selfLink);

        // all exhibitions
        Link exhibitionsLink = linkTo(methodOn(ExhibitionController.class)
                .getExhibitionsInGallery(model.getGalleryId()))
                .withRel("allExhibitions");
        model.add(exhibitionsLink);
    }
}*/
