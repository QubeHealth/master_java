package com.master.db.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrefundedEmailers {
    private String type;

    private String subject;

    @JsonProperty("is_active")
    private boolean isActive;

    @JsonProperty("claim_no")
    private String claimNo;

}