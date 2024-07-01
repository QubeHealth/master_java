package com.master.db.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailerItems {
    @JsonProperty("KH_id")
    private String khId;

    @JsonProperty("claim_no")
    private String claimNo;

    private String policy;
    
    @JsonProperty("initial_amt_req")
    private String initialAmtReq;

    @JsonProperty("initial_amt_approved")
    private String initialAmtApproved;

    @JsonProperty("final_adj_amt_req")
    private String finalAdjAmtReq;

    @JsonProperty("inal_adj_amt_approved")
    private String finalAdjAmtApproved;


}