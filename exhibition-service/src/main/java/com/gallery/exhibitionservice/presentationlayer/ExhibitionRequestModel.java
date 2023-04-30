package com.gallery.exhibitionservice.presentationlayer;

import com.gallery.exhibitionservice.domainclientlayer.PaintingResponseModel;
import com.gallery.exhibitionservice.domainclientlayer.SculptureResponseModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
@AllArgsConstructor
public class ExhibitionRequestModel {
    String galleryId;

    String exhibitionName;
    int roomNumber;
    int duration;
    String startDay;
    String endDay;

    List<PaintingResponseModel> paintings;
    List<SculptureResponseModel> sculptures;
}
