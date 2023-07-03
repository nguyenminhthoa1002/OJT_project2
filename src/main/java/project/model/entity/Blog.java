package project.model.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "blog")
public class Blog extends BaseEntity{
    private LocalDateTime creatDate;
    @Column(columnDefinition = "text")
    private String blogImg;
    @Column(columnDefinition = "text")
    private String content;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId")
    private Users users;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "catalogOfBlogId")
    private CatalogOfBlog catalogOfBlog;
    @OneToMany(mappedBy = "blog")
    private List<CommentBlog> commentBlogList = new ArrayList<>();
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable( name = "Tag_Blog", joinColumns = @JoinColumn(name = "blogId"), inverseJoinColumns = @JoinColumn(name = "tagId"))
    private List<Tags> tagList= new ArrayList<>();
}
