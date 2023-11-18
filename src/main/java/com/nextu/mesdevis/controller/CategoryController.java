package com.nextu.mesdevis.controller;

import com.nextu.mesdevis.dto.CategoryDto;
import com.nextu.mesdevis.service.CategoryService;
import com.nextu.mesdevis.service.MemberAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private MemberAuthenticationService memberAuthenticationService;

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        List<CategoryDto> categories = categoryService.getAllCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Long id) {
        CategoryDto category = categoryService.getCategoryById(id);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto categoryDto) {
        String roleMember = memberAuthenticationService.findMemberRole();
        if (Objects.equals(roleMember, "ADMIN") || Objects.equals(roleMember, "MEMBER")) {
            CategoryDto createdCategory = categoryService.createCategory(categoryDto);
            return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable Long id, @RequestBody CategoryDto categoryDto) {
        String roleMember = memberAuthenticationService.findMemberRole();
        if (Objects.equals(roleMember, "ADMIN") || Objects.equals(roleMember, "MEMBER")) {
            CategoryDto updatedCategory = categoryService.updateCategory(id, categoryDto);
            return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        String roleMember = memberAuthenticationService.findMemberRole();
        if (Objects.equals(roleMember, "ADMIN") || Objects.equals(roleMember, "MEMBER")) {
            categoryService.deleteCategory(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
}
