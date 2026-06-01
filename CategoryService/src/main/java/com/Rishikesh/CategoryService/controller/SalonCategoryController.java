package com.Rishikesh.CategoryService.controller;


import com.Rishikesh.CategoryService.modal.Category;
import com.Rishikesh.CategoryService.payload.SalonDTO;
import com.Rishikesh.CategoryService.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/categories/salon-owner")
public class SalonCategoryController {

    private final CategoryService categoryService;

    public SalonCategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping()
    public ResponseEntity<Category> createCategory(@RequestBody Category category){
        SalonDTO salonDTO = new SalonDTO();
        salonDTO.setId(1L); // Replace with actual salon ID
        Category createCategory = categoryService.createCategory(category, salonDTO);
        return ResponseEntity.ok(createCategory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) throws Exception {
        SalonDTO salonDTO = new SalonDTO();
        salonDTO.setId(1L); // Replace with actual salon ID
        categoryService.deleteCategoryById(id, salonDTO.getId());
        return ResponseEntity.ok("Category Deleted Successfully.");
    }
}

