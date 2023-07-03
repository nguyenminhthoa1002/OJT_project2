package project.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.model.entity.Coupon;

import java.util.List;

@Repository
public interface CouponRepository extends JpaRepository<Coupon,Integer> {
    List<Coupon> findByStatusAndUsers_UserId(Integer status,Integer userId);
    Page<Coupon> findByNameContaining(String searchName, Pageable pageable);
    List<Coupon> findByName(String name);
    Coupon findByNameAndUsers_UserId(String name, int userId);
}
