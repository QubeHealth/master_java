package com.master.db.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HspMetadata {
    @JsonProperty("hsp_id")
    private String hspId;

    @JsonProperty("partner_category")
    private String partnerCategory;

    @JsonProperty("partner_sub_category")
    private String partnerSubCategory;

    @JsonProperty("status")
    private String status;

    @JsonProperty("search_response")
    private String searchResponse;

    private String keyword;
    
    @JsonProperty("is_active")
    private boolean isActive;

}