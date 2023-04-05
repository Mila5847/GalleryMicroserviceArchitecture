package com.gallery.sculptureservice.datalayer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SculptureRepository extends JpaRepository<Sculpture, Integer> {
    List<Sculpture> findAllByGalleryIdentifier_GalleryId(String galleryId);
    Sculpture findByGalleryIdentifier_GalleryIdAndSculptureIdentifier_SculptureId(String galleryId, String sculptureId);
    //List<Sculpture> findAllByGalleryIdentifier_GalleryIdAndExhibitionIdentifier_ExhibitionId(String galleryId, String exhibitionId);
    boolean existsBySculptureIdentifier_SculptureId(String sculptureId);
    Sculpture findByTitle(String title);
    boolean existsByGalleryIdentifier_GalleryId(String galleryId);
    Sculpture findBySculptureIdentifier_SculptureId(String sculptureId);
    //boolean existsByGalleryIdentifier_GalleryIdAndExhibitionIdentifier_ExhibitionIdAndTitle(String galleryId, String exhibitionId, String title);
    //Sculpture findByGalleryIdentifier_GalleryIdAndExhibitionIdentifier_ExhibitionIdAndTitle(String galleryId, String exhibitionId, String title);
}
