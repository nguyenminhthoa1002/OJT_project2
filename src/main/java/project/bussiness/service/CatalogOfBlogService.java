package project.bussiness.service;

import project.model.dto.request.CatalogOfBlogRequest;
import project.model.dto.response.CatalogOfBlogReponse;
import project.model.entity.CatalogOfBlog;

public interface CatalogOfBlogService extends RootService<CatalogOfBlog,Integer, CatalogOfBlogRequest, CatalogOfBlogReponse> {
}
