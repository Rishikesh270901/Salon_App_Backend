package com.Rishikesh.CategoryService.service.Impl;

import com.Rishikesh.CategoryService.modal.Category;
import com.Rishikesh.CategoryService.payload.SalonDTO;
import com.Rishikesh.CategoryService.repository.CategoryRepository;
import com.Rishikesh.CategoryService.service.CategoryService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public Category createCategory(Category category, SalonDTO salonDTO) {
        Category newCategory = new Category();
        newCategory.setName(category.getName());
        newCategory.setImage(category.getImage());
        newCategory.setSalonId(salonDTO.getId());
        return categoryRepository.save(newCategory);
    }

    @Override
    public Set<Category> getAllCategoriesBySalonId(Long id) {
        return categoryRepository.findBySalonId(id);
    }

    @Override
    public Category getCategoryById(Long id) throws RuntimeException{

        return categoryRepository.findById(id).orElseThrow(()  -> new RuntimeException("Category not found with id: " + id));
    }

    @Override
    public void deleteCategoryById(Long id, Long salonId) throws Exception {
        Category category = getCategoryById(id);
        if(category.getSalonId().equals(salonId)){
            throw new Exception("You don't have permission to delete this category");
        }
        categoryRepository.deleteById(id);
    }
}
