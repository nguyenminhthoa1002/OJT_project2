package project.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;
import project.model.entity.Review;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review,Integer> {
    Page<Review>findReviewByProductId (Integer productId,Pageable pageable);
    Page<Review>findReviewByUsersUserId(Integer userId,Pageable pageable);
}
