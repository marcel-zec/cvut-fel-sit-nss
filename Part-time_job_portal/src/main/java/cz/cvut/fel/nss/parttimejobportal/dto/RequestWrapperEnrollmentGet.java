package cz.cvut.fel.nss.parttimejobportal.dto;

import javax.validation.constraints.NotNull;

public class RequestWrapperEnrollmentGet {

    @NotNull(message = "Enrollment cannot be blank")
    private EnrollmentDto enrollmentDto;

    @NotNull(message = "User cannot be blank")
    private UserDto owner;


    public RequestWrapperEnrollmentGet(@NotNull(message = "Enrollment cannot be blank") EnrollmentDto enrollmentDto,
                                       @NotNull(message = "User cannot be blank") UserDto userDto) {

        this.enrollmentDto = enrollmentDto;
        this.owner = userDto;
    }


    public RequestWrapperEnrollmentGet() {

    }


    public EnrollmentDto getEnrollmentDto() {

        return enrollmentDto;
    }


    public void setEnrollmentDto(EnrollmentDto enrollmentDto) {

        this.enrollmentDto = enrollmentDto;
    }


    public UserDto getOwner() {

        return owner;
    }


    public void setOwner(UserDto owner) {

        this.owner = owner;
    }
}
