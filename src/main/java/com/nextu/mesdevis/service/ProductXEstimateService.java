package com.nextu.mesdevis.service;

import com.nextu.mesdevis.dto.ProductXEstimateDto;
import com.nextu.mesdevis.entity.Estimate;
import com.nextu.mesdevis.entity.Product;
import com.nextu.mesdevis.entity.ProductXEstimate;
import com.nextu.mesdevis.repository.EstimateRepository;
import com.nextu.mesdevis.repository.ProductRepository;
import com.nextu.mesdevis.repository.ProductXEstimateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductXEstimateService {

    @Autowired
    private ProductXEstimateRepository productXEstimateRepository;

    @Autowired
    private EstimateRepository estimateRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PriceService priceService;


    public List<ProductXEstimateDto> getAllProductXEstimates() {
        List<ProductXEstimate> productXEstimates = productXEstimateRepository.findAll();
        return productXEstimates.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public ProductXEstimateDto getProductXEstimateById(Long id) {
        ProductXEstimate productXEstimate = productXEstimateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ProductXEstimate not found with id: " + id));
        return convertToDto(productXEstimate);
    }

    public ProductXEstimateDto createProductXEstimate(ProductXEstimateDto productXEstimateDto) {
        ProductXEstimate productXEstimate = convertToEntity(productXEstimateDto);
        float price = priceService.findProductPrice(productXEstimateDto.getProductId());
        if(price > -1){
            productXEstimate.setPrice(price);
        }else {
            throw new RuntimeException("Unable to recover the price");
        }
        ProductXEstimate savedProductXEstimate = productXEstimateRepository.save(productXEstimate);
        return convertToDto(savedProductXEstimate);
    }

    public ProductXEstimateDto updateProductXEstimate(Long id, ProductXEstimateDto productXEstimateDto) {
        ProductXEstimate productXEstimate = productXEstimateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ProductXEstimate not found with id: " + id));

        productXEstimate.setPrice(productXEstimateDto.getPrice());
        productXEstimate.setInventory(productXEstimateDto.getInventory());

        if (productXEstimateDto.getEstimateId() != 0) {
            Estimate estimate = estimateRepository.findById(productXEstimateDto.getEstimateId())
                    .orElseThrow(() -> new RuntimeException("Estimate not found with id: " + productXEstimateDto.getEstimateId()));
            productXEstimate.setEstimate(estimate);
        }

        if (productXEstimateDto.getProductId() != 0) {
            Product product = productRepository.findById(productXEstimateDto.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found with id: " + productXEstimateDto.getEstimateId()));
            productXEstimate.setProduct(product);
        }

        ProductXEstimate updatedProductXEstimate = productXEstimateRepository.save(productXEstimate);
        return convertToDto(updatedProductXEstimate);
    }

    public void deleteProductXEstimate(Long id) {
        productXEstimateRepository.deleteById(id);
    }

    private ProductXEstimateDto convertToDto(ProductXEstimate productXEstimate) {
        return new ProductXEstimateDto(
                productXEstimate.getIdProductXEstimate(),
                productXEstimate.getPrice(),
                productXEstimate.getInventory(),
                productXEstimate.getEstimate().getIdEstimate(),
                productXEstimate.getProduct().getIdProduct()
        );
    }

    private ProductXEstimate convertToEntity(ProductXEstimateDto productXEstimateDto) {
        ProductXEstimate productXEstimate = new ProductXEstimate(
                productXEstimateDto.getPrice(),
                productXEstimateDto.getInventory()
        );

        if (productXEstimateDto.getEstimateId() != 0) {
            Estimate estimate = estimateRepository.findById(productXEstimateDto.getEstimateId())
                    .orElseThrow(() -> new RuntimeException("Estimate not found with id: " + productXEstimateDto.getEstimateId()));
            productXEstimate.setEstimate(estimate);
        }

        if (productXEstimateDto.getProductId() != 0) {
            Product product = productRepository.findById(productXEstimateDto.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found with id: " + productXEstimateDto.getEstimateId()));
            productXEstimate.setProduct(product);
        }

        return productXEstimate;
    }
}
