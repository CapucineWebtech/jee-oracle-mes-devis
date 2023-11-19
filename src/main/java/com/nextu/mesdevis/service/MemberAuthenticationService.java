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
 * Service responsable de l'authentification des membres avec l'appel API.
 */
@Service
public class MemberAuthenticationService {
    private final Logger logger = LoggerFactory.getLogger(MemberService.class);

    @Value("${apiLoginMember}")
    private String apiUrl;

    /**
     * Recherche le rôle d'un membre.
     *
     * @return Le rôle du membre, ou une chaîne vide si une erreur se produit.
     */
    public String findMemberRole() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String jsonBody = "{\"roleMember\":\"ADMIN\"}";

        HttpEntity<String> httpEntity = new HttpEntity<>(jsonBody, headers);
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, httpEntity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode;
        try {
            jsonNode = objectMapper.readTree(response.getBody());
            return jsonNode.path("json").path("roleMember").asText();
        } catch (Exception e) {
            logger.error("An error occurred while parsing JSON", e);
        }
        return "";
    }

    /**
     * Recherche l'identifiant d'un membre.
     *
     * @return L'identifiant du membre, ou -1 si une erreur se produit.
     */
    public long findMemberId() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String jsonBody = "{\"IdMember\":\"1\"}";

        HttpEntity<String> httpEntity = new HttpEntity<>(jsonBody, headers);
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, httpEntity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode;
        try {
            jsonNode = objectMapper.readTree(response.getBody());
            String idMember = jsonNode.path("json").path("IdMember").asText();
            return Long.parseLong(idMember);
        } catch (Exception e) {
            logger.error("An error occurred while parsing JSON", e);
        }
        return -1;
    }
}
