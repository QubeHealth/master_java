package com.master.core.validations;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaveHspBrandName {

    @NotNull(message = "hsp_id cannot be empty")
    @JsonProperty("hsp_id")
    private Integer hspId;

    @NotNull(message = "transaction_id cannot be empty")
    @Pattern(regexp = ".*\\D.*", message = "transaction_id must be a String and not empty")
    @JsonProperty("transaction_id")
    private String transactionId;

    @NotNull(message = "hsp_brand_name cannot be empty")
    @Pattern(regexp = ".*\\D.*", message = "hsp_brand_name must be a String and not empty")
    @JsonProperty("hsp_brand_name")
    private String hspBrandName;

}