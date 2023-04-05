package com.gallery.galleryservice.datamapperlayer;

import com.gallery.galleryservice.datalayer.Gallery;
import com.gallery.galleryservice.presentationlayer.GalleryRequestModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GalleryRequestMapper {
    @Mapping(target = "galleryIdentifier", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "address", ignore = true)
    Gallery requestModelToEntity(GalleryRequestModel galleryRequestModel);
}
