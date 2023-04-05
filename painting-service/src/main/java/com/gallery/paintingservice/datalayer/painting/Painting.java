package com.gallery.paintingservice.datalayer.painting;

import com.gallery.paintingservice.datalayer.painter.PainterIdentifier;
import jakarta.persistence.*;
import lombok.Data;

@Table(name="paintings")
@Entity
@Data
public class Painting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    @Embedded
    private PaintingIdentifier paintingIdentifier;
    private String title;

    private Integer yearCreated;

    @Embedded
    private GalleryIdentifier galleryIdentifier;

    @Embedded
    private PainterIdentifier painterIdentifier;

    /*@Embedded
    private ExhibitionIdentifier exhibitionIdentifier;*/

    public Painting() {
        this.paintingIdentifier = new PaintingIdentifier();
    }

    public Painting(String title, int year) {
        this.paintingIdentifier = new PaintingIdentifier();
        this.title = title;
        this.yearCreated = year;
    }
}
