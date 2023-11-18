package com.nextu.mesdevis.entity;

import jakarta.persistence.*;

@Entity
public class ProductXEstimate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idProductXEstimate;

    @Column(nullable = false)
    private float price;

    @Column(nullable = false)
    private long inventory;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idEstimate", nullable = false)
    private Estimate estimate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idProduct", nullable = false)
    private Product product;

    public ProductXEstimate() {
    }

    public ProductXEstimate(float price, long inventory) {
        this.price = price;
        this.inventory = inventory;
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

    public Estimate getEstimate() {
        return estimate;
    }

    public void setEstimate(Estimate estimate) {
        this.estimate = estimate;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
