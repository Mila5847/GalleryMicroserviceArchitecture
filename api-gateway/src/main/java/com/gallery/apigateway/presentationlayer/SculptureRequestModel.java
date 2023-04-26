package com.gallery.apigateway.presentationlayer;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SculptureRequestModel {
    private String galleryId;
    private String title;
    private String material;
    private String texture;
}
