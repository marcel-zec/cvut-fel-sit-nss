package cz.cvut.fel.nss.parttimejobportal.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "ADDRESS")
public class Address extends AbstractEntity {

    @Basic(optional = false)
    @Column(nullable = false)
    @Size(max = 100, min = 1)
    @NotBlank(message = "City cannot be blank")
    private String city;

    @Basic(optional = false)
    @Column(nullable = false)
    @Size(max = 100, min = 1)
    @NotBlank(message = "Street cannot be blank")
    private String street;

    @Basic(optional = false)
    @Column(nullable = false)
    private int houseNumber;

    @Basic(optional = false)
    @Column(nullable = false)
    @NotBlank(message = "ZIP code cannot be blank")
    private String zipCode;

    @Basic(optional = false)
    @Column(nullable = false)
    @NotBlank(message = "Country cannot be blank")
    private String country;

    @JsonIgnore
    @OneToOne(mappedBy = "address")
    private User user;

    public Address(@NotBlank(message = "City cannot be blank") String city,
                   @NotBlank(message = "Street cannot be blank") String street,
                   int houseNumber,
                   @NotBlank(message = "ZIP code cannot be blank") String zipCode,
                   @NotBlank(message = "Country cannot be blank") String country,
                   User user) {
        this.city = city;
        this.street = street;
        this.houseNumber = houseNumber;
        this.zipCode = zipCode;
        this.country = country;
        this.user = user;
    }


    public Address() {

    }
    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public int getHouseNumber() {
        return houseNumber;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getCountry() {
        return country;
    }

    public User getUser() {
        return user;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setHouseNumber(int houseNumber) {
        this.houseNumber = houseNumber;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Address{" +
                "city='" + city + '\'' +
                ", street='" + street + '\'' +
                ", houseNumber=" + houseNumber +
                ", zipCode='" + zipCode + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}
