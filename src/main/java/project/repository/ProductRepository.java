package project.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;
import project.model.entity.CartDetail;
import project.model.entity.Location;
import project.model.entity.Product;

import java.util.Date;
import java.util.List;


@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByCartDetailListIn(List<CartDetail> listCartDetail);
    Page<Product> findByNameContaining(String searchName, Pageable pageable);
    int countByCatalog_Id(int catalogId);
    List<Product> findByLocationIn(List<Location> listLocation);
    Product findByIdAndCartDetailListIn(int id, List<CartDetail> cartDetailList);
    List<Product>findByCatalog_IdAndCartDetailListIn(int catalog_id, List<CartDetail> cartDetailList);
    @Query(value = "select w.productId from wish w", nativeQuery = true)
    List<Integer> likeProduct();
}
