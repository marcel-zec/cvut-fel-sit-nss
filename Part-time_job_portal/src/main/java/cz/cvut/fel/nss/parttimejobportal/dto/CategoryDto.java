package cz.cvut.fel.nss.parttimejobportal.dto;

import javax.persistence.Basic;
import javax.validation.constraints.NotNull;
import java.util.List;

public class CategoryDto {

    @NotNull(message = "Id cannot be blank")
    private Long id;

    @Basic(optional = false)
    @NotNull(message = "Name of category cannot be blank")
    private String name;

    private List<OfferDto> trips;


    public CategoryDto(@NotNull(message = "Id cannot be blank") Long id,
                       @NotNull(message = "Name of category cannot be blank") String name, List<OfferDto> trips) {

        this.id = id;
        this.name = name;
        this.trips = trips;
    }


    public CategoryDto() {

    }


    public String getName() {

        return name;
    }


    public void setName(String name) {

        this.name = name;
    }


    public List<OfferDto> getTrips() {

        return trips;
    }


    public void setTrips(List<OfferDto> trips) {

        this.trips = trips;
    }


    public Long getId() {

        return id;
    }


    public void setId(Long id) {

        this.id = id;
    }
}
