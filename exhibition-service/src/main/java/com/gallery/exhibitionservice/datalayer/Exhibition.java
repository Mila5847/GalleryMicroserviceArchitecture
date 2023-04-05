package com.gallery.exhibitionservice.datalayer;

import jakarta.persistence.*;
import lombok.Data;

/*@Table(name="exhibitions")
@Entity
@Data
public class Exhibition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Embedded
    private ExhibitionIdentifier exhibitionIdentifier;

    @Embedded
    private GalleryIdentifier galleryIdentifier;
    private String name;
    private int roomNumber;
    private int duration;
    private String startDay;
    private String endDay;

    public Exhibition() {
        this.exhibitionIdentifier = new ExhibitionIdentifier();
    }

    public Exhibition(String name, int roomNumber, int duration, String startDay, String endDay) {
        this.exhibitionIdentifier = new ExhibitionIdentifier();
        this.name = name;
        this.roomNumber = roomNumber;
        this.duration = duration;
        this.startDay = startDay;
        this.endDay = endDay;
    }
}*/
