package com.gallery.exhibitionservice.datalayer;

import org.springframework.data.mongodb.core.index.Indexed;

import java.util.UUID;

public class ExhibitionIdentifier {
    @Indexed(unique = true)
    private String exhibitionId;

    public ExhibitionIdentifier() {
        this.exhibitionId = UUID.randomUUID().toString();
    }

    public void setExhibitionId(String exhibitionId) {
        this.exhibitionId = exhibitionId;
    }

    public String getExhibitionId(){
        return exhibitionId;
    }
}
