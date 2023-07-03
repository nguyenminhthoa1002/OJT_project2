package project.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.model.entity.Blog;
import project.model.entity.Tags;

import java.util.List;

@Repository
public interface BlogRepository extends JpaRepository<Blog,Integer> {
    Page<Blog>findByNameContaining(String searchName, Pageable pageable);
    List<Blog> findByCatalogOfBlog_IdAndTagListIn(int catId, List<Tags> listTag);//tìm list Blog theo Catalog id và theo tagList.
    List<Blog> findByCatalogOfBlog_Id(int catId);
    List<Blog> findByTagListIn(List<Tags> listTag);

}
