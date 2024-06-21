package com.master.core.validations;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class NearbySearchSchema {
    
    @NotBlank(message = "Latitude is required")
    private String latitude;

    @NotBlank(message = "Longitude code is required")
    private String longitude;

    @NotNull(message = "Hsp Id is required")
    @JsonProperty("hsp_id")
    private Integer hspId; 

    private String keyword;

}
