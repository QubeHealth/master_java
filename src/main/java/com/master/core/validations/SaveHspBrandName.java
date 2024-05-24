package com.master.core.validations;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaveHspBrandName {

    @JsonProperty("hsp_id")
    private Integer hspId;

    @NotEmpty(message = "transactionId cannot be empty")
    @Pattern(regexp = ".*\\D.*", message = "transactionId must be a String")
    @JsonProperty("transaction_id")
    private String transactionId;

    @Pattern(regexp = "^[^\\d]*$", message = "hspBrandName must be a String")
    @JsonProperty("hsp_brand_name")
    private String hspBrandName;
    
}