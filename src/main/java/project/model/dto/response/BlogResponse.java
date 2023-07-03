package project.model.dto.response;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class BlogResponse extends RootResponse{
    private LocalDateTime creatDate;
    private String blogImg;
    private String content;
    private String userName;
    private int countComment;
    private List<CommentResponse> listCommentResponse = new ArrayList<>();
    private String catalogBlogName;
    private List<String> tagName;
}

