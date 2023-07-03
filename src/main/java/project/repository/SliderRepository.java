package project.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.model.entity.Slider;

@Repository
public interface SliderRepository extends JpaRepository<Slider,Integer> {
    Page<Slider> findByNameContaining(String searchName, Pageable pageable);

}
