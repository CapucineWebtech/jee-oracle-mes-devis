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

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private MemberAuthenticationService memberAuthenticationService;

    @Autowired
    private ClientAuthenticationService clientAuthenticationService;

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

    @PostMapping
    public ResponseEntity<ClientDto> createClient(@RequestBody ClientDto clientDto) {
        ClientDto createdClient = clientService.createClient(clientDto);
        return new ResponseEntity<>(createdClient, HttpStatus.CREATED);
    }

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
