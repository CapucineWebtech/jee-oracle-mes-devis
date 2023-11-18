package com.nextu.mesdevis.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
public class Estimate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEstimate;

    @Column(nullable = false)
    private LocalDate creationDate;

    @Column
    private LocalDate validationDate;

    @Column
    private LocalDate paymentDate;

    @Column
    private LocalDate cancellationDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idClient")
    private Client client;

    @OneToMany(targetEntity = ProductXEstimate.class, mappedBy = "estimate")
    private List<ProductXEstimate> productsXEstimates;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idMember")
    private Member member;

    public Estimate() {
    }

    public Estimate(LocalDate creationDate, LocalDate validationDate, LocalDate paymentDate, LocalDate cancellationDate) {
        this.creationDate = creationDate;
        this.validationDate = validationDate;
        this.paymentDate = paymentDate;
        this.cancellationDate = cancellationDate;
    }

    public Long getIdEstimate() {
        return idEstimate;
    }
    public void setIdEstimate(Long idEstimate) {
        this.idEstimate = idEstimate;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }
    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDate getValidationDate() {
        return validationDate;
    }
    public void setValidationDate(LocalDate validationDate) {
        this.validationDate = validationDate;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }
    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public LocalDate getCancellationDate() {
        return cancellationDate;
    }
    public void setCancellationDate(LocalDate cancellationDate) {
        this.cancellationDate = cancellationDate;
    }

    public Client getClient() {
        return client;
    }
    public void setClient(Client client) {
        this.client = client;
    }

    public List<ProductXEstimate> getProductsXEstimates() {
        return productsXEstimates;
    }
    public void setProductsXEstimates(List<ProductXEstimate> productsXEstimates) {
        this.productsXEstimates = productsXEstimates;
    }

    public Member getMember() {
        return member;
    }
    public void setMember(Member member) {
        this.member = member;
    }

}
