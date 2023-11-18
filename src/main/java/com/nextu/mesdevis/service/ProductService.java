package com.nextu.mesdevis.service;

import com.nextu.mesdevis.dto.ProductDto;
import com.nextu.mesdevis.entity.Category;
import com.nextu.mesdevis.entity.Product;
import com.nextu.mesdevis.repository.CategoryRepository;
import com.nextu.mesdevis.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PriceService priceService;


    public List<ProductDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public List<ProductDto> getProductsWithPrice(long start, long end) {
        List<Product> products = productRepository.findAll(Sort.by(Sort.Direction.ASC, "idProduct"));

        if (start < 0 || end >= products.size() || start > end) {
            throw new IllegalArgumentException("Invalid indices");
        }

        List<Product> subList = products.subList((int) (start -1), (int) end);

        return subList.stream()
                .map(product -> {
                    ProductDto productDto = convertToDto(product);
                    float productPrice = priceService.findProductPrice(product.getIdProduct());
                    productDto.setPrice(productPrice);
                    return productDto;
                })
                .collect(Collectors.toList());
    }

    public List<ProductDto> getProductsByNameContainingWithPrice(String name) {
        List<Product> products = productRepository.findByNameContaining(name);
        return products.stream()
                .map(product -> {
                    ProductDto productDto = convertToDto(product);
                    float productPrice = priceService.findProductPrice(product.getIdProduct());
                    productDto.setPrice(productPrice);
                    return productDto;
                })
                .collect(Collectors.toList());
    }

    public List<ProductDto> getProductsByCodeContainingWithPrice(String productCode) {
        List<Product> products = productRepository.findByProductCodeContaining(productCode);
        return products.stream()
                .map(product -> {
                    ProductDto productDto = convertToDto(product);
                    float productPrice = priceService.findProductPrice(product.getIdProduct());
                    productDto.setPrice(productPrice);
                    return productDto;
                })
                .collect(Collectors.toList());
    }

    public List<ProductDto> getProductsByCategoryNameWithPrice(String categoryName) {
        Category category = categoryRepository.findByName(categoryName);
        if (category == null) {
            throw new RuntimeException("Category not found with name: " + categoryName);
        }
        List<Product> products = productRepository.findByCategory(category);
        return products.stream()
                .map(product -> {
                    ProductDto productDto = convertToDto(product);
                    float productPrice = priceService.findProductPrice(product.getIdProduct());
                    productDto.setPrice(productPrice);
                    return productDto;
                })
                .collect(Collectors.toList());
    }

    public ProductDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        return convertToDto(product);
    }

    public ProductDto createProduct(ProductDto productDto) {
        Product product = convertToEntity(productDto);
        Product savedProduct = productRepository.save(product);
        return convertToDto(savedProduct);
    }

    public ProductDto updateProduct(Long id, ProductDto productDto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        product.setName(productDto.getName());
        product.setProductCode(productDto.getProductCode());
        product.setInventory(productDto.getInventory());

        if (productDto.getCategoryId() != 0) {
            Category category = categoryRepository.findById(productDto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found with id: " + productDto.getCategoryId()));
            product.setCategory(category);
        }

        Product updatedProduct = productRepository.save(product);
        return convertToDto(updatedProduct);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    private ProductDto convertToDto(Product product) {
        return new ProductDto(
                product.getIdProduct(),
                product.getName(),
                product.getProductCode(),
                product.getInventory(),
                product.getCategory().getIdCategory()
        );
    }

    private Product convertToEntity(ProductDto productDto) {
        Product product = new Product(
                productDto.getName(),
                productDto.getProductCode(),
                productDto.getInventory()
        );

        if (productDto.getCategoryId() != 0) {
            Category category = categoryRepository.findById(productDto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found with id: " + productDto.getCategoryId()));
            product.setCategory(category);
        }

        return product;
    }

}
