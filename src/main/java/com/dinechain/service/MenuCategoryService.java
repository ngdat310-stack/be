package com.dinechain.service;

import com.dinechain.dto.request.CreateMenuCategoryRequest;
import com.dinechain.dto.request.UpdateMenuCategoryRequest;
import com.dinechain.dto.response.MenuCategoryResponse;

import java.util.List;

public interface MenuCategoryService {
    MenuCategoryResponse createCategory(CreateMenuCategoryRequest request);
    MenuCategoryResponse getCategory(Integer categoryId);
    MenuCategoryResponse updateCategory(Integer categoryId, UpdateMenuCategoryRequest request);
    void deleteCategory(Integer categoryId);
    List<MenuCategoryResponse> searchCategories(String name);
}
