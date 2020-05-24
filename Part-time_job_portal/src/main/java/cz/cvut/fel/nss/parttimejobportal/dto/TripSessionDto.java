package cz.cvut.fel.nss.parttimejobportal.dto;

import javax.persistence.Basic;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class TripSessionDto {

    @NotNull(message = "Id cannot be blank")
    private Long id;

    @Basic(optional = false)
    @FutureOrPresent
    private LocalDate from_date;

    @Basic(optional = false)
    @FutureOrPresent
    private LocalDate to_date;

    @Basic(optional = false)
    @Min(value = 0, message = "Min 0")
    @Max(value = 99999, message = "Max 99999")
    private double price;

    @Basic(optional = false)
    private Long tripDtoId;


    public TripSessionDto(@NotNull(message = "Id cannot be blank") Long id, @FutureOrPresent LocalDate from_date, @FutureOrPresent LocalDate to_date, @Min(value = 0, message = "Min 0") @Max(value = 99999, message = "Max 99999") double price,
                          Long tripDtoId) {

        this.id = id;
        this.from_date = from_date;
        this.to_date = to_date;
        this.price = price;
        this.tripDtoId = tripDtoId;
    }


    public TripSessionDto() {

    }


    public LocalDate getFrom_date() {
        return from_date;
    }


    public void setFrom_date(LocalDate from_date) {

        this.from_date = from_date;
    }


    public LocalDate getTo_date() {

        return to_date;
    }


    public void setTo_date(LocalDate to_date) {

        this.to_date = to_date;
    }


    public double getPrice() {

        return price;
    }


    public void setPrice(double price) {

        this.price = price;
    }


    public Long getTripDto() {

        return tripDtoId;
    }


    public void setTripDto(Long tripDto) {

        this.tripDtoId = tripDto;
    }


    public Long getId() {

        return id;
    }


    public void setId(Long id) {

        this.id = id;
    }
}
