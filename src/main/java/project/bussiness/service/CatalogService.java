package project.bussiness.service;

import project.model.dto.request.CatalogRequest;
import project.model.dto.response.CatalogResponse;
import project.model.dto.response.ProductResponse;
import project.model.entity.Catalog;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface CatalogService extends RootService<Catalog,Integer, CatalogRequest, CatalogResponse>{

    List<CatalogResponse> getListFeatured();
    List<CatalogResponse> getFeatureCatalogForScreen2(LocalDateTime startDate, LocalDateTime endDate, int size);
    List<CatalogResponse> countTypeOfProduct();
    CatalogResponse finByIdResponse(int id);
    List<CatalogResponse> getTop5CatalogsHaveTheBestRevenue(LocalDateTime startTime, LocalDateTime endTime);

}
