package com.gallery.exhibitionservice.presentationlayer;

import com.gallery.exhibitionservice.domainclientlayer.PaintingResponseModel;
import com.gallery.exhibitionservice.domainclientlayer.SculptureResponseModel;
import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
public class ExhibitionRequestModel {
    String exhibitionName;
    Integer roomNumber;
    Integer duration;
    String startDay;
    String endDay;

    List<PaintingResponseModel> paintings;
    List<SculptureResponseModel> sculptures;
}
