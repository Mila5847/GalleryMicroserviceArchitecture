package com.gallery.paintingservice.datalayer.painting;

import jakarta.persistence.Embeddable;

import java.util.UUID;

@Embeddable
public class PaintingIdentifier {
    private String paintingId;

    PaintingIdentifier() {
        this.paintingId = UUID.randomUUID().toString();
    }

    public String getPaintingId(){
        return paintingId;
    }

}
