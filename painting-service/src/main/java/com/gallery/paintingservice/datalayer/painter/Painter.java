package com.gallery.paintingservice.datalayer.painter;

import jakarta.persistence.*;
import lombok.Data;


@Table(name="painters")
@Entity
@Data
public class Painter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;
    private String name;
    private String origin;
    private String birthDate;
    private String deathDate;

    @Embedded
    private PainterIdentifier painterIdentifier;


    public Painter() {
        this.painterIdentifier = new PainterIdentifier();
    }

    public Painter(String name, String origin, String birthDate, String deathDate) {
        this.painterIdentifier = new PainterIdentifier();
        this.name = name;
        this.origin = origin;
        this.birthDate = birthDate;
        this.deathDate = deathDate;
    }
}
