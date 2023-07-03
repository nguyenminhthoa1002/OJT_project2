package project.bussiness.service;

import org.springframework.data.domain.Pageable;
import project.model.dto.request.ReviewRequest;
import project.model.dto.response.ReviewResponse;
import project.model.entity.Review;

import java.util.Map;

public interface ReviewService extends RootService<Review,Integer, ReviewRequest, ReviewResponse> {
    ReviewResponse addNewReview(ReviewRequest reviewRequest);
    ReviewResponse getReviewResponseById(int reviewId);
    ReviewResponse updateAdmin(Integer id, ReviewRequest reviewRequest);
    Map<String,Object> findReviewByProductId (int productId, Pageable pageable);
    Map<String,Object>findReviewByUsersUserId(int userId,Pageable pageable);

}
