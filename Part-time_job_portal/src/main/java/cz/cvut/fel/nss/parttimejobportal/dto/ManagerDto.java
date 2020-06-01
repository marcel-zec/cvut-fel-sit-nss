package cz.cvut.fel.nss.parttimejobportal.dto;

import cz.cvut.fel.nss.parttimejobportal.model.Role;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

public class ManagerDto extends AbstractUserDto{

    @Size(max = 12, min = 9, message = "Phone number is in incorrect format.")
    @NotNull(message = "Phone number cannot be blank")
    private String phone_number;

    @Size(max = 255, min = 1, message = "Company is in incorrect format.")
    @NotNull(message = "Company cannot be blank")
    private String company;

    private List<UserReviewDto> userReviewsAuthor;
    private List<OfferDto> offers;


    public ManagerDto() {
        super(Role.ADMIN);
        offers = new ArrayList<>();
        userReviewsAuthor = new ArrayList<>();
    }


    public ManagerDto(@NotNull(message = "Id cannot be blank") Long id, @Size(max = 30, min = 1, message = "First name is in incorrect format.") @NotNull(message = "First name cannot be blank") String firstName,
                      @NotNull(message = "Last name cannot be blank") String lastName, @Email(message = "Email should be valid") @NotNull(message = "Email cannot be blank") String email, AddressDto address, @Size(max = 12, min = 9, message = "Phone number is in incorrect format.") @NotNull(message = "Phone number cannot be blank") String phone_number,
                      @Size(max = 255, min = 1, message = "Company is in incorrect format.") @NotNull(message = "Company cannot be blank") String company, List<UserReviewDto> userReviewsAuthor, List<OfferDto> offers) {

        super(id, firstName, lastName, email, address, Role.ADMIN);
        this.phone_number = phone_number;
        this.company = company;
        this.userReviewsAuthor = userReviewsAuthor;
        this.offers = offers;
    }


    public String getPhone_number() {

        return phone_number;
    }


    public void setPhone_number(String phone_number) {

        this.phone_number = phone_number;
    }


    public String getCompany() {

        return company;
    }


    public void setCompany(String company) {

        this.company = company;
    }


    public List<UserReviewDto> getUserReviewsAuthor() {

        return userReviewsAuthor;
    }


    public void setUserReviewsAuthor(List<UserReviewDto> userReviewsAuthor) {

        this.userReviewsAuthor = userReviewsAuthor;
    }


    public List<OfferDto> getOffers() {

        return offers;
    }


    public void setOffers(List<OfferDto> offers) {

        this.offers = offers;
    }
}
