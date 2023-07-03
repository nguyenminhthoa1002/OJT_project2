package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.model.entity.SubcribeEmail;

@Repository
public interface SubcribeEmailRepository extends JpaRepository<SubcribeEmail,Integer> {
}
