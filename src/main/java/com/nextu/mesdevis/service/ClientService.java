package com.nextu.mesdevis.service;

import com.nextu.mesdevis.dto.ClientDto;
import com.nextu.mesdevis.entity.Client;
import com.nextu.mesdevis.entity.Estimate;
import com.nextu.mesdevis.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service gérant les opérations liées aux clients.
 */
@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private EstimateRepository estimateRepository;

    /**
     * Récupère tous les clients.
     *
     * @return La liste des clients sous forme de DTO.
     */
    public List<ClientDto> getAllClients() {
        List<Client> clients = clientRepository.findAll();
        return clients.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    /**
     * Récupère un client par son identifiant.
     *
     * @param id L'identifiant du client.
     * @return Le client sous forme de DTO.
     * @throws RuntimeException Si le client n'est pas trouvé.
     */
    public ClientDto getClientById(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + id));
        return convertToDto(client);
    }

    /**
     * Crée un nouveau client.
     *
     * @param clientDto Les informations du client à créer. Voir la documentation de {@link ClientDto}.
     * @return Le client créé sous forme de DTO.
     */
    public ClientDto createClient(ClientDto clientDto) {
        Client client = convertToEntity(clientDto);
        client.setRegistrationDate(LocalDate.now());
        Client savedClient = clientRepository.save(client);
        return convertToDto(savedClient);
    }

    /**
     * Met à jour les informations d'un client existant.
     *
     * @param id        L'identifiant du client à mettre à jour.
     * @param clientDto Les nouvelles informations du client.
     * @return Le client mis à jour sous forme de DTO.
     * @throws RuntimeException Si le client n'est pas trouvé.
     */
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

    /**
     * Supprime un client par son identifiant.
     *
     * @param id L'identifiant du client à supprimer.
     * @throws RuntimeException Si le client n'est pas trouvé.
     */
    public void deleteClient(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + id));
        List<Estimate> estimates = client.getEstimates();
        for (Estimate estimate : estimates ){
            estimate.setClient(null);
            estimateRepository.save(estimate);
        }
        clientRepository.deleteById(id);
    }

    /**
     * Convertit un objet Client en un objet ClientDto.
     *
     * @param client L'objet Client à convertir.
     * @return Le Client converti en ClientDto.
     */
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

    /**
     * Convertit un objet ClientDto en un objet Client.
     *
     * @param clientDto L'objet ClientDto à convertir.
     * @return Le ClientDto converti en Client.
     */
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

