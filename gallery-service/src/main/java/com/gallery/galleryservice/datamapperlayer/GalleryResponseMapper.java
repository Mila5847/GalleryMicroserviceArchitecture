package com.gallery.galleryservice.datamapperlayer;

import com.gallery.galleryservice.datalayer.Gallery;
import com.gallery.galleryservice.presentationlayer.GalleryController;
import com.gallery.galleryservice.presentationlayer.GalleryResponseModel;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.hateoas.Link;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Mapper(componentModel = "spring")
public interface GalleryResponseMapper {
    @Mapping(expression = "java(gallery.getGalleryIdentifier().getGalleryId())",  target = "galleryId")
    @Mapping(expression = "java(gallery.getAddress().getStreetAddress())", target = "streetAddress" )
    @Mapping(expression = "java(gallery.getAddress().getCity())", target = "city" )
    @Mapping(expression = "java(gallery.getAddress().getProvince())", target = "province" )
    @Mapping(expression = "java(gallery.getAddress().getCountry())", target = "country" )
    @Mapping(expression = "java(gallery.getAddress().getPostalCode())", target = "postalCode" )
    GalleryResponseModel entityToResponseModel(Gallery gallery);

    List<GalleryResponseModel> entityListToResponseModelList(List<Gallery> galleries);

    @AfterMapping
    default void addLinks(@MappingTarget GalleryResponseModel model, Gallery gallery){
        // self link
        Link selfLink = linkTo(methodOn(GalleryController.class)
                .getGalleryById(model.getGalleryId()))
                .withSelfRel();
        model.add(selfLink);

        // all galleries
        Link galleriesLink = linkTo(methodOn(GalleryController.class)
                .getGalleries())
                .withRel("allGalleries");
        model.add(galleriesLink);
    }
}
