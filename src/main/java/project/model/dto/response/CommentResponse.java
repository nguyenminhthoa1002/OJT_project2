package project.model.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class CommentResponse extends RootResponse{
    private LocalDateTime createDate;
    private String content;
    private int userId;
}
