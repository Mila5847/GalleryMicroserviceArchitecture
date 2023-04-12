package com.gallery.paintingservice.presentationlayer;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PaintingRequestModel {
    private String title;
    private int yearCreated;
    private String painterId;
    private String galleryId;
}
