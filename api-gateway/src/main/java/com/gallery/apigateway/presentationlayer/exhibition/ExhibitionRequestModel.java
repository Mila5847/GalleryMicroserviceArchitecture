package com.gallery.apigateway.presentationlayer.exhibition;

import com.gallery.apigateway.presentationlayer.PaintingResponseModel;
import com.gallery.apigateway.presentationlayer.SculptureResponseModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
