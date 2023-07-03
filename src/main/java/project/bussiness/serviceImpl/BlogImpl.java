package project.bussiness.serviceImpl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import project.bussiness.service.BlogService;
import project.bussiness.service.CommentService;
import project.model.dto.request.BlogRequest;
import project.model.dto.response.BlogResponse;
import project.model.dto.response.CommentResponse;
import project.model.entity.Blog;
import project.model.entity.CommentBlog;
import project.model.entity.Tags;
import project.model.shopMess.Message;
import project.model.utility.Utility;
import project.repository.BlogRepository;
import project.repository.CatalogOfBlogRepository;
import project.repository.TagsRepository;
import project.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BlogImpl implements BlogService {
    private BlogRepository blogRepo;
    private UserRepository userRepo;
    private CatalogOfBlogRepository catalogOfBlogRepository;
    private TagsRepository tagsRepository;
    private CommentService commentService;

    @Override
    public Map<String, Object> getPagingAndSort(Pageable pageable) {
        Page<Blog> blogPage = blogRepo.findAll(pageable);
        Map<String, Object> result = Utility.returnResponse(blogPage);
        return result;
    }

    @Override
    public BlogResponse saveOrUpdate(BlogRequest blogRequest) {
        Blog blog = mapRequestToPoJo(blogRequest);
        Blog blog1 = blogRepo.save(blog);
        BlogResponse blogResponse = mapPoJoToResponse(blog1);

        return blogResponse;
    }

    @Override
    public BlogResponse update(Integer id, BlogRequest blogRequest) {
        Blog blog = mapRequestToPoJo(blogRequest);
        blog.setId(id);
        Blog blogUpdate = blogRepo.save(blog);
        BlogResponse blogResponse = mapPoJoToResponse(blogUpdate);
        return blogResponse;
    }

    @Override
    public ResponseEntity<?> delete(Integer id) {
        try {
            Blog blogDelete = blogRepo.findById(id).get();
            blogDelete.setStatus(0);
            blogRepo.save(blogDelete);
            return ResponseEntity.ok().body(Message.SUCCESS);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }

    }


    @Override
    public List<Blog> findAll() {
        List<Blog> blogList =blogRepo.findAll();

        return blogList;
    }

    @Override
    public List<BlogResponse> getAllForClient() {
//        List<BlogResponse> blogResponses= blogRepo.findAll().stream().map(this::mapPoJoToResponse).collect(Collectors.toList());
        List<Blog> listBlog = blogRepo.findAll();
        List<BlogResponse> blogResponseList = new ArrayList<>();

        for (Blog b : listBlog) {
            BlogResponse blogResponse = new BlogResponse();
            blogResponse.setBlogImg(b.getBlogImg());
            blogResponse.setName(b.getName());
            blogResponse.setContent(b.getContent());
            blogResponse.setStatus(b.getStatus());
            blogResponse.setCreatDate(b.getCreatDate());
            blogResponse.setUserName(b.getName());
            blogResponse.setId(b.getId());
            blogResponse.setCatalogBlogName(b.getCatalogOfBlog().getName());
            blogResponseList.add(blogResponse);
        }
        return blogResponseList;
//        return blogResponses;
    }

    @Override
    public Blog findById(Integer id) {
        return blogRepo.findById(id).get();
    }

    @Override
    public Map<String, Object> findByName(String name, Pageable pageable) {
        Page<BlogResponse> blogPage = blogRepo.findByNameContaining(name, pageable).map(this::mapPoJoToResponse);
        Map<String, Object> result = Utility.returnResponse(blogPage);

        return result;

    }

    @Override
    public Blog mapRequestToPoJo(BlogRequest rq) {
        Blog blog = new Blog();
        blog.setName(rq.getName());
        blog.setContent(rq.getContent());
        blog.setCreatDate(LocalDateTime.now());
        blog.setBlogImg(rq.getBlogImg());
        blog.setStatus(rq.getStatus());
//        CatalogOfBlog cat = catalogOfBlogRepository.findById(rq.getCatalogBlogId()).get();
        blog.setCatalogOfBlog(catalogOfBlogRepository.findById(rq.getCatalogBlogId()).get());
        blog.setUsers(userRepo.findById(rq.getUserId()).get());
        blog.setTagList(tagsRepository.findByIdIn(rq.getTagId()));
        if (blog.getUsers() == null) {
            return null;
        }
        return blog;
    }

    @Override
    public BlogResponse mapPoJoToResponse(Blog blog) {
        BlogResponse response = new BlogResponse();
        response.setId(blog.getId());
        response.setName(blog.getName());
        response.setCreatDate(blog.getCreatDate());
        response.setUserName(blog.getUsers().getUserName());
        response.setStatus(blog.getStatus());
        response.setContent(blog.getContent());
        response.setBlogImg(blog.getBlogImg());
        response.setCatalogBlogName(blog.getCatalogOfBlog().getName());
        List<CommentResponse> commentResponseList = new ArrayList<>();
        for (CommentBlog cm : blog.getCommentBlogList()) {
            commentResponseList.add(commentService.mapPoJoToResponse(cm));
        }
        response.setListCommentResponse(commentResponseList);
        response.setCountComment(commentResponseList.size());
        List<String> taglist = new ArrayList<>();
        for (Tags tag : blog.getTagList()) {
            taglist.add(tag.getName());
        }
        response.setTagName(taglist);
        return response;
    }

    @Override
    public List<BlogResponse> getTopNew() {
        List<BlogResponse> responses = blogRepo.findAll().stream().sorted(Comparator.comparing(Blog::getCreatDate)).map(this::mapPoJoToResponse).collect(Collectors.toList());
        List<BlogResponse> result = responses.stream().skip(Math.max(0, responses.size()) - 3).collect(Collectors.toList());
        return result;
    }

    @Override
    public BlogResponse getBlogResponseForClient(int blogId) {
        Blog blog = findById(blogId);
        return mapPoJoToResponse(blog);
    }
    @Override
    public List<BlogResponse> getRelatedBlog(int catId) {
        List<Blog> blogList = blogRepo.findByCatalogOfBlog_Id(catId);
        List<BlogResponse> responses = new ArrayList<>();
        if (blogList.size()!=0) {
            responses = blogList.stream()
                    .skip(blogList.size() - 3)
                    .limit(3)
                    .map(this::mapPoJoToResponse)
                    .collect(Collectors.toList());
        }
        return responses;
    }

    @Override
    public List<BlogResponse> searchByCatalogAndTag(List<Integer> listCatalogId, List<Integer> listTagId) {
        List<BlogResponse> blogList = new ArrayList<>();
        if (listTagId.size()==0) {
            for (int i = 0; i < listCatalogId.size(); i++) {
                List<Blog> lb = blogRepo.findByCatalogOfBlog_Id(listCatalogId.get(i));
                List<BlogResponse> lbr = lb.stream().map(this::mapPoJoToResponse).collect(Collectors.toList());
                blogList.addAll(lbr);
            }
        } else if (listCatalogId.size()==0) {
            List<Tags> listTag = tagsRepository.findByIdIn(listTagId);
            List<Blog> lb = blogRepo.findByTagListIn(listTag);
            List<BlogResponse> lbr = lb.stream().map(this::mapPoJoToResponse).collect(Collectors.toList());
            blogList.addAll(lbr);
        }
        else {
            List<Tags> listTag = tagsRepository.findByIdIn(listTagId);
            for (int i = 0; i < listCatalogId.size(); i++) {
                List<Blog> listBlogByTagId = blogRepo.findByCatalogOfBlog_IdAndTagListIn(listCatalogId.get(i),listTag);
                List<BlogResponse> blogResponseList = listBlogByTagId.stream().map(this::mapPoJoToResponse).collect(Collectors.toList());
                blogList.addAll(blogResponseList);
            }
        }

        return blogList;
    }


}
