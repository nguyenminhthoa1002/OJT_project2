package project.bussiness.serviceImpl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import project.bussiness.service.ProductService;
import project.bussiness.service.WishService;
import project.model.dto.request.WishRequest;
import project.model.dto.response.ProductResponse;
import project.model.dto.response.WishResponse;
import project.model.entity.Wish;
import project.model.shopMess.Constants;
import project.repository.ProductRepository;
import project.repository.UserRepository;
import project.repository.WishRepository;

import java.util.List;
import java.util.Map;
@Service
@AllArgsConstructor
public class WishImpl implements WishService {
    private WishRepository wishRepo;
    private UserRepository userRepo;
    private ProductRepository productRepo;
    private ProductService productService;
    @Override
    public Map<String, Object> getPagingAndSort(Pageable pageable) {
        return null;
    }
    @Override
    public WishResponse saveOrUpdate(WishRequest rq) {
        Wish result= wishRepo.save(mapRequestToPoJo(rq));
        result.setStatus(Constants.ONLINE);
        WishResponse response=mapPoJoToResponse(result);
        return response;
    }
    @Override
    public WishResponse update(Integer id, WishRequest rq) {
        Wish wish =wishRepo.findById(id).get();
        Wish map = mapRequestToPoJo(rq);
        if (wish.getStatus()==Constants.ONLINE){
            map.setStatus(Constants.OFFLINE);
        }else {
            map.setStatus(Constants.ONLINE);
        }
        map.setId(id);
        Wish result= wishRepo.save(map);
        WishResponse response=mapPoJoToResponse(result);
        return response;
    }

    @Override
    public ResponseEntity<?> delete(Integer id) {
        return null;
    }
    @Override
    public List<Wish> findAll() {
        return null;
    }
    @Override
    public List<WishResponse> getAllForClient() {
        return null;
    }
    @Override
    public Wish findById(Integer id) {
        return null;
    }
    @Override
    public Map<String, Object> findByName(String name, Pageable pageable) {
        return null;
    }
    @Override
    public Wish mapRequestToPoJo(WishRequest rq) {
        Wish wish = new Wish();
        wish.setUsers(userRepo.findById(rq.getUserId()).get());
        wish.setProduct(productRepo.findById(rq.getProductId()).get());
        return wish;
    }
    @Override
    public WishResponse mapPoJoToResponse(Wish wish) {
        WishResponse response = new WishResponse();
        response.setId(wish.getId());
        response.setStatus(wish.getStatus());
        ProductResponse pro= productService.mapPoJoToResponse(wish.getProduct());
        response.setProductResponse(pro);
        return response;
    }
    @Override
    public Page<WishResponse> getByUserId(Integer userId,Pageable pageable) {
        return null;
    }
    @Override
    public Wish findByUsers_UserIdAndProduct_Id(Integer userId, Integer productId) {
        return wishRepo.findByUsers_UserIdAndProduct_Id(userId, productId);
    }
}
