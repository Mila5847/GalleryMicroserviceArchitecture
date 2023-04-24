package com.gallery.apigateway.presentationlayer;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PainterRequestModel {
    private String name;
    private String origin;
    private String birthDate;
    private String deathDate;
}
