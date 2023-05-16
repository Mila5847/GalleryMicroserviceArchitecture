package com.gallery.exhibitionservice.datalayer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


public interface ExhibitionRepository extends MongoRepository<Exhibition, String> {

    Exhibition findByExhibitionIdentifier_ExhibitionId(String exhibitionId);

    boolean existsByExhibitionIdentifier_ExhibitionId(String exhibitionId);
    boolean existsByExhibitionName(String exhibitionName);

    boolean existsByExhibitionIdentifier_ExhibitionIdAndPaintings_PaintingId(String exhibitionId, String paintingId);
    boolean existsByExhibitionIdentifier_ExhibitionIdAndSculptures_SculptureId(String exhibitionId, String sculptureId);
    boolean existsByPaintings_PaintingId(String paintingId);
    boolean existsBySculptures_SculptureId(String sculptureId);
}
