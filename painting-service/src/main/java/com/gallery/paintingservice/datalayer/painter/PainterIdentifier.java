package com.gallery.paintingservice.datalayer.painter;

import jakarta.persistence.Embeddable;

import java.util.UUID;

@Embeddable
public class PainterIdentifier {
    private String painterId;

    public PainterIdentifier() {
        this.painterId = UUID.randomUUID().toString();
    }

    public void setPainterId(String painterId) {
        this.painterId = painterId;
    }

    public String getPainterId(){
        return painterId;
    }
}
