package com.gallery.exhibitionservice.presentationlayer;

import com.gallery.exhibitionservice.domainclientlayer.PaintingResponseModel;
import com.gallery.exhibitionservice.domainclientlayer.SculptureResponseModel;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Builder
public class ExhibitionResponseModel {

    private String exhibitionId;
    private String galleryId;

    private String galleryName;

    private String exhibitionName;
    private int roomNumber;
    private int duration;
    private String startDay;
    private String endDay;

    private List<PaintingResponseModel> paintings;
    private List<SculptureResponseModel> sculptures;
}
