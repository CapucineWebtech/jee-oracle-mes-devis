package com.nextu.mesdevis.controller;

import com.nextu.mesdevis.dto.ProductDto;
import com.nextu.mesdevis.dto.ProductXEstimateDto;
import com.nextu.mesdevis.service.MemberAuthenticationService;
import com.nextu.mesdevis.service.ProductXEstimateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/productXEstimates")
public class ProductXEstimateController {

    @Autowired
    private ProductXEstimateService productXEstimateService;

    @Autowired
    private MemberAuthenticationService memberAuthenticationService;

    @GetMapping
    public ResponseEntity<List<ProductXEstimateDto>> getAllProductXEstimates() {
        List<ProductXEstimateDto> productXEstimates = productXEstimateService.getAllProductXEstimates();
        return new ResponseEntity<>(productXEstimates, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductXEstimateDto> getProductXEstimateById(@PathVariable Long id) {
        ProductXEstimateDto productXEstimate = productXEstimateService.getProductXEstimateById(id);
        return new ResponseEntity<>(productXEstimate, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ProductXEstimateDto> createProductXEstimate(@RequestBody ProductXEstimateDto productXEstimateDto) {
        ProductXEstimateDto createdProductXEstimate = productXEstimateService.createProductXEstimate(productXEstimateDto);
        return new ResponseEntity<>(createdProductXEstimate, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductXEstimateDto> updateProductXEstimate(@PathVariable Long id, @RequestBody ProductXEstimateDto productXEstimateDto) {
        String roleMember = memberAuthenticationService.findMemberRole();
        if (Objects.equals(roleMember, "ADMIN") || Objects.equals(roleMember, "MEMBER")) {
            ProductXEstimateDto updatedProductXEstimate = productXEstimateService.updateProductXEstimate(id, productXEstimateDto);
            return new ResponseEntity<>(updatedProductXEstimate, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductXEstimate(@PathVariable Long id) {
        String roleMember = memberAuthenticationService.findMemberRole();
        if (Objects.equals(roleMember, "ADMIN") || Objects.equals(roleMember, "MEMBER")) {
            productXEstimateService.deleteProductXEstimate(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
}
