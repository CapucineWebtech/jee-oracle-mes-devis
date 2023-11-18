package com.nextu.mesdevis.service;

import com.nextu.mesdevis.dto.ClientDto;
import com.nextu.mesdevis.entity.Client;
import com.nextu.mesdevis.entity.Estimate;
import com.nextu.mesdevis.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private EstimateRepository estimateRepository;

    public List<ClientDto> getAllClients() {
        List<Client> clients = clientRepository.findAll();
        return clients.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public ClientDto getClientById(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + id));
        return convertToDto(client);
    }

    public ClientDto createClient(ClientDto clientDto) {
        Client client = convertToEntity(clientDto);
        client.setRegistrationDate(LocalDate.now());
        Client savedClient = clientRepository.save(client);
        return convertToDto(savedClient);
    }

    public ClientDto updateClient(Long id, ClientDto clientDto) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + id));

        client.setFirstName(clientDto.getFirstName());
        client.setLastName(clientDto.getLastName());
        client.setEmail(clientDto.getEmail());
        client.setPhone(clientDto.getPhone());
        client.setAddress(clientDto.getAddress());
        client.setRegistrationDate(clientDto.getRegistrationDate());

        if (clientDto.getEstimateIds() != null && !clientDto.getEstimateIds().isEmpty()) {
            List<Estimate> newEstimates = estimateRepository.findAllById(clientDto.getEstimateIds());
            client.getEstimates().addAll(newEstimates);
        }

        Client updatedClient = clientRepository.save(client);
        return convertToDto(updatedClient);
    }

    public void deleteClient(Long id) {
        clientRepository.deleteById(id);
    }

    private ClientDto convertToDto(Client client) {
        List<Long> estimateIds = null;

        if (client.getEstimates() != null) {
            estimateIds = client.getEstimates()
                    .stream()
                    .map(Estimate::getIdEstimate)
                    .collect(Collectors.toList());
        }

        return new ClientDto(
                client.getIdClient(),
                client.getFirstName(),
                client.getLastName(),
                client.getEmail(),
                client.getPhone(),
                client.getAddress(),
                client.getRegistrationDate(),
                estimateIds
        );
    }

    private Client convertToEntity(ClientDto clientDto) {
        return new Client(
                clientDto.getFirstName(),
                clientDto.getLastName(),
                clientDto.getEmail(),
                clientDto.getPhone(),
                clientDto.getAddress(),
                clientDto.getRegistrationDate()
        );
    }
}

