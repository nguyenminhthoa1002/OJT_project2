package project.bussiness.serviceImpl;

import lombok.AllArgsConstructor;
import org.apache.commons.math3.analysis.function.Add;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import project.bussiness.service.ReportService;
import project.model.dto.response.AddressRevenue;
import project.model.dto.response.ProductReportByBrand;
import project.model.dto.response.ProductReportByCatalog;
import project.model.dto.response.ProductReportByLocation;
import project.model.dto.response.*;
import project.model.entity.Cart;
import project.model.entity.CartDetail;
import project.model.entity.ExcelExport;
import project.model.shopMess.Constants;
import project.model.shopMess.Message;
import project.repository.CartDetailRepository;
import project.repository.CartRepository;
import project.repository.ReportRepository;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReportImpl implements ReportService {
    private CartRepository cartRepo;
    private CartDetailRepository cartDetailRepo;
    private ExcelExport export;
    private ReportRepository reportRepo;

    @Override
    public ResponseEntity<?> reportByAddress(Map<String, String> header, HttpServletResponse response) {
        try {
            LocalDateTime start = LocalDateTime.parse(header.get("start"));
            LocalDateTime end = LocalDateTime.parse(header.get("end"));
            String reportTime = header.get("reportTime");
            String city = header.get("city");
            List<Object[]> objects= new ArrayList<>();
            switch (reportTime){
                case Constants.DAY:
                    objects=reportRepo.find_by_day_address(start.toLocalDate(),end.toLocalDate(),city,Constants.CART_STATUS_DONE);
                    break;
                case Constants.WEEK:
                    objects=reportRepo.find_by_week_address(start.toLocalDate(),end.toLocalDate(),city,Constants.CART_STATUS_DONE);
                    break;
                case Constants.MONTH:
                    objects=reportRepo.find_by_month_address(start.toLocalDate(),end.toLocalDate(),city,Constants.CART_STATUS_DONE);
                    break;
                default: break;
            }
            if (header.get("export").equals("excel")){
                AddressRevenue aR = new AddressRevenue();
                export.setData(objects);
                export.export(response,aR);
            }
            List<AddressRevenue> result = objects.stream().map(obj -> {
                String date = obj[0].toString();
                float totalTax = Float.parseFloat(obj[1].toString()) ;
                float totalShip = Float.parseFloat(obj[2].toString());
                float totalDiscount = Float.parseFloat(obj[3].toString());
                float total = Float.parseFloat(obj[4].toString());
                int numberOder =Integer.parseInt(obj[5].toString());
                String address=obj[6].toString();
                return new AddressRevenue(date, totalTax, totalShip,totalDiscount,total,numberOder,address);
            }).collect(Collectors.toList());
            return new ResponseEntity<>(result,HttpStatus.OK);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }

    @Override
    public List<ProductReportByCatalog> reportByCatalog(int status, int catId, LocalDateTime creDate, LocalDateTime endTime) {
        List<Cart> list = cartRepo.findCartByStatusAndCreatDateBetween(status, creDate, endTime);
        List<CartDetail> detailList = cartDetailRepo.findByCartIn(list);
        Map<Integer, ProductReportByCatalog> result = detailList.stream()
                .filter(cartDetail -> cartDetail.getProduct().getCatalog().getId() == catId)
                .collect(Collectors.toMap(cd -> cd.getProduct().getId(),
                        cd -> new ProductReportByCatalog(cd.getProduct().getId(),
                                cd.getProduct().getName(),
                                cd.getPrice() * cd.getQuantity(),
                                cd.getQuantity(),
                                cd.getDiscount(),
                                cd.getProduct().getCatalog().getName(),
                                cd.getPrice() * cd.getQuantity() + cd.getDiscount() * cd.getQuantity()
                        ),
                        (pr1, pr2) -> {
                            pr1.setQuantitySales(pr1.getQuantitySales() + pr2.getQuantitySales());
                            pr1.setRealRevenue(pr1.getRealRevenue() + pr2.getRealRevenue());
                            pr1.setRevenue(pr1.getRevenue() + pr2.getRevenue());
                            return pr1;
                        }
                ));
        return new ArrayList<>(result.values());
    }

    @Override
    public List<ProductReportByBrand> reportByBrand(int status, int bradId, LocalDateTime createDate, LocalDateTime endDate) {
        List<Cart> list = cartRepo.findCartByStatusAndCreatDateBetween(status, createDate, endDate);
        List<CartDetail> detailList = cartDetailRepo.findByCartIn(list);
        Map<Integer,ProductReportByBrand>result=detailList.stream()
                .filter(cartDetail -> cartDetail.getProduct().getBrand().getId()==bradId)
                .collect(Collectors.toMap(cartDetail -> cartDetail.getProduct().getId(),
                        cartDetail -> new ProductReportByBrand(cartDetail.getProduct().getId(),
                                cartDetail.getProduct().getName(),
                                cartDetail.getQuantity(),
                                cartDetail.getQuantity()* cartDetail.getPrice()+cartDetail.getQuantity()* cartDetail.getDiscount(),
                                cartDetail.getDiscount(),
                                cartDetail.getQuantity()*cartDetail.getPrice(),
                                cartDetail.getProduct().getBrand().getName()),
                        (pr1, pr2) -> {
                            pr1.setQuantitySales(pr1.getQuantitySales() + pr2.getQuantitySales());
                            pr1.setRealRevenue(pr1.getRealRevenue() + pr2.getRealRevenue());
                            pr1.setRevenue(pr1.getRevenue() + pr2.getRevenue());
                            return pr1;
                        }
                        ));
        return new ArrayList<>(result.values());
    }

    @Override
    public List<ProductReportByLocation> reportByLocation(int status, int locationId, LocalDateTime createDate, LocalDateTime endDate) {
        List<Cart> list = cartRepo.findCartByStatusAndCreatDateBetween(status, createDate, endDate);
        List<CartDetail> detailList = cartDetailRepo.findByCartIn(list);
        Map<Integer,ProductReportByLocation> result =detailList.stream()
                .filter(cartDetail -> cartDetail.getProduct().getLocation().getId()==locationId)
                .collect(Collectors.toMap(cd -> cd.getProduct().getId(),
                        cd ->new ProductReportByLocation(cd.getProduct().getId(),
                                cd.getProduct().getName(),
                                cd.getQuantity(),
                                cd.getQuantity()* cd.getPrice()+cd.getQuantity()* cd.getDiscount(),
                                cd.getDiscount(),
                                cd.getPrice()*cd.getQuantity(),
                                cd.getProduct().getLocation().getName()),
                        (pr1, pr2) -> {
                            pr1.setQuantitySales(pr1.getQuantitySales() + pr2.getQuantitySales());
                            pr1.setRealRevenue(pr1.getRealRevenue() + pr2.getRealRevenue());
                            pr1.setRevenue(pr1.getRevenue() + pr2.getRevenue());
                            return pr1;
                        }
                        ));
        return new ArrayList<>(result.values());
    }

    @Override
    public List<ProductByCartStatusResponse> reportByCart(int status, LocalDateTime startDate, LocalDateTime endDate) {
        List<Cart> cartList = cartRepo.findCartByStatusAndCreatDateBetween(status, startDate, endDate);
        List<CartDetail> detailList = cartDetailRepo.findByCartIn(cartList);
        Map<Integer, ProductByCartStatusResponse> resultMap = detailList.stream() // duyet mang detailList
                .collect(Collectors.toMap(cd -> cd.getProduct().getId(),//mỗi 1 phần tử detail thì chuyeenr giá trị qua product
                        cd -> new ProductByCartStatusResponse(cd.getProduct().getId(),
                                cd.getProduct().getName(),
                                cd.getProduct().getExportPrice(),
                                cd.getQuantity(),
                                cd.getProduct().getDiscount()),
                        (pr1, pr2) -> {
                            pr1.setQuantity(pr1.getQuantity() + pr2.getQuantity());
                            return pr1;
                        }
                ));


        return new ArrayList<>(resultMap.values());
    }
    @Override
    public List<ProductByCatalogByCartStt> reportProByCatalogCart(int status, int catId, LocalDateTime startDate, LocalDateTime endDate) {
        List<Cart> cartList = cartRepo.findCartByStatusAndCreatDateBetween(status, startDate, endDate);
        List<CartDetail> detailList = cartDetailRepo.findByCartIn(cartList);
        Map<Integer, ProductByCatalogByCartStt> result = detailList.stream()
                .filter(cartDetail -> cartDetail.getProduct().getCatalog().getId() == catId)
                .collect(Collectors.toMap(cd -> cd.getProduct().getId(),
                        cd -> new ProductByCatalogByCartStt(cd.getProduct().getId(),
                                cd.getProduct().getName(),
                                cd.getPrice() * cd.getQuantity(),
                                cd.getQuantity(),
                                cd.getDiscount(),
                                cd.getProduct().getCatalog().getName(),
                                cd.getPrice() * cd.getQuantity() + cd.getDiscount() * cd.getQuantity()
                        ),
                        (pr1, pr2) -> {
                            pr1.setQuantitySales(pr1.getQuantitySales() + pr2.getQuantitySales());
                            pr1.setRealRevenue(pr1.getRealRevenue() + pr2.getRealRevenue());
                            pr1.setRevenue(pr1.getRevenue() + pr2.getRevenue());
                            return pr1;
                        }
                ));
        return new ArrayList<>(result.values());

    }
    @Override
    public List<ProductByCartSttCancel> reportProByCartCancel(int status, LocalDateTime startDate, LocalDateTime endDate) {
        List<Cart> list = cartRepo.findCartByStatusAndCreatDateBetween(status, startDate, endDate);
        List<CartDetail> cartDetailList = cartDetailRepo.findByCartIn(list);
        Map<Integer, ProductByCartSttCancel> resultMap = cartDetailList.stream() // duyet mang detailList
                .collect(Collectors.toMap(cd -> cd.getProduct().getId(),//mỗi 1 phần tử detail thì chuyển giá trị qua product
                        cd -> new ProductByCartSttCancel(cd.getProduct().getId(),
                                cd.getProduct().getName(),
                                cd.getProduct().getExportPrice(),
                                cd.getQuantity(),
                                cd.getProduct().getDiscount(),
                                cd.getProduct().getCatalog().getName()),
                        (pr1, pr2) -> {
                            pr1.setQuantity(pr1.getQuantity() + pr2.getQuantity());
                            return pr1;
                        }
                ));

        return new ArrayList<>(resultMap.values());
    }

    @Override
    public ResponseEntity<?> reportRevenueAll(Map<String, String> header, HttpServletResponse response) {
        try {
            LocalDateTime start = LocalDateTime.parse(header.get("start"));
            LocalDateTime end = LocalDateTime.parse(header.get("end"));
            String reportTime = header.get("reportTime");
            List<Object[]> objects= new ArrayList<>();
            switch (reportTime){
                case Constants.DAY:
                    objects=reportRepo.find_by_day_total(start.toLocalDate(),end.toLocalDate(),Constants.CART_STATUS_DONE);
                    break;
                case Constants.WEEK:
                    objects=reportRepo.find_by_week_total(start.toLocalDate(),end.toLocalDate(),Constants.CART_STATUS_DONE);
                    break;
                case Constants.MONTH:
                    objects=reportRepo.find_by_month_total(start.toLocalDate(),end.toLocalDate(),Constants.CART_STATUS_DONE);
                    break;
                default: break;
            }
            if (header.get("export").equals("excel")){
                Revenue revenue = new Revenue();
                export.setData(objects);
                export.export(response,revenue);
            }
            List<Revenue> result = objects.stream().map(obj -> {
                String date = obj[0].toString();
                float totalTax = Float.parseFloat(obj[1].toString()) ;
                float totalShip = Float.parseFloat(obj[2].toString());
                float totalDiscount = Float.parseFloat(obj[3].toString());
                float total = Float.parseFloat(obj[4].toString());
                int numberOder =Integer.parseInt(obj[5].toString());
                return new Revenue(date, totalTax, totalShip,totalDiscount,total,numberOder);
            }).collect(Collectors.toList());
                return new ResponseEntity<>(result,HttpStatus.OK);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }
}
