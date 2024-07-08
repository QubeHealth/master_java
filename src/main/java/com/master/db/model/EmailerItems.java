package com.master.db.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailerItems {
    @JsonProperty("tpa_desk_id")
    private String tpaDeskId;

    @JsonProperty("claim_no")
    private String claimNo;

    @JsonProperty("policy_no")
    private String policyNo;

    @JsonProperty("initial_amt_req")
    private String initialAmtReq;

    @JsonProperty("initial_amt_approved")
    private String initialAmtApproved;

    @JsonProperty("final_adj_amt_req")
    private String finalAdjAmtReq;

    @JsonProperty("inal_adj_amt_approved")
    private String finalAdjAmtApproved;

    @JsonProperty("patient_name")
    private String patientName;

    @JsonProperty("metadata")
    private String metadata;

}