package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.model.entity.Wish;

public interface WishRepository extends JpaRepository<Wish,Integer> {
    Wish findByUsers_UserIdAndProduct_Id(Integer userId,Integer productId);
}
