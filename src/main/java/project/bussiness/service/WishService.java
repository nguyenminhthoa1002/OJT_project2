package project.bussiness.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.model.dto.request.WishRequest;
import project.model.dto.response.WishResponse;
import project.model.entity.Wish;

public interface WishService extends RootService<Wish,Integer, WishRequest,WishResponse> {
    Page<WishResponse> getByUserId(Integer userId, Pageable pageable);
    Wish findByUsers_UserIdAndProduct_Id(Integer userId,Integer productId);
}
