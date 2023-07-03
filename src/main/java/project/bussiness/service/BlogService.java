package project.bussiness.service;

import project.model.dto.request.BlogRequest;
import project.model.dto.response.BlogResponse;
import project.model.entity.Blog;

import java.util.List;

public interface BlogService extends RootService<Blog,Integer, BlogRequest, BlogResponse>{
    List<BlogResponse> getTopNew();
    BlogResponse getBlogResponseForClient(int blogId);
    List<BlogResponse> searchByCatalogAndTag (List<Integer> listCatalogId,List<Integer> listTagId);
    List<BlogResponse> getRelatedBlog(int catId);

}
