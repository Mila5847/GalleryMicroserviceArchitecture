package com.gallery.paintingservice.presentationlayer;

import lombok.*;

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
