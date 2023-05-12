package com.gallery.apigateway.presentationlayer.exhibition;

import com.gallery.apigateway.presentationlayer.PaintingResponseModel;
import com.gallery.apigateway.presentationlayer.SculptureResponseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
