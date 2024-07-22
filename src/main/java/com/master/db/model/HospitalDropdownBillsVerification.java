package com.master.db.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;



   @Data
    public class HospitalDropdownBillsVerification {
        @JsonProperty("hospital_name")
        private String hospitalName;
    }

    
    
