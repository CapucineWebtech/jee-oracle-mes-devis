package com.nextu.mesdevis.dto;

public class ProductDto {

    private long idProduct;
    private String name;
    private String productCode;
    private long inventory;
    private long categoryId;
    private float price;

    public ProductDto() {
    }

    public ProductDto(long idProduct, String name, String productCode, long inventory, long categoryId) {
        this.idProduct = idProduct;
        this.name = name;
        this.productCode = productCode;
        this.inventory = inventory;
        this.categoryId = categoryId;
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

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
