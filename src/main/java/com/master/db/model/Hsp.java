package com.master.db.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Hsp {

    @JsonProperty("hsp_id")
    private Integer hspId;

    @JsonProperty("hsp_name")
    private String hspName;

    private String vpa;

    private String level;

    @JsonProperty("hsp_official_name")
    private String hspOfficialName;

    @JsonProperty("bank_account_number")
    private String bankAccountNumber;

    @JsonProperty("bank_ifsc")
    private String bankIfsc;

    private String status;

    @JsonProperty("mcc_code")
    private Integer mccCode;

}
