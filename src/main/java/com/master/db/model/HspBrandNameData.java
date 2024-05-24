package com.master.db.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HspBrandNameData {
    @JsonProperty("hsp_id")
    private Integer hspId;

    @JsonProperty("hsp_brand_name")
    private String hspBrandName;
}