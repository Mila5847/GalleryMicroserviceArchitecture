package com.gallery.paintingservice.datalayer.painter;

import org.springframework.data.jpa.repository.JpaRepository;
public interface PainterRepository extends JpaRepository<Painter, Integer> {
    Painter findByPainterIdentifier_PainterId(String painterId);
    Boolean existsByPainterIdentifier_PainterId(String painterId);

}
