package project.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name="commentBlog")
public class CommentBlog extends BaseEntity{
    @Column(name="createDate")
    private LocalDateTime createDate;
    @Column(name = "content")
    private String content;
    @JoinColumn(name="blogId")
    @ManyToOne(fetch = FetchType.EAGER)
    private Blog blog;
    @JoinColumn(name="userId")
    @ManyToOne(fetch = FetchType.EAGER)
    private Users users;
}
