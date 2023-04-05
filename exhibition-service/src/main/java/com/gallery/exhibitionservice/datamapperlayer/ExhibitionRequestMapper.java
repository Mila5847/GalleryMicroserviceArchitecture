package com.gallery.exhibitionservice.datamapperlayer;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/*@Mapper(componentModel = "spring")
public interface ExhibitionRequestMapper {
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "exhibitionIdentifier", ignore = true),
            @Mapping(expression = "java(galleryIdentifier)", target = "galleryIdentifier")
    })
    Exhibition requestModelToEntity(ExhibitionRequestModel exhibitionRequestModel, GalleryIdentifier galleryIdentifier);
}*/
