package com.nextu.mesdevis.dto;

import java.util.List;

public class CategoryDto {

    private long idCategory;
    private String name;
    private long memberId;
    private List<Long> productIds;

    public CategoryDto() {
    }

    public CategoryDto(long idCategory, String name, long memberId, List<Long> productIds) {
        this.idCategory = idCategory;
        this.name = name;
        this.memberId = memberId;
        this.productIds = productIds;
    }

    public long getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(long idCategory) {
        this.idCategory = idCategory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getMemberId() {
        return memberId;
    }

    public void setMemberId(long memberId) {
        this.memberId = memberId;
    }

    public List<Long> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<Long> productIds) {
        this.productIds = productIds;
    }
}
