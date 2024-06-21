package com.master.db.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HspNameData {
    @JsonProperty("hsp_official_name")
    private String hspOfficialName;

    @JsonProperty("hospital_name")
    private String hospitalName;
}