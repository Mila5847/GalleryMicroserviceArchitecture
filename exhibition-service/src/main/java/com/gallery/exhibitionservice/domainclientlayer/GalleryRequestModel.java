package com.gallery.exhibitionservice.domainclientlayer;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GalleryRequestModel {
    private String name;
    private String openFrom;
    private String openUntil;
    private String streetAddress;
    private String city;
    private String province;
    private String country;
    private String postalCode;
}
