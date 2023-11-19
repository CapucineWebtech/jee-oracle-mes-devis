package com.nextu.mesdevis.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Représente un client de JeVendsTOUS.
 */
@Entity
public class Client {

    /**
     * Identifiant unique du client.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idClient;

    /**
     * Prénom du client.
     */
    @Column(nullable = false)
    private String firstName;

    /**
     * Nom de famille du client.
     */
    @Column(nullable = false)
    private String lastName;

    /**
     * Adresse e-mail du client.
     */
    @Column(nullable = false)
    private String email;

    /**
     * Numéro de téléphone du client.
     */
    @Column
    private String phone;

    /**
     * Adresse du client.
     */
    @Column
    private String address;

    /**
     * Date d'inscription du client.
     */
    @Column(nullable = false)
    private LocalDate registrationDate;

    /**
     * Liste des devis associés à ce client.
     */
    @OneToMany(fetch = FetchType.EAGER, targetEntity = Estimate.class, mappedBy = "client")
    private List<Estimate> estimates;

    /**
     * Constructeur par défaut.
     */
    public Client() {
    }

    /**
     * Constructeur avec des paramètres.
     *
     * @param firstName       Le prénom du client.
     * @param lastName        Le nom du client.
     * @param email           L'adresse e-mail du client.
     * @param phone           Le numéro de téléphone du client.
     * @param address         L'adresse du client.
     * @param registrationDate La date d'inscription du client.
     */
    public Client(String firstName, String lastName, String email, String phone, String address, LocalDate registrationDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.registrationDate = registrationDate;
    }

    /**
     * Obtient l'identifiant du client.
     *
     * @return L'identifiant du client.
     */
    public Long getIdClient() {
        return idClient;
    }

    /**
     * Définit l'identifiant du client.
     *
     * @param idClient Le nouvel identifiant du client.
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
     * Définit le prénom du client.
     *
     * @param firstName Le nouveau prénom du client.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Obtient le nom du client.
     *
     * @return Le nom du client.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Définit le nom du client.
     *
     * @param lastName Le nouveau nom du client.
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
     * Définit l'adresse e-mail du client.
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
     * Définit le numéro de téléphone du client.
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
     * Définit l'adresse du client.
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
     * Définit la date d'inscription du client.
     *
     * @param registrationDate La nouvelle date d'inscription du client.
     */
    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    /**
     * Obtient la liste des devis associés à ce client.
     *
     * @return La liste des devis associés à ce client.
     */
    public List<Estimate> getEstimates() {
        return estimates;
    }

    /**
     * Définit la liste des devis associés à ce client.
     *
     * @param estimates La nouvelle liste des devis associés à ce client.
     */
    public void setEstimates(List<Estimate> estimates) {
        this.estimates = estimates;
    }
}
