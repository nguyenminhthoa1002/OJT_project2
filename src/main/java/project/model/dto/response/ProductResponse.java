package project.model.dto.response;

import lombok.Data;
import project.model.entity.SubImg;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class ProductResponse extends RootResponse{
    private String productDescriptions;
    private String title;
    private int discount;
    private int productQuantity;
    private String productImg;
    private float importPrice;
    private float exportPrice;
    private LocalDateTime creatDate;
    private String catalogName;
    private String brandName;
    private List<ReviewResponse> reviewList = new ArrayList<>();
    private List<SubImg> subImgList= new ArrayList<>();
    private double starPoint;
    private int countAllReview;
}
