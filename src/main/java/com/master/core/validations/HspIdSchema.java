package com.master.core.validations;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotEmpty;

public class HspIdSchema {

    @NotEmpty(message = "HspIds cannot be empty")
    @JsonProperty("hsp_ids")
    private List<Integer> hspIds;

    // Getter and setter methods
    public List<Integer> getHspIds() {
        return hspIds;
    }

    public void setHspIds(List<Integer> hspIds) {
        this.hspIds = hspIds;
    }
}
