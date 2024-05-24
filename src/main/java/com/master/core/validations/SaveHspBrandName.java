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

    @NotEmpty(message = "transaction_id cannot be empty")
    @Pattern(regexp = ".*\\D.*", message = "transaction_id must be a String")
    @JsonProperty("transaction_id")
    private String transactionId;

    @NotEmpty(message = "hsp_brand_name cannot be empty")
    @Pattern(regexp = ".*\\D.*", message = "hsp_brand_name must be a String")
    @JsonProperty("hsp_brand_name")
    private String hspBrandName;
    
}