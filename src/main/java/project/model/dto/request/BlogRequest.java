package project.model.dto.request;
import lombok.Data;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class BlogRequest {
    private String name;
    private String blogImg;
    private String content;
    private int userId;
    private int status;
    private int catalogBlogId;
    private List<Integer> tagId;
    private List<Integer> catalogId;
}
