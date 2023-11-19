package com.nextu.mesdevis.service;

import com.nextu.mesdevis.dto.ProductDto;
import com.nextu.mesdevis.entity.Category;
import com.nextu.mesdevis.entity.Product;
import com.nextu.mesdevis.repository.CategoryRepository;
import com.nextu.mesdevis.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PriceService priceService;

    public List<ProductDto> getAllProductsFilter(String startStr, String endStr, String name, String code, String category) {
        List<Product> products = productRepository.findAll();
        List<Long> productIds = products.stream()
                .map(Product::getIdProduct)
                .filter(id -> filterByIdRange(id, startStr, endStr))
                .filter(id -> filterByName(products, id, name))
                .filter(id -> filterByCode(products, id, code))
                .filter(id -> filterByCategory(products, id, category))
                .collect(Collectors.toList());

        Map<Long, Float> productPricesMap = priceService.findProductsPrices(productIds);

        return products.stream()
                .filter(product -> productIds.contains(product.getIdProduct()))
                .map(product -> convertToDtoWithPrice(product, productPricesMap.get(product.getIdProduct())))
                .collect(Collectors.toList());
    }

    private boolean filterByIdRange(Long id, String startStr, String endStr) {
        if (startStr != null && !startStr.isEmpty()) {
            Long startId = Long.parseLong(startStr);
            if (id < startId) {
                return false;
            }
        }
        if (endStr != null && !endStr.isEmpty()) {
            Long endId = Long.parseLong(endStr);
            return id <= endId;
        }
        return true;
    }

    private boolean filterByName(List<Product> products, long id, String name) {
        if (name != null && !name.isEmpty()) {
            for (Product product : products) {
                if (product.getIdProduct() == id && product.getName().toLowerCase().contains(name.toLowerCase())) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    private boolean filterByCode(List<Product> products, long id, String code) {
        if (code != null && !code.isEmpty()) {
            for (Product product : products) {
                if (product.getIdProduct() == id && product.getProductCode().contains(code)) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    private boolean filterByCategory(List<Product> products, long id, String categoryId) {
        if (categoryId != null && !categoryId.isEmpty()) {
            long category = Long.parseLong(categoryId);
            for (Product product : products) {
                if (product.getIdProduct() == id && product.getCategory().getIdCategory() == category) {
                    return true;
                }
            }
            return false;
        }
        return true;
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

    private ProductDto convertToDtoWithPrice(Product product, Float price) {
        ProductDto productDto = new ProductDto(
                product.getIdProduct(),
                product.getName(),
                product.getProductCode(),
                product.getInventory(),
                product.getCategory().getIdCategory()
        );
        productDto.setPrice(price);
        return productDto;
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
