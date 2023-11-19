package com.nextu.mesdevis.dto;

import java.time.LocalDate;
import java.util.List;

/**
 * Data Transfer Object (DTO) représentant les informations d'un client de JeVendsTOUS.
 */
public class ClientDto {

    /**
     * Identifiant unique du client.
     */
    private Long idClient;

    /**
     * Prénom du client.
     */
    private String firstName;

    /**
     * Nom de famille du client.
     */
    private String lastName;

    /**
     * Adresse e-mail du client.
     */
    private String email;

    /**
     * Numéro de téléphone du client.
     */
    private String phone;

    /**
     * Adresse du client.
     */
    private String address;

    /**
     * Date d'inscription du client.
     */
    private LocalDate registrationDate;

    /**
     * Liste des identifiants des devis associés à ce client.
     */
    private List<Long> estimateIds;

    /**
     * Constructeur par défaut de la classe ClientDto.
     */
    public ClientDto() {
    }

    /**
     * Constructeur avec tous les champs de la classe ClientDto.
     *
     * @param idClient         L'identifiant unique du client.
     * @param firstName        Le prénom du client.
     * @param lastName         Le nom de famille du client.
     * @param email            L'adresse e-mail du client.
     * @param phone            Le numéro de téléphone du client.
     * @param address          L'adresse du client.
     * @param registrationDate La date d'inscription du client.
     * @param estimateIds      La liste des identifiants des devis associés au client.
     */
    public ClientDto(Long idClient, String firstName, String lastName, String email, String phone, String address, LocalDate registrationDate, List<Long> estimateIds) {
        this.idClient = idClient;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.registrationDate = registrationDate;
        this.estimateIds = estimateIds;
    }

    /**
     * Obtient l'identifiant unique du client.
     *
     * @return L'identifiant unique du client.
     */
    public Long getIdClient() {
        return idClient;
    }

    /**
     * Modifie l'identifiant unique du client.
     *
     * @param idClient Le nouvel identifiant unique du client.
     */
    public void setIdClient(Long idClient) {
        this.idClient = idClient;
    }

    /**
     * Obtient le prénom du client.
     *
     * @return Le prénom du client.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Modifie le prénom du client.
     *
     * @param firstName Le nouveau prénom du client.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Obtient le nom de famille du client.
     *
     * @return Le nom de famille du client.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Modifie le nom de famille du client.
     *
     * @param lastName Le nouveau nom de famille du client.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Obtient l'adresse e-mail du client.
     *
     * @return L'adresse e-mail du client.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Modifie l'adresse e-mail du client.
     *
     * @param email La nouvelle adresse e-mail du client.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Obtient le numéro de téléphone du client.
     *
     * @return Le numéro de téléphone du client.
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Modifie le numéro de téléphone du client.
     *
     * @param phone Le nouveau numéro de téléphone du client.
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Obtient l'adresse du client.
     *
     * @return L'adresse du client.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Modifie l'adresse du client.
     *
     * @param address La nouvelle adresse du client.
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Obtient la date d'inscription du client.
     *
     * @return La date d'inscription du client.
     */
    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    /**
     * Modifie la date d'inscription du client.
     *
     * @param registrationDate La nouvelle date d'inscription du client.
     */
    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    /**
     * Obtient la liste des identifiants des devis associés au client.
     *
     * @return La liste des identifiants des devis associés au client.
     */
    public List<Long> getEstimateIds() {
        return estimateIds;
    }

    /**
     * Modifie la liste des identifiants des devis associés au client.
     *
     * @param estimateIds La nouvelle liste des identifiants des devis associés au client.
     */
    public void setEstimateIds(List<Long> estimateIds) {
        this.estimateIds = estimateIds;
    }
}
