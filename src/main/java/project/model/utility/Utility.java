package project.model.utility;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.HashMap;
import java.util.Map;

public class Utility {
    public static Pageable sort_order(Map<String,String> headers){
        if (headers.get("sortBy")!=""){
            Sort.Order order;
            if (headers.get("direction").equals("asc")){
                order = new Sort.Order(Sort.Direction.ASC,  headers.get("sortBy"));
            }else {
                order = new Sort.Order(Sort.Direction.DESC,  headers.get("sortBy"));
            }
            Pageable pageable = PageRequest.of(Integer.parseInt(headers.get("page")) , Integer.parseInt(headers.get("size")),Sort.by(order));
            return pageable;
        }else {
            Pageable pageable = PageRequest.of(Integer.parseInt(headers.get("page")) , Integer.parseInt(headers.get("size")));
            return pageable;
        }
    }
    public static Map<String, Object> returnResponse(Page page){
        Map<String,Object> result = new HashMap<>();
        result.put("Item", page.getContent());
        result.put("Size", page.getSize());
        result.put("TotalItems", page.getTotalElements());
        result.put("TotalPages", page.getTotalPages());
        return result;
    }
}
