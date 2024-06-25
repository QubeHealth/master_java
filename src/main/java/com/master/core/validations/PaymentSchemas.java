package com.master.core.validations;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

public class PaymentSchemas {

    private PaymentSchemas() {

    }

    @Data
    public static class MobileSchema {
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

    @Data
    public static class VpaSchemas {
        @NotBlank(message = "Please enter a valid vpa")
        @Pattern(regexp = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9._-]+$", message = "Enter a valid vpa")
        @JsonProperty("vpa")
        private String vpa;
    }

    @Data
    public static class BankSchema {

        @NotBlank(message = "Please enter a valid Bank Account Number")
        @Pattern(regexp = "^\\d{6,}$", message = "Invalid Account Number")
        @JsonProperty("account_number")
        private String accountNumber;

        @NotBlank(message = "Please enter a valid IFSC Code")
        @Pattern(regexp = "^[A-Z]{4}0[A-Z0-9]{6}$", message = "Invalid IFSC Code")
        @JsonProperty("ifsc_code")
        private String ifscCode;

    }

    @Data
    public static class QrSchema {
        @NotBlank(message = "QR cannot be empty")
        @JsonProperty("upi_qr_url")
        private String upiQrUrl;
    }

}
