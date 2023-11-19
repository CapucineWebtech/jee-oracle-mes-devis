package com.nextu.mesdevis.service;

import com.nextu.mesdevis.dto.CategoryDto;
import com.nextu.mesdevis.entity.Category;
import com.nextu.mesdevis.entity.Member;
import com.nextu.mesdevis.entity.Product;
import com.nextu.mesdevis.repository.CategoryRepository;
import com.nextu.mesdevis.repository.MemberRepository;
import com.nextu.mesdevis.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MemberAuthenticationService memberAuthenticationService;

    public List<CategoryDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public CategoryDto getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        return convertToDto(category);
    }

    public CategoryDto createCategory(CategoryDto categoryDto) {
        Category category = convertToEntity(categoryDto);

        if (categoryDto.getMemberId() != 0) {
            Member member = memberRepository.findById(categoryDto.getMemberId())
                    .orElseThrow(() -> new RuntimeException("Member not found with id: " + categoryDto.getMemberId()));
            category.setMember(member);
        }

        Category savedCategory = categoryRepository.save(category);
        return convertToDto(savedCategory);
    }

    public CategoryDto updateCategory(Long id, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

        category.setName(categoryDto.getName());

        if (categoryDto.getMemberId() != 0) {
            Member member = memberRepository.findById(categoryDto.getMemberId())
                    .orElseThrow(() -> new RuntimeException("Member not found with id: " + categoryDto.getMemberId()));
            category.setMember(member);
        }
        if (categoryDto.getProductIds() != null && !categoryDto.getProductIds().isEmpty()) {
            List<Product> products = productRepository.findAllById(categoryDto.getProductIds());
            category.setProducts(products);
        }

        Category updatedCategory = categoryRepository.save(category);
        return convertToDto(updatedCategory);
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    private CategoryDto convertToDto(Category category) {
        String roleMember = memberAuthenticationService.findMemberRole();
        boolean isAdminOrMember = Objects.equals(roleMember, "ADMIN") || Objects.equals(roleMember, "MEMBER");

        List<Long> productIds = category.getProducts() != null
                ? category.getProducts().stream().map(Product::getIdProduct).toList()
                : List.of();

        if (isAdminOrMember) {
            return new CategoryDto(
                    category.getIdCategory(),
                    category.getName(),
                    category.getMember().getIdMember(),
                    productIds
            );
        } else {
            return new CategoryDto(
                    category.getIdCategory(),
                    category.getName(),
                    0,
                    productIds
            );
        }
    }


    private Category convertToEntity(CategoryDto categoryDto) {
        Category category = new Category();
        category.setName(categoryDto.getName());

        Member member = memberRepository.findById(categoryDto.getMemberId())
                .orElseThrow(() -> new RuntimeException("Member not found with id: " + categoryDto.getMemberId()));

        if (member != null) {
            category.setMember(member);
        } else {
            throw new IllegalArgumentException("Member not found with id: " + categoryDto.getMemberId());
        }
        return category;
    }
}
