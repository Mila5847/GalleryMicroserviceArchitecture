package com.gallery.sculptureservice.datamapperlayer;

import com.gallery.sculptureservice.datalayer.GalleryIdentifier;
import com.gallery.sculptureservice.datalayer.Sculpture;
import com.gallery.sculptureservice.presentationlayer.SculptureRequestModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel ="spring")
public interface SculptureRequestMapper {
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "sculptureIdentifier", ignore = true),
            @Mapping(expression = "java(galleryIdentifier)", target = "galleryIdentifier")
    })
    Sculpture requestModelToEntity(SculptureRequestModel sculptureRequestModel, GalleryIdentifier galleryIdentifier);
}
