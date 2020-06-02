package cz.cvut.fel.nss.parttimejobportal.dto;


import javax.persistence.Basic;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

public class AchievementCertificateDto extends AchievementDto {
    @NotNull(message = "Id cannot be blank")
    private Long id;

    @Basic(optional = false)
    private String name;

    @Basic(optional = false)
    private String description;

    @Basic(optional = false)
    private String icon;

    private List<Long> trips;
    private List<Long> owned_travel_journals;


    public AchievementCertificateDto(@NotNull(message = "Id cannot be blank") Long id, String name, String description, String icon,
                                     List<Long> trips, List<Long> owned_travel_journals) {

        this.id = id;
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.trips = trips;
        this.owned_travel_journals = owned_travel_journals;
    }


    public AchievementCertificateDto() {

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


    public List<Long> getTrips() {

        return trips;
    }


    public void setTrips(List<Long> trips) {

        this.trips = trips;
    }


    public List<Long> getOwned_travel_journals() {

        return owned_travel_journals;
    }


    public void setOwned_travel_journals(List<Long> owned_travel_journals) {

        this.owned_travel_journals = owned_travel_journals;
    }


    public Long getId() {

        return id;
    }


    public void setId(Long id) {

        this.id = id;
    }
}
