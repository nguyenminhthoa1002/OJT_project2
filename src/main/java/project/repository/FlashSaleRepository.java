package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.model.entity.FlashSale;

import java.util.List;

@Repository
public interface FlashSaleRepository extends JpaRepository<FlashSale,Integer> {
    FlashSale findByStatusAndProduct_Id(Integer status,Integer productId);
    List<FlashSale> findByStatus(Integer status);
    boolean existsByStatusAndProduct_Id(Integer status,Integer productId);
}
