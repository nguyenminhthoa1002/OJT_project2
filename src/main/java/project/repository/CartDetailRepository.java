package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import project.model.entity.Cart;
import project.model.entity.CartDetail;
import project.model.entity.Product;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface CartDetailRepository extends JpaRepository<CartDetail,Integer> {
    List<CartDetail> findByCartIn(List<Cart> listCart);
    List<CartDetail> findByProduct_IdAndCart_Id(int productId, int cartId);
    List<CartDetail> findByCart_Id(Integer cartId);
    CartDetail findByProduct_Id(int productId);
    @Query(value = "select cartdetail.id, cartdetail.name, cartdetail.status, cartdetail.price," +
            "cartdetail.quantity, cartdetail.cartId, cartdetail.discount, cartdetail.productId, sum(cartdetail.price * cartdetail.quantity) as summary\n" +
            "from cartdetail\n" +
            "where cartdetail.cartId in\n" +
            "      (select cart.id from cart where cart.creatDate between :startTime and :endTime and cart.status = 1)\n" +
            "group by cartdetail.name\n" +
            "order by summary desc", nativeQuery = true)
    List<CartDetail> get_real_revenue_product_des(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

}
