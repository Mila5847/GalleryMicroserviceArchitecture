package com.gallery.exhibitionservice.datalayer;

import org.springframework.data.mongodb.core.index.Indexed;

import java.util.UUID;
public class GalleryIdentifier {
    @Indexed(unique = true)
    private String galleryId;

    public GalleryIdentifier(String galleryId) {
        this.galleryId = galleryId;
    }

    public void setGalleryId(String galleryId) {
        this.galleryId = galleryId;
    }

    public String getGalleryId() {
        return galleryId;
    }


}
