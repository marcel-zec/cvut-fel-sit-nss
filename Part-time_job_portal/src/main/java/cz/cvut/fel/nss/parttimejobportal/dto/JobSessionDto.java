package cz.cvut.fel.nss.parttimejobportal.dto;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class JobSessionDto {

    private Long id;

    private LocalDate from_date;

    private LocalDate to_date;

    private int capacity;

    private Long tripDtoId;


    public JobSessionDto(@NotNull(message = "Id cannot be blank") Long id, @FutureOrPresent LocalDate from_date, @FutureOrPresent LocalDate to_date, @Min(value = 1, message = "Min 1") @Max(value = 500, message = "Max 500") int capacity,
                          Long tripDtoId) {

        this.id = id;
        this.from_date = from_date;
        this.to_date = to_date;
        this.capacity = capacity;
        this.tripDtoId = tripDtoId;
    }


    public JobSessionDto() {

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


    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
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
