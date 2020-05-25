package cz.cvut.fel.nss.parttimejobportal.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@MappedSuperclass
public abstract class Achievement extends AbstractEntity{

    @Basic(optional = false)
    @Column(nullable = false)
    private String name;

    @Basic(optional = false)
    @Column(nullable = false)
    private String description;

    @Basic(optional = false)
    @Column(nullable = false)
    private String icon;

    @JsonIgnore
    @ManyToMany
    private List<Offer> trips;

    public Achievement() {
    }

    public Achievement(String name, String description, String icon) {
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.trips = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<Offer> getTrips() {
        return trips;
    }

    public void setTrips(List<Offer> trips) {
        this.trips = trips;
    }

    public void addTrips(Offer trip) {
        this.trips.add(trip);
    }

    public void removeTrips(Offer trip) {
        this.trips.remove(trip);
    }
}
