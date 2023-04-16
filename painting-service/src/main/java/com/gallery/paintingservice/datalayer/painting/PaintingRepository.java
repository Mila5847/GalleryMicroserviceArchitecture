package com.gallery.paintingservice.datalayer.painting;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaintingRepository extends JpaRepository<Painting, Integer> {
    Painting findByPaintingIdentifier_PaintingId(String paintingId);
    Painting findByGalleryIdentifier_GalleryIdAndPaintingIdentifier_PaintingId(String galleryId, String paintingID);
    List<Painting> findAllByGalleryIdentifier_GalleryId(String galleryId);
    List<Painting> findAllByGalleryIdentifier_GalleryIdAndPainterIdentifier_PainterId(String galleryId, String painterId);
    //List<Painting> findAllByGalleryIdentifier_GalleryIdAndExhibitionIdentifier_ExhibitionId(String galleryId, String exhibitionId);
    //boolean existsByGalleryIdentifier_GalleryIdAndExhibitionIdentifier_ExhibitionIdAndTitle(String galleryId, String exhibitionId, String paintingTitle);
    boolean existsByPaintingIdentifier_PaintingId(String paintingId);
    //Painting findByGalleryIdentifier_GalleryIdAndExhibitionIdentifier_ExhibitionIdAndTitle(String galleryId, String exhibitionId, String title);

    Painting findByGalleryIdentifier_GalleryIdAndTitle(String galleryId, String paintingTitle);
    //Painting findByGalleryIdentifier_GalleryIdAndPaintingIdentifier_PaintingIdAndPainterIdentifier_PainterId(String galleryId, String paintingId, String painterId);
}
