package com.gallery.exhibitionservice.businesslayer;

import com.gallery.exhibitionservice.datalayer.Exhibition;
import com.gallery.exhibitionservice.datalayer.ExhibitionIdentifier;
import com.gallery.exhibitionservice.datalayer.ExhibitionRepository;
import com.gallery.exhibitionservice.datalayer.GalleryIdentifier;
import com.gallery.exhibitionservice.datamapperlayer.ExhibitionResponseMapper;

import com.gallery.exhibitionservice.domainclientlayer.*;
import com.gallery.exhibitionservice.presentationlayer.ExhibitionRequestModel;
import com.gallery.exhibitionservice.presentationlayer.ExhibitionResponseModel;

import com.gallery.exhibitionservice.domainclientlayer.PaintingResponseModel;
import com.gallery.exhibitionservice.domainclientlayer.SculptureResponseModel;
import com.gallery.exhibitionservice.utils.exceptions.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExhibitionServiceImpl implements ExhibitionService {

    private final ExhibitionRepository exhibitionRepository;
    private final ExhibitionResponseMapper exhibitionResponseMapper;

    private final GalleryServiceClient galleryServiceClient;

    private final PaintingServiceClient paintingServiceClient;

    private final SculptureServiceClient sculptureServiceClient;

    @Override
    public List<ExhibitionResponseModel> getAllExhibitions() {
       // check if gallery exists
        List<Exhibition> exhibitions = exhibitionRepository.findAll();
        return exhibitionResponseMapper.entityListToResponseModelList(exhibitions);
    }

    @Override
    public ExhibitionResponseModel getExhibitionById(String exhibitionId) {
        // check if gallery exists
        Exhibition exhibition = exhibitionRepository.findByExhibitionIdentifier_ExhibitionId(exhibitionId);
        if(exhibition == null){
            throw new NotFoundException("Unknown exhibition id " + exhibitionId);
        }
        return exhibitionResponseMapper.entityToResponseModel(exhibition);
    }

    @Override
    public ExhibitionResponseModel createExhibition(String galleryId, ExhibitionRequestModel exhibitionRequestModel) {
        // orchestration pattern

        GalleryResponseModel galleryResponseModel = galleryServiceClient.getGallery(galleryId);
        if(galleryResponseModel == null){
            throw new ExistingExhibitionNotFoundException("Unknown gallery id " + galleryId);
        }

        for (PaintingResponseModel painting: exhibitionRequestModel.getPaintings()) {
            if(exhibitionRepository.existsByPaintings_PaintingId(painting.getPaintingId())){
                throw new PaintingAlreadyInExhibition("The painting with id " + painting.getPaintingId() + " is already in an exhibition");
            }
            if(!painting.getGalleryId().equals(galleryId)){
                throw new PaintingNotFromGalleryException("The painting should come from the gallery with id " + galleryId);
            }
        }

        for (SculptureResponseModel sculpture: exhibitionRequestModel.getSculptures()) {
            if(exhibitionRepository.existsBySculptures_SculptureId(sculpture.getSculptureId())){
                throw new SculptureAlreadyInExhibition("The sculpture with id " + sculpture.getSculptureId() + " is already in an exhibition");
            }
            if(!sculpture.getGalleryId().equals(galleryId)){
                throw new PaintingNotFromGalleryException("The sculpture should come from the gallery with id " + galleryId);
            }
        }

        Exhibition exhibition = new Exhibition().builder()
                .exhibitionIdentifier(new ExhibitionIdentifier())
                .galleryIdentifier(new GalleryIdentifier(galleryId))
                .galleryName("SOME GALLERY")
                .exhibitionName(exhibitionRequestModel.getExhibitionName())
                .roomNumber(exhibitionRequestModel.getRoomNumber())
                .duration(exhibitionRequestModel.getDuration())
                .startDay(exhibitionRequestModel.getStartDay())
                .endDay(exhibitionRequestModel.getEndDay())
                .paintings(exhibitionRequestModel.getPaintings())
                .sculptures(exhibitionRequestModel.getSculptures())
                .build();

        Exhibition saved = exhibitionRepository.save(exhibition);
        return exhibitionResponseMapper.entityToResponseModel(saved);

    }

    @Override
    public ExhibitionResponseModel updateExhibition(String exhibitionId, ExhibitionRequestModel exhibitionRequestModel) {
        Exhibition exhibition = exhibitionRepository.findByExhibitionIdentifier_ExhibitionId(exhibitionId);
        if(exhibition == null){
            throw new ExistingExhibitionNotFoundException("Unknown exhibition id " + exhibitionId);
        }

        for (PaintingResponseModel painting: exhibitionRequestModel.getPaintings()) {
            // only update paintings that are already in the exhibition
            if(exhibitionRepository.existsByExhibitionIdentifier_ExhibitionIdAndPaintings_PaintingId(exhibitionId, painting.getPaintingId())){
                PaintingRequestModel paintingRequestModel = PaintingRequestModel.builder()
                        .title(painting.getTitle())
                        .yearCreated(painting.getYearCreated())
                        .painterId(painting.getPainterId())
                        .galleryId(painting.getGalleryId())
                        .build();
                paintingServiceClient.updatePaintingInGallery(painting.getGalleryId(), painting.getPaintingId(), paintingRequestModel);
            }
            // else if painting not in exhibition, check if painting exists in gallery and if it does, add it to the exhibition
            else if(painting.getGalleryId().equals(exhibition.getGalleryIdentifier().getGalleryId())){
                exhibition.getPaintings().add(painting);
            }
            else{
                throw new PaintingNotFromGalleryException("The painting is not in the gallery with id " + exhibition.getGalleryIdentifier().getGalleryId());
            }
        }

        for (SculptureResponseModel sculpture: exhibitionRequestModel.getSculptures()) {
            // only update sculptures that are already in the exhibition
            if(exhibitionRepository.existsByExhibitionIdentifier_ExhibitionIdAndSculptures_SculptureId(exhibitionId, sculpture.getSculptureId())){
                SculptureRequestModel sculptureRequestModel = SculptureRequestModel.builder()
                        .galleryId(sculpture.getGalleryId())
                        .title(sculpture.getTitle())
                        .material(sculpture.getMaterial())
                        .texture(sculpture.getTexture())
                        .build();
                sculptureServiceClient.updateSculptureInGallery(sculpture.getGalleryId(), sculpture.getSculptureId(), sculptureRequestModel);
            }
            // else if sculpture not in exhibition, check if sculpture exists in gallery and if it does, add it to the exhibition
            else if(sculpture.getGalleryId().equals(exhibition.getGalleryIdentifier().getGalleryId())){
                exhibition.getSculptures().add(sculpture);
            }
            else{
                throw new SculptureNotFromGalleryException("The sculpture is not in the gallery with id " + exhibition.getGalleryIdentifier().getGalleryId());
            }
        }

        exhibition.setExhibitionName(exhibitionRequestModel.getExhibitionName());
        exhibition.setRoomNumber(exhibitionRequestModel.getRoomNumber());
        exhibition.setDuration(exhibitionRequestModel.getDuration());
        exhibition.setStartDay(exhibitionRequestModel.getStartDay());
        exhibition.setEndDay(exhibitionRequestModel.getEndDay());
        exhibition.setPaintings(exhibitionRequestModel.getPaintings());
        exhibition.setSculptures(exhibitionRequestModel.getSculptures());

        Exhibition saved = exhibitionRepository.save(exhibition);
        return exhibitionResponseMapper.entityToResponseModel(saved);
    }

    @Override
    public void removeExhibition(String exhibitionId) {
        Exhibition exhibition = exhibitionRepository.findByExhibitionIdentifier_ExhibitionId(exhibitionId);
        if(exhibition == null){
            throw new NotFoundException("Unknown exhibition id " + exhibitionId);
        }
        exhibitionRepository.delete(exhibition);
    }

}
