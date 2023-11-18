package com.nextu.mesdevis.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMember;

    @Column(nullable = false)
    private String firstname;
    @Column(nullable = false)
    private String lastname;
    @Column(nullable = false)
    private String role;
    @Column(nullable = false)
    private String email;

    @OneToMany(targetEntity = Estimate.class, mappedBy = "member")
    private List<Estimate> Estimates;

    @OneToMany(targetEntity = Category.class, mappedBy = "member")
    private List<Category> categories;

    public Member() {
    }

    public Member(String firstname, String lastname, String role, String email) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.role = role;
        this.email = email;
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

    public List<Estimate> getEstimates() {
        return Estimates;
    }

    public void setEstimates(List<Estimate> estimates) {
        Estimates = estimates;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
}