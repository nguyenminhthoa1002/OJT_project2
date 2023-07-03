package project.bussiness.serviceImpl;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import project.bussiness.service.CommentService;
import project.model.dto.request.CommentRequest;
import project.model.dto.response.CommentResponse;
import project.model.entity.CatalogOfBlog;
import project.model.entity.CommentBlog;
import project.model.entity.Users;
import project.model.shopMess.Message;
import project.model.utility.Utility;
import project.repository.BlogRepository;
import project.repository.CommentRepository;
import project.repository.UserRepository;
import project.security_jwt.CustomUserDetails;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class CommentBlogServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private BlogRepository blogRepository;
    @Autowired
    private UserRepository userRepository;


    @Override
    public Map<String, Object> getPagingAndSort(Pageable pageable) {
        Page<CommentBlog> commentBlogs = commentRepository.findAll(pageable);
        Map<String,Object>result= Utility.returnResponse(commentBlogs);
        return result;
    }
    @Override
    public CommentResponse saveOrUpdate(CommentRequest commentRequest) {
        CommentBlog commentBlog = mapRequestToPoJo(commentRequest);
        return mapPoJoToResponse(commentRepository.save(commentBlog));
    }
    @Override
    public CommentResponse update(Integer id, CommentRequest commentRequest) {
        CommentBlog commentBlog = mapRequestToPoJo(commentRequest);
        commentBlog.setId(id);
        CommentBlog commentBlogUpdate = commentRepository.save(commentBlog);
        CommentResponse commentResponse = mapPoJoToResponse(commentBlogUpdate);
        return commentResponse;
    }
    @Override
    public ResponseEntity<?> delete(Integer id) {
        try {
            CommentBlog commentBlogDelete = commentRepository.findById(id).get();
            commentBlogDelete.setStatus(0);
            commentRepository.save(commentBlogDelete);
            return ResponseEntity.ok().body(Message.SUCCESS);

        }catch (Exception e){
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }
    @Override
    public List<CommentBlog> findAll() {
        List<CommentBlog>list =commentRepository.findAll();
        return list;
    }
    @Override
    public List<CommentResponse> getAllForClient() {
        return null;
    }

    @Override
    public CommentBlog findById(Integer id) {
        CommentBlog commentBlog = commentRepository.findById(id).get();
        return commentBlog;
    }
    @Override
    public Map<String, Object> findByName(String name, Pageable pageable) {
        Page<CommentBlog> commentBlogs = commentRepository.findByNameContaining(name,pageable);
        Map<String,Object>result = Utility.returnResponse(commentBlogs);
        return result;
    }
    @Override
    public CommentBlog mapRequestToPoJo(CommentRequest commentRequest) {
        CustomUserDetails userIsLoggingIn = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users users = userRepository.findUsersByUserName(userIsLoggingIn.getUsername());
        CommentBlog commentBlog = new CommentBlog();
        commentBlog.setContent(commentRequest.getContent());
        LocalDateTime now = LocalDateTime.now();
        commentBlog.setCreateDate(now);
        commentBlog.setBlog(blogRepository.findById(commentRequest.getBlogId()).get());
        commentBlog.setUsers(users);
        commentBlog.setStatus(0);
        return commentBlog;
    }
    @Override
    public CommentResponse mapPoJoToResponse(CommentBlog commentBlog) {
        CommentResponse commentResponse = new CommentResponse();
        commentResponse.setId(commentBlog.getId());
        commentResponse.setName(commentBlog.getName());
        commentResponse.setStatus(commentBlog.getStatus());
        commentResponse.setContent(commentBlog.getContent());
        commentResponse.setCreateDate(commentBlog.getCreateDate());
        commentResponse.setUserId(commentBlog.getUsers().getUserId());
        return commentResponse;
    }
    @Override
    public CommentResponse finByIdResponse(int id) {
        return mapPoJoToResponse(findById(id));
    }

    @Override
    public Map<String, Object> searchByBlogId(Pageable pageable, int id) {
        Page<CommentResponse> commentResponses =commentRepository.searchAllByBlog_Id(pageable,id).map(this::mapPoJoToResponse);
       Map<String,Object>result =Utility.returnResponse(commentResponses);
        return result;
    }
}
