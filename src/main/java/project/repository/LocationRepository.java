package project.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.model.entity.Location;

@Repository
public interface LocationRepository extends JpaRepository<Location,Integer> {
    Page<Location>findByNameContaining(String searchName, Pageable pageable);
}
