package project.bussiness.service;

import org.springframework.data.domain.Pageable;
import project.model.dto.request.CommentRequest;
import project.model.dto.response.CatalogResponse;
import project.model.dto.response.CommentResponse;
import project.model.entity.CommentBlog;

import java.util.Map;

public interface CommentService extends RootService<CommentBlog,Integer, CommentRequest, CommentResponse>{
    CommentResponse finByIdResponse(int id);
    Map<String,Object> searchByBlogId(Pageable pageable,int id);
}
