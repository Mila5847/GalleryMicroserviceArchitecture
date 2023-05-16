package com.gallery.apigateway.presentationlayer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

//@Value
//@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GalleryResponseModel extends RepresentationModel<GalleryResponseModel>{
    String galleryId;
    String name;
    String openFrom;
    String openUntil;
    String streetAddress;
    String city;
    String province;
    String country;
    String postalCode;
}
