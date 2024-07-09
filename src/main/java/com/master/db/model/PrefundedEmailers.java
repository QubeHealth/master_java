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

    @JsonProperty("partnered_claim_no")
    private String partneredClaimNo;

    @JsonProperty("pf_request_id")
    private String pfRequestId;

    @JsonProperty("policy_no")
    private String policyNo;

    @JsonProperty("claim_no")
    private String claimNo;
}