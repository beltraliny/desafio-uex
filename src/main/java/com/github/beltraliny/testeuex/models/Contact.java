package com.github.beltraliny.testeuex.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.github.beltraliny.testeuex.models.dtos.ContactDTO;
import jakarta.persistence.*;

@Entity
@Table(name = "contacts")
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;
    private String cpf;
    private String phoneNumber;
    private String street;
    private String number;
    private String neighborhood;
    private String city;
    private String state;
    private String country;
    private String complement;
    private String postalCode;
    private String latitude;
    private String longitude;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    public Contact() { }

    public Contact(ContactDTO contactDTO) {
        this.name = contactDTO.name();
        this.cpf = contactDTO.cpf();
        this.phoneNumber = contactDTO.phoneNumber();
        this.street = contactDTO.street();
        this.number = contactDTO.number();
        this.neighborhood = contactDTO.neighborhood();
        this.state = contactDTO.state();
        this.city = contactDTO.city();
        this.country = contactDTO.country();
        this.complement = contactDTO.complement();
        this.postalCode = contactDTO.postalCode();
    }

    public String getFullAddress() {
        String fullAddress = this.getNumber() + " " + this.getStreet();
        fullAddress += (this.getNeighborhood() != null) ? ", " + this.getNeighborhood() : "";
        fullAddress += (this.getCity() != null) ? " " +  this.getCity() : "";
        fullAddress += (this.getState() != null) ? " " +  this.getState() : "";

        return fullAddress;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getComplement() {
        return complement;
    }

    public void setComplement(String complement) {
        this.complement = complement;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
