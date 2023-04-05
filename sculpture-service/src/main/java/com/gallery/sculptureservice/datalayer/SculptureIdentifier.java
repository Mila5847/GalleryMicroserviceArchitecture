package com.gallery.sculptureservice.datalayer;

import jakarta.persistence.Embeddable;

import java.util.UUID;

@Embeddable
public class SculptureIdentifier {
    private String sculptureId;

    SculptureIdentifier() {
        this.sculptureId = UUID.randomUUID().toString();
    }

    public String getSculptureId(){
        return sculptureId;
    }

}
