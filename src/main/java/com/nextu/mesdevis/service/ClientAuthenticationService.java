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

/**
 * Service responsable de l'appel api pour l'authentification du client.
 */
@Service
public class ClientAuthenticationService {
    private final Logger logger = LoggerFactory.getLogger(ClientService.class);

    @Value("${apiLoginClient}")
    private String apiUrl;

    /**
     * Vérifie l'existence d'un client en effectuant une requête POST vers l'API d'authentification.
     *
     * @return L'identifiant du client s'il existe, sinon -1.
     */
    public long isClientExist() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String jsonBody = "{\"clientId\":\"1\"}";

        HttpEntity<String> httpEntity = new HttpEntity<>(jsonBody, headers);
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, httpEntity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode;
        try {
            jsonNode = objectMapper.readTree(response.getBody());
            String clientId = jsonNode.path("json").path("clientId").asText();
            return Long.parseLong(clientId);
        } catch (Exception e) {
            logger.error("An error occurred while parsing JSON", e);
            return -1;
        }
    }
}
