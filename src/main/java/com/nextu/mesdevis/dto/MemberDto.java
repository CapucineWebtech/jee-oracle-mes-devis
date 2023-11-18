package com.nextu.mesdevis.dto;

import java.util.List;

public class MemberDto {

    private Long idMember;
    private String firstname;
    private String lastname;
    private String role;
    private String email;
    private List<Long> estimateIds;
    private List<Long> categoryIds;

    public MemberDto() {
    }

    public MemberDto(Long idMember, String firstname, String lastname, String role, String email, List<Long> estimateIds, List<Long> categoryIds) {
        this.idMember = idMember;
        this.firstname = firstname;
        this.lastname = lastname;
        this.role = role;
        this.email = email;
        this.estimateIds = estimateIds;
        this.categoryIds = categoryIds;
    }

    public Long getIdMember() {
        return idMember;
    }

    public void setIdMember(Long idMember) {
        this.idMember = idMember;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Long> getEstimateIds() {
        return estimateIds;
    }

    public void setEstimateIds(List<Long> estimateIds) {
        this.estimateIds = estimateIds;
    }

    public List<Long> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<Long> categoryIds) {
        this.categoryIds = categoryIds;
    }
}
