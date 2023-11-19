package com.nextu.mesdevis.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.core.type.TypeReference;

import java.text.DecimalFormat;
import java.util.*;

@Service
public class PriceService {
    @Value("${apiProductPrice}")
    private String apiUrl;

    private final Logger logger = LoggerFactory.getLogger(ProductService.class);

    public float findProductPrice(long productId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Random random = new Random();
        float randomPrice = 200 + random.nextFloat() * (1000 - 200);
        DecimalFormat df = new DecimalFormat("#.##");
        String formattedPrice = df.format(randomPrice);

        String jsonBody = "{\"productPrice\":\"" + formattedPrice + "\"}";

        HttpEntity<String> httpEntity = new HttpEntity<>(jsonBody, headers);
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, httpEntity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode;
        try {
            jsonNode = objectMapper.readTree(response.getBody());
            String productPrice = jsonNode.path("json").path("productPrice").asText();
            String productPriceFormatted = productPrice.replace(",", ".");
            return Float.parseFloat(productPriceFormatted);
        } catch (Exception e) {
            logger.error("An error occurred while parsing JSON", e);
        }
        return -1;
    }

    public Map<Long, Float> findProductsPrices(List<Long> productIds) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        List<ProductPrice> productPrices = new ArrayList<>();
        Random random = new Random();
        DecimalFormat df = new DecimalFormat("#.##");

        for (Long productId : productIds) {
            float randomPrice = 200 + random.nextFloat() * (1000 - 200);
            String formattedPrice = df.format(randomPrice);
            formattedPrice = formattedPrice.replace(",", ".");
            ProductPrice productPrice = new ProductPrice(productId, Float.parseFloat(formattedPrice));
            productPrices.add(productPrice);
        }

        ProductsPrices productPriceRequest = new ProductsPrices(productPrices);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonBody;
        try {
            jsonBody = objectMapper.writeValueAsString(productPriceRequest);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("An error occurred while parsing JSON : " + e);
        }
        HttpEntity<String> httpEntity = new HttpEntity<>(jsonBody, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, httpEntity, String.class);
        JsonNode jsonNode;
        try {
            jsonNode = objectMapper.readTree(response.getBody());
            JsonNode productPricesNode = jsonNode.path("json").path("productPrices");
            List<ProductPrice> responseProductPrices = objectMapper.readValue(productPricesNode.toString(),
                    new TypeReference<List<ProductPrice>>() {});
            Map<Long, Float> pricesMap = new HashMap<>();
            for (ProductPrice responseProductPrice : responseProductPrices) {
                pricesMap.put(responseProductPrice.getId(), responseProductPrice.getPrice());
            }
            return pricesMap;
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while parsing JSON : " + e);
        }
    }


    public static class ProductsPrices {
        private List<ProductPrice> productPrices;

        public ProductsPrices(List<ProductPrice> productPrices) {
            this.productPrices = productPrices;
        }

        public List<ProductPrice> getProductPrices() {
            return productPrices;
        }

        public void setProductPrices(List<ProductPrice> productPrices) {
            this.productPrices = productPrices;
        }
    }

    public static class ProductPrice {
        private Long id;
        private Float price;

        public ProductPrice() {
        }

        public ProductPrice(Long id, Float price) {
            this.id = id;
            this.price = price;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Float getPrice() {
            return price;
        }

        public void setPrice(Float price) {
            this.price = price;
        }
    }
}
