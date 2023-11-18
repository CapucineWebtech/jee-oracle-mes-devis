package com.nextu.mesdevis.service;

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

import java.text.DecimalFormat;
import java.util.Random;

@Service
public class PriceService {
    @Value("${apiProductPrice}")
    private String apiUrl;

    private final Logger logger = LoggerFactory.getLogger(MemberService.class);

    public float findProductPrice() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Random random = new Random();
        float randomPrice = 200 + random.nextFloat() * (1000 - 200);
        DecimalFormat df = new DecimalFormat("#.##");
        String formattedPrice = df.format(randomPrice);

        String jsonBody = "{\"productPrice\":" + formattedPrice + "}";

        HttpEntity<String> httpEntity = new HttpEntity<>(jsonBody, headers);
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, httpEntity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode;
        try {
            jsonNode = objectMapper.readTree(response.getBody());
            String productPrice = jsonNode.path("json").path("productPrice").asText();
            return Float.parseFloat(productPrice);
        } catch (Exception e) {
            logger.error("An error occurred while parsing JSON", e);
        }
        return -1;
    }
}
