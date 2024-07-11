package com.master.db.model;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrefundedBankDetails {

        @JsonProperty("bank_account_number")
        private String bankAccountNumber;

        private String pincode;

        @JsonProperty("hsp_id")
        private String hspId;

        @JsonProperty("bank_name")
        private String bankName;

        @JsonProperty("bank_ifsc")
        private String bankIfsc;

        private String vpa;

        private String state;

        @JsonProperty("city_name")
        private String cityName;

        @JsonProperty("hospital_name")
        private String hospitalName;

        private String address;

    }

