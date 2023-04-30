package com.gallery.exhibitionservice.datalayer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExhibitionRepository extends MongoRepository<Exhibition, String> {
}
