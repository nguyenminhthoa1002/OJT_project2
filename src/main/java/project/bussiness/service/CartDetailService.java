package project.bussiness.service;

import project.model.dto.request.CartDetailRequest;
import project.model.dto.response.CartDetailResponse;
import project.model.entity.Cart;
import project.model.entity.CartDetail;

import java.util.List;

public interface CartDetailService extends RootService<CartDetail, Integer, CartDetailRequest, CartDetailResponse> {
    List<CartDetail> findByCartIn(List<Cart> listCart);
}
