package com.gallery.galleryservice.datalayer;

import jakarta.persistence.Embeddable;

import java.util.UUID;

@Embeddable
public class GalleryIdentifier {
    private String galleryId;

    public GalleryIdentifier() {
        this.galleryId = UUID.randomUUID().toString();
    }

    public void setGalleryId(String galleryId) {
        this.galleryId = galleryId;
    }

    public String getGalleryId() {
        return galleryId;
    }


}
