package com.gallery.exhibitionservice.datalayer;

import org.springframework.data.mongodb.core.index.Indexed;

import java.util.UUID;

public class PainterIdentifier {
    @Indexed(unique = true)
    private String painterId;

    public PainterIdentifier(String painterId) {
        this.painterId = painterId;
    }

    public void setPainterId(String painterId) {
        this.painterId = painterId;
    }

    public String getPainterId(){
        return painterId;
    }
}
