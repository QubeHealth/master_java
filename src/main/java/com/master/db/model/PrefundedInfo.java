package com.master.db.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;



   @Data
    public class PrefundedInfo {
        @JsonProperty("data")
        private String data;
    }

    
    
