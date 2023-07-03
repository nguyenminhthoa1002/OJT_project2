package project.bussiness.serviceImpl;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import project.bussiness.service.CartDetailService;
import project.model.dto.request.CartDetailRequest;
import project.model.dto.response.CartDetailResponse;
import project.model.entity.Cart;
import project.model.entity.CartDetail;
import project.model.entity.FlashSale;
import project.model.shopMess.Constants;
import project.model.shopMess.Message;
import project.repository.CartDetailRepository;
import project.repository.CartRepository;
import project.repository.FlashSaleRepository;
import project.repository.ProductRepository;
import project.security_jwt.CustomUserDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CartDetailImpl implements CartDetailService {
    private CartDetailRepository cartDetailRepo;
    private ProductRepository productRepo;
    private CartRepository cartRepo;
    private FlashSaleRepository flashSaleRepo;
    @Override
    public Map<String, Object> getPagingAndSort(Pageable pageable) {
        return null;
    }

    @Override
    public CartDetailResponse saveOrUpdate(CartDetailRequest rq) {

        return null;
    }

    @Override
    public CartDetailResponse update(Integer id, CartDetailRequest rq) {

        return null ;
    }
    @Override
    public ResponseEntity<?> delete(Integer id) {
        try {
            CustomUserDetails customUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            List<Cart> cart =cartRepo.findByUsers_UserIdAndStatus(customUser.getUserId(),0);
            List<CartDetail> detailList=  cartDetailRepo.findByCart_Id(cart.get(0).getId())
                    .stream()
                    .filter(cartDetail -> cartDetail.getId()==id).collect(Collectors.toList());
            if (detailList.size()!=0){
                cartDetailRepo.deleteById(id);
                return ResponseEntity.ok(Message.SUCCESS);
            }else {
                return ResponseEntity.badRequest().body(Message.ERROR_NOT_IN_CART);
            }
        }catch (Exception e){
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }
    @Override
    public List<CartDetail> findAll() {
        return null;
    }

    @Override
    public List<CartDetailResponse> getAllForClient() {
        return null;
    }

    @Override
    public CartDetail findById(Integer id) {
        return null;
    }

    @Override
    public Map<String, Object> findByName(String name, Pageable pageable) {
        return null;
    }

    @Override
    public CartDetail mapRequestToPoJo(CartDetailRequest rq) {
        CartDetail cartDetail = new CartDetail();
        cartDetail.setPrice(rq.getPrice());
        cartDetail.setQuantity(rq.getQuantity());
        return cartDetail;
    }

    @Override
    public CartDetailResponse mapPoJoToResponse(CartDetail cartDetail) {
        CartDetailResponse rp=new CartDetailResponse();
        rp.setId(cartDetail.getId());
        rp.setName(cartDetail.getName());
        rp.setQuantity(cartDetail.getQuantity());
        rp.setPrice(cartDetail.getPrice());
        rp.setStatus(cartDetail.getStatus());
        rp.setProductId(cartDetail.getProduct().getId());
        return rp;
    }


    @Override
    public List<CartDetail> findByCartIn(List<Cart> listCart) {
        return cartDetailRepo.findByCartIn(listCart);
    }
}
