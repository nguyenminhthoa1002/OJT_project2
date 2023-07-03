package project.model.dto.response;

import lombok.Data;

@Data
public class ReviewResponse extends RootResponse{
    private String firstName;
    private String lastName;
    private String avatar;
    private int starPoint;
    private String comment;
    private String daysAgo;
}
