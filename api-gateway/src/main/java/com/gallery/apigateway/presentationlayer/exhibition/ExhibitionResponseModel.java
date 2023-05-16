package com.gallery.apigateway.presentationlayer.exhibition;

import com.gallery.apigateway.presentationlayer.PaintingResponseModel;
import com.gallery.apigateway.presentationlayer.SculptureResponseModel;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
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
