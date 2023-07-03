package project.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import project.model.entity.Tags;

import java.util.List;

public interface TagsRepository extends JpaRepository<Tags,Integer> {
    Page<Tags>findByNameContaining(String searchName, Pageable pageable);
    List<Tags> findByIdIn(List<Integer> listTag);//tìm tất cả đối tượng task có id trong List<Interger> truyền vào
}
