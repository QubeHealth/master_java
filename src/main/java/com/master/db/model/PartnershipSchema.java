package com.master.db.model;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor

public class PartnershipSchema {
        @JsonProperty("hospital_name")
        private String hospitalName;
        @JsonProperty("city_name")
        private String cityName;
        @JsonProperty("bank_ifsc")
        private String bankIfsc;
        @JsonProperty("bank_account_number")
        private String bankAccountNumber;
        private String address;
        private String vpa;
        private String status;
        @JsonProperty("partner_category")
        private String partnerCategory;
        @JsonProperty("partner_sub_category")
        private String partnerSubCategory;
        private String pincode;
        private String state;
        @JsonProperty("hsp_id")
        private String id;
    }

