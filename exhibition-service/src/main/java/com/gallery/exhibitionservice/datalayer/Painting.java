package com.gallery.exhibitionservice.datalayer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Painting {

    private String paintingIdentifier;
    private String title;

    private int yearCreated;

    private GalleryIdentifier galleryIdentifier;

    private PainterIdentifier painterIdentifier;

}
