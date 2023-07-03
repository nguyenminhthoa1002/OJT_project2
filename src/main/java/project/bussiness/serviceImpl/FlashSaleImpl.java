package project.bussiness.serviceImpl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import project.bussiness.service.FlashSaleService;
import project.model.dto.request.FlashSaleRequest;
import project.model.dto.response.FlashSaleResponse;
import project.model.entity.FlashSale;
import project.model.shopMess.Constants;
import project.repository.FlashSaleRepository;
import project.repository.ProductRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FlashSaleImpl implements FlashSaleService {
    private ProductRepository productRepo;
    private FlashSaleRepository flashSaleRepo;

    @Override
    public Map<String, Object> getPagingAndSort(Pageable pageable) {
        return null;
    }

    @Override
    public FlashSaleResponse saveOrUpdate(FlashSaleRequest rq) {
        FlashSale map = mapRequestToPoJo(rq);
        FlashSale result = flashSaleRepo.save(map);
        List<FlashSale> flashSales = findAll().stream().filter(fs -> fs.getStatus() == Constants.ONLINE).collect(Collectors.toList());
        boolean check =false;
        for (FlashSale fs : flashSales) {
                if (fs.getProduct().getId()== map.getProduct().getId()&&fs.getEndTime().compareTo(map.getStartTime())>0){
                    check =true;}
        }
        if (!check){
            FlashSaleResponse response = mapPoJoToResponse(result);
            return response;
        }else {
            return null;
        }
    }

    @Override
    public FlashSaleResponse update(Integer id, FlashSaleRequest flashSaleRequest) {
        return null;
    }

    @Override
    public ResponseEntity<?> delete(Integer id) {
        return null;
    }

    @Override
    public List<FlashSale> findAll() {
        List<FlashSale> saleList = flashSaleRepo.findAll().stream()
                .map(flashSale -> {
                    flashSale.getStatus();
                    FlashSale result = flashSaleRepo.save(flashSale);
                    return result;
                })
                .collect(Collectors.toList());
        return saleList;
    }

    @Override
    public List<FlashSaleResponse> getAllForClient() {
        List<FlashSaleResponse> responses = flashSaleRepo.findAll().stream()
                .map(this::mapPoJoToResponse)
                .filter(response -> response.getStatus() == Constants.ONLINE)
                .collect(Collectors.toList());
        return responses;
    }

    @Override
    public FlashSale findById(Integer id) {
        return null;
    }

    @Override
    public Map<String, Object> findByName(String name, Pageable pageable) {
        return null;
    }

    @Override
    public FlashSale mapRequestToPoJo(FlashSaleRequest rq) {
        FlashSale flashSale = new FlashSale();
        flashSale.setProduct(productRepo.findById(rq.getProductId()).get());
        flashSale.setName(String.format("%s%s", productRepo.findById(rq.getProductId()).get().getName(), Constants.FLASH_SALE_NAME));
        flashSale.setStartTime(rq.getStartTime());
        flashSale.setEndTime(rq.getEndTime());
        flashSale.setDiscount(rq.getDiscount());
        flashSale.setQuantity(rq.getQuantity());
        flashSale.setSold(rq.getSold());
        return flashSale;
    }

    @Override
    public FlashSaleResponse mapPoJoToResponse(FlashSale flashSale) {
        FlashSaleResponse response = new FlashSaleResponse();
        response.setName(flashSale.getName());
        response.setId(flashSale.getId());
        response.setDiscount(flashSale.getDiscount());
        response.setQuantity(flashSale.getQuantity());
        response.setImg(flashSale.getProduct().getProductImg());
        response.setPrice(flashSale.getProduct().getExportPrice());
        response.setSold(flashSale.getSold());
        response.setStatus(flashSale.getStatus());
        response.setStartTime(flashSale.getStartTime());
        response.setEndTime(flashSale.getEndTime());
        flashSaleRepo.save(flashSale);
        return response;
    }


}
