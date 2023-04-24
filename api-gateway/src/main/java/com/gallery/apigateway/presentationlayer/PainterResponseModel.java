package com.gallery.apigateway.presentationlayer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//@Value
//@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PainterResponseModel {
    String painterId;
    String name;
    String origin;
    String birthDate;
    String deathDate;
}
