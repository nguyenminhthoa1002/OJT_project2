package project.bussiness.service;

import org.springframework.http.ResponseEntity;
import project.model.dto.response.*;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface ReportService {
    ResponseEntity<?> reportByAddress(Map<String, String> header, HttpServletResponse response );
    List<ProductReportByCatalog> reportByCatalog(int status, int catId, LocalDateTime creDate, LocalDateTime endTime);
    List<ProductReportByBrand> reportByBrand(int status , int bradId, LocalDateTime createDate, LocalDateTime endDate);
    List<ProductReportByLocation> reportByLocation(int status, int locationId, LocalDateTime createDate, LocalDateTime endDate);
    List<ProductByCartStatusResponse>reportByCart(int status,LocalDateTime startDate,LocalDateTime endDate);
    List<ProductByCatalogByCartStt>reportProByCatalogCart(int status , int catId, LocalDateTime startDate, LocalDateTime endDate);
    List<ProductByCartSttCancel>reportProByCartCancel(int status ,LocalDateTime startDate, LocalDateTime endDate);
    ResponseEntity<?> reportRevenueAll(Map<String, String> header, HttpServletResponse response );
}
