package project.model.dto.response;

import lombok.Data;

@Data
public class CatalogResponse extends RootResponse {
    private int quantityTypeOfProduct;
    private int totalProductInCatalog;
}
