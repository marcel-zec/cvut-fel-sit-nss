package cz.cvut.fel.nss.parttimejobportal.dto;

import cz.cvut.fel.nss.parttimejobportal.model.Role;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

public class UserDto extends AbstractUserDto {

    @Size(max = 12, min = 9, message = "Phone number is in incorrect format.")
    @NotBlank(message = "Phone number cannot be blank")
    private String phone_number;

    @NotNull
    private int level = 0;

    private TravelJournalDto travel_journal;
    private List<TripReviewDto> tripReviews;
    private List<UserReviewDto> userReviewDtos;


    public UserDto() {
        super(Role.USER);
        userReviewDtos = new ArrayList<>();
        tripReviews = new ArrayList<>();
    }


    public UserDto(@NotNull(message = "Id cannot be blank") Long id, @Size(max = 30, min = 1, message = "First name is in incorrect format.") @NotNull(message = "First name cannot be blank") String firstName,
                   @NotNull(message = "Last name cannot be blank") String lastName, @Email(message = "Email should be valid") @NotNull(message = "Email cannot be blank") String email, AddressDto address,
                   @Size(max = 12, min = 9, message = "Phone number is in incorrect format.") @NotBlank(message = "Phone number cannot be blank") String phone_number, @NotNull int level,
                   TravelJournalDto travel_journal, List<TripReviewDto> tripReviews, List<UserReviewDto> userReviewDtos) {

        super(id, firstName, lastName, email, address, Role.USER);
        this.phone_number = phone_number;
        this.level = level;
        this.travel_journal = travel_journal;
        this.tripReviews = tripReviews;
        this.userReviewDtos = userReviewDtos;
    }


    public String getPhone_number() {

        return phone_number;
    }


    public void setPhone_number(String phone_number) {

        this.phone_number = phone_number;
    }


    public int getLevel() {

        return level;
    }


    public void setLevel(int level) {

        this.level = level;
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
