package com.nextu.mesdevis.controller;

import com.nextu.mesdevis.dto.ClientDto;
import com.nextu.mesdevis.service.ClientAuthenticationService;
import com.nextu.mesdevis.service.ClientService;
import com.nextu.mesdevis.service.MemberAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * Contrôleur gérant les opérations liées aux clients.
 */
@RestController
@RequestMapping("/api/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private MemberAuthenticationService memberAuthenticationService;

    @Autowired
    private ClientAuthenticationService clientAuthenticationService;

    /**
     * Récupère tous les clients.
     *
     * @return Liste des clients avec un code de statut approprié si l'utilisateur est authentifié en tant que MEMBRE ou ADMIN. Sinon 403 Forbidden.
     */
    @GetMapping
    public ResponseEntity<List<ClientDto>> getAllClients() {
        String roleMember = memberAuthenticationService.findMemberRole();
        if (Objects.equals(roleMember, "ADMIN") || Objects.equals(roleMember, "MEMBER")) {
            List<ClientDto> clients = clientService.getAllClients();
            return new ResponseEntity<>(clients, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    /**
     * Récupère un client par son identifiant.
     *
     * @param id Identifiant du client.
     * @return Le client avec un code de statut approprié si l'utilisateur est authentifié en tant que MEMBRE ou ADMIN ou comme étant le client avec l'id passer en paramètre. Sinon 403 Forbidden.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ClientDto> getClientById(@PathVariable Long id) {
        String roleMember = memberAuthenticationService.findMemberRole();
        long authenticatedClient = clientAuthenticationService.isClientExist();
        if (Objects.equals(roleMember, "ADMIN") || Objects.equals(roleMember, "MEMBER") || authenticatedClient == id) {
            ClientDto client = clientService.getClientById(id);
            return new ResponseEntity<>(client, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    /**
     * Crée un nouveau client.
     *
     * @param clientDto Les informations du client à créer.
     * @return Le client créé avec un code de statut approprié. (n'importe qui peut créer un compte client)
     */
    @PostMapping
    public ResponseEntity<ClientDto> createClient(@RequestBody ClientDto clientDto) {
        ClientDto createdClient = clientService.createClient(clientDto);
        return new ResponseEntity<>(createdClient, HttpStatus.CREATED);
    }

    /**
     * Met à jour un client existant.
     *
     * @param id       Identifiant du client à mettre à jour.
     * @param clientDto Les nouvelles informations du client.
     * @return Le client mis à jour avec un code de statut approprié si l'utilisateur est authentifié en tant que MEMBRE ou ADMIN ou comme étant le client avec l'id passer en paramètre. Sinon 403 Forbidden.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ClientDto> updateClient(@PathVariable Long id, @RequestBody ClientDto clientDto) {
        String roleMember = memberAuthenticationService.findMemberRole();
        long authenticatedClient = clientAuthenticationService.isClientExist();
        if (Objects.equals(roleMember, "ADMIN") || Objects.equals(roleMember, "MEMBER") || authenticatedClient == id) {
            ClientDto updatedClient = clientService.updateClient(id, clientDto);
            return new ResponseEntity<>(updatedClient, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    /**
     * Supprime un client existant.
     *
     * @param id Identifiant du client à supprimer.
     * @return Aucun contenu avec un code de statut approprié si l'utilisateur est authentifié en tant que MEMBRE ou ADMIN. Sinon 403 Forbidden.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        String roleMember = memberAuthenticationService.findMemberRole();
        if (Objects.equals(roleMember, "ADMIN") || Objects.equals(roleMember, "MEMBER")) {
            clientService.deleteClient(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
}
