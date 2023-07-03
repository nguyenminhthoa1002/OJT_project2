package project.bussiness.serviceImpl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import project.bussiness.service.CouponService;
import project.bussiness.service.UserService;
import project.model.dto.request.CouponRequest;
import project.model.dto.response.CouponResponse;
import project.model.dto.response.UserResponse;
import project.model.entity.Coupon;
import project.model.entity.Slider;
import project.model.shopMess.Constants;
import project.model.shopMess.Message;
import project.model.utility.Utility;
import project.repository.CouponRepository;
import project.repository.UserRepository;
import project.security_jwt.CustomUserDetails;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CouponImpl implements CouponService {
    private CouponRepository couponRepo;
    private UserRepository userRepository;

    @Override
    public Map<String, Object> getPagingAndSort(Pageable pageable) {
        findAll();
        Page<Coupon> page = couponRepo.findAll(pageable);
        Map<String, Object> result = Utility.returnResponse(page);
        return result;
    }

    @Override
    public CouponResponse saveOrUpdate(CouponRequest couponRequest) {
        if (couponRepo.findByNameAndUsers_UserId(couponRequest.getName(), couponRequest.getUserId()) == null) {
            Coupon coupon = new Coupon();
            coupon.setName(couponRequest.getName());
            coupon.setCouponValue(couponRequest.getCouponValue());
            coupon.setStatus(1);
            coupon.setStartDate(couponRequest.getStartDate());
            coupon.setEndDate(couponRequest.getEndDate());
            if (couponRequest.getUserId() == 0) {
                coupon.setUsers(userRepository.findById(1).get());
            } else {
                coupon.setUsers(userRepository.findById(couponRequest.getUserId()).get());
            }
            coupon.setQuantity(couponRequest.getQuantity());
            couponRepo.save(coupon);
            return mapPoJoToResponse(coupon);
        }
        return null;
    }

    @Override
    public CouponResponse update(Integer id, CouponRequest couponRequest) {
        Coupon coupon = findById(id);
        coupon.setName(coupon.getName());
        coupon.setCouponValue(couponRequest.getCouponValue());
        coupon.setStartDate(couponRequest.getStartDate());
        coupon.setEndDate(couponRequest.getEndDate());
        if (LocalDateTime.now().compareTo(couponRequest.getStartDate()) < 0 || LocalDateTime.now().compareTo(couponRequest.getEndDate()) > 0) {
            coupon.setStatus(Constants.OFFLINE);
        } else {
            coupon.setStatus(Constants.ONLINE);
        }
        coupon.setUsers(coupon.getUsers());
        coupon.setQuantity(coupon.getQuantity());
        couponRepo.save(coupon);
        List<Coupon> list = couponRepo.findByName(coupon.getName());
        for (Coupon cp : list) {
            cp.setCouponValue(couponRequest.getCouponValue());
            cp.setStartDate(couponRequest.getStartDate());
            cp.setEndDate(couponRequest.getEndDate());
            cp.setStatus(coupon.getStatus());
            couponRepo.save(cp);
        }
        return mapPoJoToResponse(coupon);
    }

    @Override
    public ResponseEntity<?> delete(Integer id) {
        try {
            Coupon coupon = findById(id);
            coupon.setStatus(0);
            couponRepo.save(coupon);
            List<Coupon> couponList = couponRepo.findByName(coupon.getName());
            for (Coupon cp : couponList) {
                cp.setStatus(0);
                couponRepo.save(cp);
            }
            return ResponseEntity.ok().body(Message.DELETE_SUCCESS);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }

    @Override
    public List<Coupon> findAll() {
        for (Coupon cp : couponRepo.findAll()) {
            if (LocalDateTime.now().compareTo(cp.getEndDate()) > 0 || cp.getQuantity()==0) {
                cp.setStatus(0);
                couponRepo.save(cp);
            }
        }
        return couponRepo.findAll();
    }

    @Override
    public List<CouponResponse> getAllForClient() {
        findAll();
        CustomUserDetails customUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<CouponResponse> responseList = couponRepo.findByStatusAndUsers_UserId(Constants.ONLINE, customUser.getUserId())
                .stream()
                .map(this::mapPoJoToResponse)
                .filter(response -> response.getStatus() == Constants.ONLINE)
                .collect(Collectors.toList());
        return responseList;
    }

    @Override
    public Coupon findById(Integer id) {
        return couponRepo.findById(id).get();
    }

    @Override
    public Map<String, Object> findByName(String name, Pageable pageable) {
        findAll();
        Page<Coupon> page = couponRepo.findByNameContaining(name, pageable);
        Map<String, Object> result = Utility.returnResponse(page);
        return result;
    }

    @Override
    public Coupon mapRequestToPoJo(CouponRequest couponRequest) {
        return null;
    }

    @Override
    public CouponResponse mapPoJoToResponse(Coupon coupon) {
        CouponResponse response = new CouponResponse();
        response.setId(coupon.getId());
        response.setName(coupon.getName());
        response.setCouponValue(coupon.getCouponValue());
        Duration duration = Duration.between(LocalDateTime.now(), coupon.getEndDate());
        long seconds = duration.getSeconds();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM dd, yyyy HH:mm");
        String str = String.format("%d ngày %d giờ %d phút",
                seconds / 86400,
                (seconds % 86400) / 3600,
                ((seconds % 86400) % 3600) / 60);
        response.setExpiration(str);
        if (LocalDateTime.now().compareTo(coupon.getStartDate()) < 0 || LocalDateTime.now().compareTo(coupon.getEndDate()) > 0) {
            coupon.setStatus(Constants.OFFLINE);
        } else {
            coupon.setStatus(Constants.ONLINE);
        }
        couponRepo.save(coupon);
        response.setStatus(coupon.getStatus());
        return response;
    }
}
