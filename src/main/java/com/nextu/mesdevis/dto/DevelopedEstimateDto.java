package com.nextu.mesdevis.dto;

import java.time.LocalDate;
import java.util.List;

public class DevelopedEstimateDto {

    private Long idEstimate;
    private LocalDate creationDate;
    private LocalDate validationDate;
    private LocalDate paymentDate;
    private LocalDate cancellationDate;
    private Long clientId;
    private List<DevelopedProductXEstimateDto> productsXEstimates;
    private Long memberId;

    public DevelopedEstimateDto() {
    }

    public DevelopedEstimateDto(Long idEstimate, LocalDate creationDate, LocalDate validationDate, LocalDate paymentDate, LocalDate cancellationDate, Long clientId, List<DevelopedProductXEstimateDto> productsXEstimates, Long memberId) {
        this.idEstimate = idEstimate;
        this.creationDate = creationDate;
        this.validationDate = validationDate;
        this.paymentDate = paymentDate;
        this.cancellationDate = cancellationDate;
        this.clientId = clientId;
        this.productsXEstimates = productsXEstimates;
        this.memberId = memberId;
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

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public List<DevelopedProductXEstimateDto> getProductsXEstimates() {
        return productsXEstimates;
    }

    public void setProductsXEstimates(List<DevelopedProductXEstimateDto> productsXEstimates) {
        this.productsXEstimates = productsXEstimates;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }
}
