package project.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.model.entity.Brand;
import project.model.entity.Product;

import java.util.List;
import java.util.Set;

@Repository
public interface BrandRepository extends JpaRepository<Brand,Integer> {
    Set<Brand> findByProductListIn(List<Product> listProduct);
    Page<Brand>findByNameContaining(String name, Pageable pageable);
}
