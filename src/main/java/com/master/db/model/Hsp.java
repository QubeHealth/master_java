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
    private int hspId;

    @JsonProperty("hsp_name")
    private String hspName;

    private String vpa;

    private String level;

}
