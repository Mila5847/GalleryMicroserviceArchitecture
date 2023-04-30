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

import com.gallery.exhibitionservice.datalayer.Exhibition;
import com.gallery.exhibitionservice.presentationlayer.ExhibitionResponseModel;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.hateoas.Link;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ExhibitionResponseMapper {
    @Mapping(expression = "java(exhibition.getExhibitionIdentifier().getExhibitionId())",  target = "exhibitionId")
    @Mapping(expression = "java(exhibition.getGalleryIdentifier().getGalleryId())",  target = "galleryId")
    ExhibitionResponseModel entityToResponseModel(Exhibition exhibition);

    List<ExhibitionResponseModel> entityListToResponseModelList(List<Exhibition> exhibitions);
}
