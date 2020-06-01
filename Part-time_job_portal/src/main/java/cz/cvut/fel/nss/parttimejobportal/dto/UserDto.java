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

    private JobJournalDto travel_journal;
    private List<JobReviewDto> jobReviews;
    private List<UserReviewDto> userReviewDtos;


    public UserDto() {
        super(Role.USER);
        userReviewDtos = new ArrayList<>();
        jobReviews = new ArrayList<>();
    }


    public UserDto(@NotNull(message = "Id cannot be blank") Long id, @Size(max = 30, min = 1, message = "First name is in incorrect format.") @NotNull(message = "First name cannot be blank") String firstName,
                   @NotNull(message = "Last name cannot be blank") String lastName, @Email(message = "Email should be valid") @NotNull(message = "Email cannot be blank") String email, AddressDto address,
                   @Size(max = 12, min = 9, message = "Phone number is in incorrect format.") @NotBlank(message = "Phone number cannot be blank") String phone_number,
                   JobJournalDto travel_journal, List<JobReviewDto> tripReviews, List<UserReviewDto> userReviewDtos) {

        super(id, firstName, lastName, email, address, Role.USER);
        this.phone_number = phone_number;
        this.travel_journal = travel_journal;
        this.jobReviews = tripReviews;
        this.userReviewDtos = userReviewDtos;
    }


    public String getPhone_number() {

        return phone_number;
    }


    public void setPhone_number(String phone_number) {

        this.phone_number = phone_number;
    }


    public JobJournalDto getTravel_journal() {

        return travel_journal;
    }


    public void setTravel_journal(JobJournalDto travel_journal) {

        this.travel_journal = travel_journal;
    }


    public List<JobReviewDto> getJobReviews() {

        return jobReviews;
    }


    public void setJobReviews(List<JobReviewDto> jobReviews) {

        this.jobReviews = jobReviews;
    }


    public List<UserReviewDto> getUserReviewDtos() {

        return userReviewDtos;
    }


    public void setUserReviewDtos(List<UserReviewDto> userReviewDtos) {

        this.userReviewDtos = userReviewDtos;
    }
}
