package project.bussiness.serviceImpl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import project.bussiness.service.*;
import project.model.dto.request.CartConfirmRequest;
import project.model.dto.request.CartDetailRequest;
import project.model.dto.request.CartRequest;
import project.model.dto.response.CartDetailResponse;
import project.model.dto.response.CartPendingResponse;
import project.model.dto.response.CartResponse;
import project.model.entity.*;
import project.model.shopMess.Constants;
import project.model.shopMess.Message;
import project.model.utility.Utility;
import project.repository.*;
import project.security_jwt.CustomUserDetails;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CartImpl implements CartService {
    private CartRepository cartRepository;
    private UserRepository userRepository;
    private ProductRepository productRepository;
    private CartDetailRepository cartDetailRepository;
    private CartDetailService cartDetailService;
    private TokenLogInReposirory tokenLogInReposirory;
    private FlashSaleRepository flashSaleRepo;
    private FlashSaleService flashSaleService;
    private CouponRepository couponRepository;
    private CouponService couponService;
    private ProductService productService;

    @Override
    public Map<String, Object> getPagingAndSort(Pageable pageable) {
        Page<CartResponse> responses = cartRepository.findAll(pageable).map(this::mapPoJoToResponse);
        Map<String, Object> result = Utility.returnResponse(responses);
        return result;
    }

    @Override
    public CartResponse saveOrUpdate(CartRequest cartRequest) {
        return null;
    }

    @Override
    public CartResponse update(Integer id, CartRequest cartRequest) {
        return null;
    }

    @Override
    public ResponseEntity<?> delete(Integer id) {
        return null;
    }

    @Override
    public List<Cart> findAll() {
        return null;
    }

    @Override
    public List<CartResponse> getAllForClient() {

        return null;
    }

    @Override
    public Cart findById(Integer id) {
        return null;
    }

    @Override
    public Map<String, Object> findByName(String name, Pageable pageable) {
        return null;
    }

    @Override
    public Cart mapRequestToPoJo(CartRequest cartRequest) {
        return null;
    }

    @Override
    public CartResponse mapPoJoToResponse(Cart cart) {
        CartResponse response = new CartResponse();
        response.setId(cart.getId());
        response.setName(cart.getName());
        response.setStatus(cart.getStatus());
        List<CartDetailResponse> responseList;
        if (cart.getStatus() == 0) {
            List<CartDetail> newDetailList = new ArrayList<>();
            flashSaleService.findAll();// cập nhập lại toàn bộ trạng thái flash sale;
            List<CartDetail> detailList = cart.getCartDetailList();
            Iterator<CartDetail> iterator = detailList.iterator();
            while (iterator.hasNext()) {
                    CartDetail dt =iterator.next();
                    boolean checkFlashSale = flashSaleRepo.existsByStatusAndProduct_Id(1, dt.getProduct().getId());
                    List<CartDetail> countCartDetailByProductId = cartDetailRepository.findByProduct_IdAndCart_Id(dt.getProduct().getId(), cart.getId());
                    if (checkFlashSale) {
                        FlashSale flashSale = flashSaleRepo.findByStatusAndProduct_Id(1, dt.getProduct().getId());
                        // co sale
                        if (countCartDetailByProductId.size() == 1) {
                            if (!dt.getName().contains(Constants.FLASH_SALE_NAME) && dt.getQuantity() == 1) {
                                dt.setName(String.format("%s%s", dt.getName(), Constants.FLASH_SALE_NAME));
                                dt.setDiscount(dt.getPrice() * flashSale.getDiscount() / 100);
                                dt.setPrice(dt.getPrice() * (100 - flashSale.getDiscount()) / 100);
                                cartDetailRepository.save(dt);
                            } else if (!dt.getName().contains(Constants.FLASH_SALE_NAME)) {
                                CartDetail newFlashSale = new CartDetail();
                                newFlashSale.setName(String.format("%s%s", dt.getName(), Constants.FLASH_SALE_NAME));
                                newFlashSale.setPrice(dt.getPrice() * (100 - flashSale.getDiscount()) / 100);
                                newFlashSale.setProduct(dt.getProduct());
                                newFlashSale.setDiscount(dt.getProduct().getExportPrice() * flashSale.getDiscount() / 100);
                                newFlashSale.setStatus(1);
                                newFlashSale.setCart(cart);
                                newFlashSale.setQuantity(1);
                                dt.setQuantity(dt.getQuantity() - 1);
                                cartDetailRepository.save(dt);
                                cartDetailRepository.save(newFlashSale);

                            }
                        }
                    } else {  // khong sale
                        if (countCartDetailByProductId.size() ==1&& dt.getName().contains(Constants.FLASH_SALE_NAME)) {
                            if (countCartDetailByProductId.get(0).getId()==dt.getId()){
                                dt.setPrice(dt.getProduct().getExportPrice() * (100 - dt.getProduct().getDiscount()) / 100);
                                dt.setName(dt.getProduct().getName());
                                cartDetailRepository.save(dt);
                            }else {
                                iterator.remove();
                            }
                        } else if (countCartDetailByProductId.size() ==2) {
                            if (dt.getName().contains(Constants.FLASH_SALE_NAME)) {
                                iterator.remove();
                                cartDetailRepository.delete(dt);
                            } else {
                                CartDetail ct = countCartDetailByProductId.stream().filter(cartDetail -> cartDetail.getName().contains(Constants.FLASH_SALE_NAME)).collect(Collectors.toList()).get(0);
                                dt.setQuantity(dt.getQuantity() + 1);
                                cartDetailRepository.save(dt);
                                cartDetailRepository.delete(ct);
                            }
                        }
                    }
            }

            responseList = cartDetailRepository.findByCart_Id(cart.getId()).stream().map(cartDetail -> {
                CartDetailResponse rp = cartDetailService.mapPoJoToResponse(cartDetail);
                return rp;
            }).collect(Collectors.toList());
        } else {
            responseList = cart.getCartDetailList().stream().map(cartDetail -> {
                CartDetailResponse rp = cartDetailService.mapPoJoToResponse(cartDetail);
                return rp;
            }).collect(Collectors.toList());
        }
        for (CartDetailResponse rp : responseList) {
            response.setSubTotal(response.getSubTotal() + rp.getQuantity() * rp.getPrice());
        }
        response.setDetailResponses(responseList);
        response.setDiscount(cart.getDiscount());
        response.setShipping(cart.getShipping());
        response.setTax(cart.getTax());
        CustomUserDetails userIsLoggingIn = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users users = userRepository.findUsersByUserName(userIsLoggingIn.getUsername());
        response.setFirstName(users.getFirstName());
        response.setLastName(users.getLastName());
        response.setEmail(users.getEmail());
        response.setPhone(users.getPhone());
        response.setAddress(users.getAddress());
        response.setCity(users.getCity());
        response.setCountry(users.getCountry());
        response.setState(users.getState());
        response.setTotal(response.getSubTotal() + response.getShipping() + response.getTax() - response.getDiscount());
        return response;
    }

    @Override
    public List<Cart> findByCreatDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return cartRepository.findByCreatDateBetween(startDate, endDate);
    }

    @Override
    public ResponseEntity<?> addToCart(CartDetailRequest cartDetailRequest, String action) {
        CustomUserDetails userIsLoggingIn = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users users = userRepository.findUsersByUserName(userIsLoggingIn.getUsername());
        if (tokenLogInReposirory.existsByUsers_UserId(users.getUserId())) {
            flashSaleService.findAll();// cập nhập lại toàn bộ trạng thái flash sale;
            Product product = productRepository.findById(cartDetailRequest.getProductId()).get();
            Cart pendingCart = cartRepository.findByUsers_UserIdAndStatus(users.getUserId(), 0).get(0);
            boolean checkFlashSale = flashSaleRepo.existsByStatusAndProduct_Id(1, product.getId());
            List<CartDetail> cartDetail = cartDetailRepository.findByProduct_IdAndCart_Id(product.getId(), pendingCart.getId());
            CartDetail newDetail = new CartDetail();
            newDetail.setProduct(product);
            newDetail.setCart(pendingCart);
            newDetail.setStatus(1);
            if (checkFlashSale) {
                FlashSale flashSale = flashSaleRepo.findByStatusAndProduct_Id(1, product.getId());
                List<Cart> boughtCart = cartRepository.findByStatusAndUsers_UserIdAndCreatDateBetween(1, users.getUserId(), flashSale.getStartTime(), flashSale.getEndTime());
                List<CartDetail> boughtDetail = cartDetailRepository.findByCartIn(boughtCart);
                Product checkBought = productRepository.findByIdAndCartDetailListIn(product.getId(), boughtDetail);
                boolean check = false;
                if (checkBought != null) {
                    if (cartDetail.size() == 1) {
                        cartDetail.get(0).setQuantity(cartDetail.get(0).getQuantity() + cartDetailRequest.getQuantity());
                        cartDetailRepository.save(cartDetail.get(0));
                    } else {
                        newDetail.setQuantity(cartDetailRequest.getQuantity());
                        newDetail.setPrice(product.getExportPrice() * (100 - product.getDiscount()) / 100);
                        newDetail.setName(product.getName());
                        newDetail.setDiscount(product.getDiscount() * product.getExportPrice() / 100);
                        cartDetailRepository.save(newDetail);
                    }
                    return ResponseEntity.ok().body(Message.ADD_TO_CART_SUCCESS);
                } else {
                    if (cartDetail.size() == 1) {
                        if (cartDetail.get(0).getName().contains(Constants.FLASH_SALE_NAME)) {// sản phẩm đã được thêm LẦN ĐẦU TIÊN TRONG thời gian bắt đầu sale -> tạo 1 oderDetail giá thường
                            newDetail.setQuantity(cartDetailRequest.getQuantity());
                            newDetail.setPrice(product.getExportPrice() * (100 - product.getDiscount()) / 100);
                            newDetail.setName(product.getName());
                            newDetail.setDiscount(product.getDiscount() * product.getExportPrice() / 100);
                        } else {// sản phẩm đã được thêm vào giỏ hàng TRƯỚC thời gian diễn ra sale.-> tạo 1 oderDetail với giá sale
                            check = true;
                        }
                    } else if (cartDetail.size() == 2) { // kich thuoc list cartdetal theo san pham sale = 2(gom ca oderdetail sale và oderDetail thuong)
                        newDetail = cartDetail.stream().filter(dt -> !dt.getName().contains(Constants.FLASH_SALE_NAME)).collect(Collectors.toList()).get(0);
                        if (action.equals("create")) {
                            newDetail.setQuantity(newDetail.getQuantity() + cartDetailRequest.getQuantity());
                        } else if (action.equals("edit")) {
                            newDetail.setQuantity(cartDetailRequest.getQuantity());
                        } else {
                            return ResponseEntity.badRequest().body(Message.ERROR_400);
                        }
                    } else {
                        check = true;
                    }
                    if (check) {
                        newDetail.setQuantity(1);
                        newDetail.setPrice(product.getExportPrice() * (100 - flashSale.getDiscount()) / 100);
                        newDetail.setDiscount(product.getExportPrice() * flashSale.getDiscount() / 100);
                        newDetail.setName(String.format("%s%s", product.getName(), Constants.FLASH_SALE_NAME));
                    }
                    cartDetailRepository.save(newDetail);
                    return ResponseEntity.ok().body(Message.ADD_TO_CART_SUCCESS);
                }
            } else {
                if (cartDetail.size() != 0) {
                    if (action.equals("create")) {
                        cartDetail.get(0).setQuantity(cartDetail.get(0).getQuantity() + cartDetailRequest.getQuantity());
                        cartDetail.get(0).setPrice(product.getExportPrice() * (100 - product.getDiscount()) / 100);
                    } else if (action.equals("edit")) {
                        cartDetail.get(0).setQuantity(cartDetailRequest.getQuantity());
                    }
                    cartDetailRepository.save(cartDetail.get(0));
                    return ResponseEntity.ok().body(Message.ADD_TO_CART_SUCCESS);
                } else {
                    newDetail.setDiscount(product.getDiscount() * product.getExportPrice() / 100);
                    newDetail.setQuantity(cartDetailRequest.getQuantity());
                    newDetail.setPrice(product.getExportPrice() * (100 - product.getDiscount()) / 100);
                    newDetail.setName(product.getName());
                    cartDetailRepository.save(newDetail);
                    return ResponseEntity.ok().body(Message.ADD_TO_CART_SUCCESS);
                }
            }
        } else {
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }

    @Override
    public Page<CartResponse> findByStatusIn(Integer status, Pageable pageable) {
        return null;
    }

    @Override
    public CartPendingResponse showCartPending() {
        couponService.findAll();
        flashSaleService.findAll();
        CustomUserDetails customUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Cart> cartList = cartRepository.findByUsers_UserIdAndStatus(customUser.getUserId(), 0);
        List<CartDetail> cartDetailList = cartDetailRepository.findByCartIn(cartList);
        List<CartDetailResponse> cartDetailResponseList = cartDetailList.stream()
                .map(cartDetail -> cartDetailService.mapPoJoToResponse(cartDetail))
                .collect(Collectors.toList());

        CartPendingResponse cartPendingResponse = new CartPendingResponse();
        for (CartDetailResponse rp : cartDetailResponseList) {
            cartPendingResponse.setSummary(cartPendingResponse.getSummary() + rp.getQuantity() * rp.getPrice());
        }
        cartPendingResponse.setCartDetailResponseList(cartDetailResponseList);
        return cartPendingResponse;
    }

    @Override
    public Map<String, Object> getAllForClient(Pageable pageable) {
        CustomUserDetails customUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Page<CartResponse> responses = cartRepository.findByUsers_UserIdAndStatusIsNot(customUser.getUserId(), 0, pageable).map(this::mapPoJoToResponse);
        Map<String, Object> result = Utility.returnResponse(responses);
        return result;
    }

    @Override
    public ResponseEntity<?> changeStatus(Integer cartId, Integer status) {
        try {
            Cart cart = cartRepository.findById(cartId).get();
            if (cart != null && cart.getStatus() > Constants.CART_STATUS_PENDING && cart.getStatus() < Constants.CART_STATUS_DONE) {
                cart.setStatus(status);
                cartRepository.save(cart);
                return ResponseEntity.ok(Message.SUCCESS);
            } else {
                return ResponseEntity.badRequest().body(Message.ERROR_400);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }

    @Override
    public CartResponse showCartCheckout(int couponId) {
        couponService.findAll();
        flashSaleService.findAll();
        CustomUserDetails customUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users users = userRepository.findUsersByUserName(customUser.getUsername());
        List<Cart> cartList = cartRepository.findByUsers_UserIdAndStatus(customUser.getUserId(), 0);
        CartResponse cartResponse = mapPoJoToResponse(cartList.get(0));
        CartPendingResponse cartPendingResponse = showCartPending();
        cartResponse.setSubTotal(cartPendingResponse.getSummary());
        float discount = 0;
        if (couponId != 0 && couponRepository.findById(couponId).get().getStatus() == 1) {
            discount = cartPendingResponse.getSummary() * couponRepository.findById(couponId).get().getCouponValue() / 100;
            DecimalFormat df = new DecimalFormat("#.##");
            String result = df.format(discount);
            discount = Float.parseFloat(result);
        }
        cartList.get(0).setDiscount(discount);
        cartRepository.save(cartList.get(0));
        cartResponse.setDiscount(discount);
        if (cartPendingResponse.getSummary() < 2000000) {
            cartResponse.setShipping(15000);
        } else if (2000000 <= cartPendingResponse.getSummary() && cartPendingResponse.getSummary() <= 5000000) {
            cartResponse.setShipping(25000);
        } else {
            cartResponse.setShipping(35000);
        }
        float tax = cartPendingResponse.getSummary() * 10 / 100;
        DecimalFormat df = new DecimalFormat("#.##");
        String result = df.format(tax);
        tax = Float.parseFloat(result);
        cartResponse.setTax(tax);
        float total = cartPendingResponse.getSummary() - discount + cartResponse.getShipping() + tax;
        String resultTotal = df.format(total);
        total = Float.parseFloat(resultTotal);
        cartResponse.setTotal(total);
        cartResponse.setUsers(users);
        return cartResponse;

    }

    @Override
    public ResponseEntity<?> checkout(CartConfirmRequest cartConfirmRequest) {
        try {
            CustomUserDetails customUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Users users = userRepository.findUsersByUserName(customUser.getUsername());
            productService.findAll();
            flashSaleService.findAll();
            CartResponse cartResponse = showCartCheckout(cartConfirmRequest.getCouponId());
            Cart cart = cartRepository.findById(cartResponse.getId()).get();
            cart.setFirstName(cartConfirmRequest.getFirstName());
            cart.setLastName(cartConfirmRequest.getLastName());
            cart.setAddress(cartConfirmRequest.getAddress());
            cart.setCity(cartConfirmRequest.getCity());
            cart.setCountry(cartConfirmRequest.getCountry());
            cart.setState(cartConfirmRequest.getState());
            cart.setEmail(cartConfirmRequest.getEmail());
            cart.setPhone(cartConfirmRequest.getPhone());
            cart.setNote(cartConfirmRequest.getNote());
            cart.setDiscount(cartResponse.getDiscount());
            cart.setTax(cartResponse.getTax());
            cart.setShipping(cartResponse.getShipping());
            cart.setTotal(cartResponse.getTotal());
            cart.setUsers(cartResponse.getUsers());
            cart.setStatus(1);
            cart.setCreatDate(LocalDateTime.now());
            List<CartDetailResponse> cartDetailList = cartResponse.getDetailResponses();
            Set<Integer> productId = new HashSet<>();
            for (CartDetailResponse cdr : cartDetailList) {
                Product product = productRepository.findById(cdr.getProductId()).get();
                productId.add(cdr.getProductId());
                if (product.getProductQuantity() > cdr.getQuantity()) {
                    product.setProductQuantity(product.getProductQuantity() - cdr.getQuantity());
                    productRepository.save(product);
                } else {
                    return ResponseEntity.badRequest().body("Sold out");
                }
            }

            for (Integer element : productId) {
                FlashSale flashSale = flashSaleRepo.findByStatusAndProduct_Id(1, element);
                if (flashSale != null) {
                    flashSale.setSold(flashSale.getSold() + 1);
                    flashSaleRepo.save(flashSale);
                }
            }
            cartRepository.save(cart);

            Coupon coupon = couponRepository.findById(cartConfirmRequest.getCouponId()).get();
            if (coupon.getQuantity() != 0) {
                coupon.setQuantity(coupon.getQuantity() - 1);
                couponRepository.save(coupon);
            }

            Cart newCart = new Cart();
            newCart.setUsers(users);
            newCart.setStatus(0);
            cartRepository.save(newCart);
            return ResponseEntity.ok().body(Message.SUCCESS);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }
}
