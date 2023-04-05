package com.gallery.galleryservice.datalayer;

import jakarta.persistence.*;
import lombok.Data;

@Table(name="galleries")
@Entity
@Data
public class Gallery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;
    @Embedded
    private GalleryIdentifier galleryIdentifier;
    @Embedded
    private Address address;
    private String name;
    private String openFrom;
    private String openUntil;

    public Gallery(){
        this.galleryIdentifier = new GalleryIdentifier();
    }

    public Gallery(String name, String openFrom, String openUntil, String streetAddress, String city, String province, String country, String postalCode) {
        this.galleryIdentifier = new GalleryIdentifier();
        this.name = name;
        this.openFrom = openFrom;
        this.openUntil = openUntil;
        this.address = new Address(streetAddress, city, province, country, postalCode);
    }

}
