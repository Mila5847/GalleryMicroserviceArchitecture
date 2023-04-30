package com.gallery.exhibitionservice.datalayer;

import java.util.UUID;
public class PaintingIdentifier {
    private String paintingId;

    PaintingIdentifier() {
        this.paintingId = UUID.randomUUID().toString();
    }

    public String getPaintingId(){
        return paintingId;
    }

}
