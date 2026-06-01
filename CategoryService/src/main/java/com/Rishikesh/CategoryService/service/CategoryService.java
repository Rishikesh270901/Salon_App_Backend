package com.Rishikesh.CategoryService.service;

import com.Rishikesh.CategoryService.modal.Category;
import com.Rishikesh.CategoryService.payload.SalonDTO;

import java.util.List;
import java.util.Set;

public interface CategoryService {

    Category createCategory(Category category, SalonDTO salonDTO);

    Set<Category>  getAllCategoriesBySalonId(Long id);

    Category getCategoryById(Long id);

    void deleteCategoryById(Long id, Long salonId) throws Exception;
}
