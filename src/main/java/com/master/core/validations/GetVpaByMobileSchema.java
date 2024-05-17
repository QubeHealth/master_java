package com.master.core.validations;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class GetVpaByMobileSchema {

    @NotBlank(message = "Please enter a valid mobile number")
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Mobile number must be a valid 10-digit number and should not contain any alphabets.")
    private String mobile;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
