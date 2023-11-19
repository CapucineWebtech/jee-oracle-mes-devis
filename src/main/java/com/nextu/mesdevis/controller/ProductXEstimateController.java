package com.nextu.mesdevis.controller;

import com.nextu.mesdevis.dto.ProductXEstimateDto;
import com.nextu.mesdevis.service.ProductXEstimateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productXEstimates")
public class ProductXEstimateController {

    @Autowired
    private ProductXEstimateService productXEstimateService;
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
}
