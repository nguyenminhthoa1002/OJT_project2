package project.bussiness.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import project.bussiness.service.CatalogOfBlogService;
import project.model.dto.request.CatalogOfBlogRequest;
import project.model.dto.response.CatalogOfBlogReponse;
import project.model.entity.CatalogOfBlog;
import project.model.shopMess.Message;
import project.model.utility.Utility;
import project.repository.CatalogOfBlogRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CatalogOfBlogServiceImp implements CatalogOfBlogService {
    @Autowired
    private CatalogOfBlogRepository catalogOfBlogRepository;
    @Override
    public Map<String, Object> getPagingAndSort(Pageable pageable) {
        Page<CatalogOfBlog>catalogOfBlogs=catalogOfBlogRepository.findAll(pageable);
        Map<String,Object>result =Utility.returnResponse(catalogOfBlogs);
        return result;
    }

    @Override
    public CatalogOfBlogReponse saveOrUpdate(CatalogOfBlogRequest catalogOfBlogRequest) {
        CatalogOfBlog catalogBlog = mapRequestToPoJo(catalogOfBlogRequest);
        CatalogOfBlog catalogNew =catalogOfBlogRepository.save(catalogBlog);
        CatalogOfBlogReponse catalogOfBlogReponse=mapPoJoToResponse(catalogNew);
        return catalogOfBlogReponse;
    }

    @Override
    public CatalogOfBlogReponse update(Integer id, CatalogOfBlogRequest catalogOfBlogRequest) {
        CatalogOfBlog catalog =mapRequestToPoJo(catalogOfBlogRequest);
        catalog.setId(id);
        CatalogOfBlog catalogOfBlogUpdate =catalogOfBlogRepository.save(catalog);
        CatalogOfBlogReponse catalogOfBlogReponse=mapPoJoToResponse(catalogOfBlogUpdate);
        return catalogOfBlogReponse;
    }

    @Override
    public ResponseEntity<?> delete(Integer id) {
        try {
            CatalogOfBlog catalogOfBlogDelete = catalogOfBlogRepository.findById(id).get();
            catalogOfBlogDelete.setStatus(0);
            catalogOfBlogRepository.save(catalogOfBlogDelete);
            return ResponseEntity.ok().body(Message.SUCCESS);

        }catch (Exception e){
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }

    @Override
    public List<CatalogOfBlog> findAll() {
        List<CatalogOfBlog>list = catalogOfBlogRepository.findAll();
        return list;
    }

    @Override
    public List<CatalogOfBlogReponse> getAllForClient() {
        List<CatalogOfBlogReponse>catalogOfBlogReponseList=catalogOfBlogRepository.findAll().stream().map(this::mapPoJoToResponse).collect(Collectors.toList());
        return catalogOfBlogReponseList;
    }

    @Override
    public CatalogOfBlog findById(Integer id) {
        CatalogOfBlog catalogOfBlog =catalogOfBlogRepository.findById(id).get();
        return catalogOfBlog;
    }

    @Override
    public Map<String, Object> findByName(String name, Pageable pageable) {
        Page<CatalogOfBlog> catalogOfBlogs= catalogOfBlogRepository.findByNameContaining(name,pageable);
        Map<String ,Object>result = Utility.returnResponse(catalogOfBlogs);
        return result;
    }

    @Override
    public CatalogOfBlog mapRequestToPoJo(CatalogOfBlogRequest catalogOfBlogRequest) {
        CatalogOfBlog catalogOfBlog=new CatalogOfBlog();
        catalogOfBlog.setName(catalogOfBlogRequest.getName());
        catalogOfBlog.setStatus(catalogOfBlogRequest.getStatus());
        return catalogOfBlog;
    }

    @Override
    public CatalogOfBlogReponse mapPoJoToResponse(CatalogOfBlog catalogOfBlog) {
        CatalogOfBlogReponse catalogOfBlogReponse=new CatalogOfBlogReponse();
        catalogOfBlogReponse.setId(catalogOfBlog.getId());
        catalogOfBlogReponse.setName(catalogOfBlog.getName());
        catalogOfBlogReponse.setStatus(catalogOfBlog.getStatus());
        return catalogOfBlogReponse;
    }
}
