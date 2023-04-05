package com.gallery.galleryservice.datalayer;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GalleryRepository extends JpaRepository<Gallery, Integer> {
    Gallery findByGalleryIdentifier_GalleryId(String galleryId);
    //boolean existsByGalleryIdentifier_GalleryId(String galleryId);
    Gallery findByAddress(Address address);

    boolean existsByGalleryIdentifier_GalleryId(String galleryId);
    Gallery findByName(String name);
}
