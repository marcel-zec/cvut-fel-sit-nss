package cz.cvut.fel.nss.parttimejobportal.dto;

import cz.cvut.fel.nss.parttimejobportal.model.UserReview;

import javax.validation.constraints.NotNull;

public class RequestWrapperEnrollment {

    @NotNull(message = "User review cannot be blank")
    private UserReview userReview;

    @NotNull(message = "Session id cannot be blank")
    private Long tripSessionId;

    @NotNull(message = "Enrollment cannot be blank")
    private EnrollmentDto enrollmentDto;


    public RequestWrapperEnrollment(@NotNull(message = "User review cannot be blank") UserReview userReview, @NotNull(message = "Session id cannot be blank") Long tripSessionId, @NotNull(message = "Enrollment cannot be blank") EnrollmentDto enrollmentDto) {

        this.userReview = userReview;
        this.tripSessionId = tripSessionId;
        this.enrollmentDto = enrollmentDto;
    }


    public RequestWrapperEnrollment() {
    }


    public UserReview getUserReview() {

        return userReview;
    }


    public void setUserReview(UserReview userReview) {

        this.userReview = userReview;
    }


    public Long getTripSessionId() {

        return tripSessionId;
    }


    public void setTripSessionId(Long tripSessionId) {

        this.tripSessionId = tripSessionId;
    }


    public EnrollmentDto getEnrollmentDto() {

        return enrollmentDto;
    }


    public void setEnrollmentDto(EnrollmentDto enrollmentDto) {

        this.enrollmentDto = enrollmentDto;
    }
}
