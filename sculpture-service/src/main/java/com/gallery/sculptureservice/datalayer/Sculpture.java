package com.gallery.sculptureservice.datalayer;

import jakarta.persistence.*;
import lombok.Data;

@Table(name="sculptures")
@Entity
@Data
public class Sculpture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;
    @Embedded
    private SculptureIdentifier sculptureIdentifier;
    private String title;
    private String material;
    private String texture;

    @Embedded
    private GalleryIdentifier galleryIdentifier;

    public Sculpture() {
        this.sculptureIdentifier = new SculptureIdentifier();
    }

    public Sculpture(String title, String material, String texture) {
        this.sculptureIdentifier = new SculptureIdentifier();
        this.title = title;
        this.material = material;
        this.texture = texture;
    }
}