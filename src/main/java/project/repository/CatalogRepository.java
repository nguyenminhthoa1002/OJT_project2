package project.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.model.entity.Catalog;
import project.model.entity.Product;

import java.util.List;
import java.util.Set;

@Repository
public interface CatalogRepository extends JpaRepository<Catalog,Integer> {
    Set<Catalog> findByProductListIn(List<Product> listProduct);
    Page<Catalog>findByNameContaining(String searchName, Pageable pageable);

}
