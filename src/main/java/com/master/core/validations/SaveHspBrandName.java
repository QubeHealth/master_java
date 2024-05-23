package com.master.core.validations;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaveHspBrandName {

    @NotEmpty(message = "HspIds cannot be empty")
    @JsonProperty("hsp_id")
    private String hspId;

    @NotEmpty(message = "transactionId cannot be empty")
    @JsonProperty("transaction_id")
    private String transactionId;

    @NotEmpty(message = "hspBrandName cannot be empty")
    @JsonProperty("hsp_brand_name")
    private String hspBrandName;
    
}