package com.nextu.mesdevis.dto;

public class DevelopedProductXEstimateDto {

    private long idProductXEstimate;
    private float price;
    private long inventory;
    private long estimateId;
    private long productId;
    private String productName;

    public DevelopedProductXEstimateDto() {
    }

    public DevelopedProductXEstimateDto(long idProductXEstimate, float price, long inventory, long estimateId, long productId, String productName) {
        this.idProductXEstimate = idProductXEstimate;
        this.price = price;
        this.inventory = inventory;
        this.estimateId = estimateId;
        this.productId = productId;
        this.productName = productName;
    }

    public long getIdProductXEstimate() {
        return idProductXEstimate;
    }

    public void setIdProductXEstimate(long idProductXEstimate) {
        this.idProductXEstimate = idProductXEstimate;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public long getInventory() {
        return inventory;
    }

    public void setInventory(long inventory) {
        this.inventory = inventory;
    }

    public long getEstimateId() {
        return estimateId;
    }

    public void setEstimateId(long estimateId) {
        this.estimateId = estimateId;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
