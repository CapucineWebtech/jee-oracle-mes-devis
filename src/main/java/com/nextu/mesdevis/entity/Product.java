package com.nextu.mesdevis.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idProduct;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, unique = true)
    private String productCode;

    @Column(nullable = false)
    private long inventory;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idCategory", nullable = false)
    private Category category;

    @OneToMany(fetch = FetchType.EAGER, targetEntity = ProductXEstimate.class, mappedBy = "product")
    private List<ProductXEstimate> productsXEstimates;

    public Product() {
    }

    public Product(String name, String productCode, long inventory) {
        this.name = name;
        this.productCode = productCode;
        this.inventory = inventory;
    }

    public long getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(long idProduct) {
        this.idProduct = idProduct;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public long getInventory() {
        return inventory;
    }

    public void setInventory(long inventory) {
        this.inventory = inventory;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<ProductXEstimate> getProductsXEstimates() {
        return productsXEstimates;
    }

    public void setProductsXEstimates(List<ProductXEstimate> productsXEstimates) {
        this.productsXEstimates = productsXEstimates;
    }
}
