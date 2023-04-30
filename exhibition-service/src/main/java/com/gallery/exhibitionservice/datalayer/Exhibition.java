package com.gallery.exhibitionservice.datalayer;
import com.gallery.exhibitionservice.domainclientlayer.PaintingResponseModel;
import com.gallery.exhibitionservice.domainclientlayer.SculptureResponseModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "exhibitions")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Exhibition {
    @Id
    private String id;

    private ExhibitionIdentifier exhibitionIdentifier;
    private GalleryIdentifier galleryIdentifier;

    private String galleryName;

    private String exhibitionName;
    private int roomNumber;
    private int duration;
    private String startDay;
    private String endDay;

    private List<PaintingResponseModel> paintings;
    private List<SculptureResponseModel> sculptures;
}
