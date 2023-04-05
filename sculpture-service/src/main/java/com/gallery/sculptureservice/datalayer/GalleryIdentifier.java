package com.gallery.sculptureservice.datalayer;

import jakarta.persistence.Embeddable;

@Embeddable
public class GalleryIdentifier {
    private String galleryId;

    public GalleryIdentifier(String galleryId) {
        this.galleryId = galleryId;
    }

    public GalleryIdentifier() {

    }

    public String getGalleryId() {
        return galleryId;
    }
}
