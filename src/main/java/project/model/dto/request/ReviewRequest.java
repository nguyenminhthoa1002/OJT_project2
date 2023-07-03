package project.model.dto.request;

import lombok.Data;


@Data
public class ReviewRequest {
    private int starPoint;
    private String commentContent;
    private int productId;
    private int status;
}
