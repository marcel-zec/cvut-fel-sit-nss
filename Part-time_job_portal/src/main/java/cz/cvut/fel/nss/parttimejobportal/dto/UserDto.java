package cz.cvut.fel.nss.parttimejobportal.dto;

import cz.cvut.fel.nss.parttimejobportal.model.Role;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

public class UserDto  {

    @NotNull(message = "Id cannot be blank")
    private Long id;

    @Size(max = 30, min = 1, message = "First name is in incorrect format.")
    @NotNull(message = "First name cannot be blank")
    private String firstName;

    @NotNull(message = "Last name cannot be blank")
    private String lastName;

    @Email(message = "Email should be valid")
    @NotNull(message = "Email cannot be blank")
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    private AddressDto address;
    private TravelJournalDto travel_journal;
    private List<TripReviewDto> tripReviews;
    private List<UserReviewDto> userReviewDtos;


    public UserDto() {
        this.tripReviews = new ArrayList<>();
    }


    public UserDto(@NotNull(message = "Id cannot be blank") Long id,
                   @Size(max = 30, min = 1, message = "First name is in incorrect format.") @NotNull(message = "First name cannot be blank") String firstName,
                   @NotNull(message = "Last name cannot be blank") String lastName,
                   @Email(message = "Email should be valid") @NotNull(message = "Email cannot be blank") String email,
                   AddressDto address, TravelJournalDto travel_journal, List<TripReviewDto> tripReviews, Role role,  List<UserReviewDto> userReviewDtos) {

        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
        this.travel_journal = travel_journal;
        this.tripReviews = tripReviews;
        this.role = role;
        this.userReviewDtos = userReviewDtos;
    }


    public Long getId() {

        return id;
    }


    public void setId(Long id) {

        this.id = id;
    }


    public String getFirstName() {

        return firstName;
    }


    public void setFirstName(String firstName) {

        this.firstName = firstName;
    }


    public String getLastName() {

        return lastName;
    }


    public void setLastName(String lastName) {

        this.lastName = lastName;
    }


    public String getEmail() {

        return email;
    }


    public void setEmail(String email) {

        this.email = email;
    }


    public Role getRole() {

        return role;
    }


    public void setRole(Role role) {

        this.role = role;
    }


    public AddressDto getAddress() {

        return address;
    }


    public void setAddress(AddressDto address) {

        this.address = address;
    }


    public TravelJournalDto getTravel_journal() {

        return travel_journal;
    }


    public void setTravel_journal(TravelJournalDto travel_journal) {

        this.travel_journal = travel_journal;
    }


    public List<TripReviewDto> getTripReviews() {

        return tripReviews;
    }


    public void setTripReviews(List<TripReviewDto> tripReviews) {

        this.tripReviews = tripReviews;
    }


    public List<UserReviewDto> getUserReviewDtos() {

        return userReviewDtos;
    }


    public void setUserReviewDtos(List<UserReviewDto> userReviewDtos) {

        this.userReviewDtos = userReviewDtos;
    }
}

