package project.model.dto.request;

import lombok.Data;


@Data
public class CommentRequest {
    private String content;
    private int blogId;
}
