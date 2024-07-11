package com.master.core.validations;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class MiscDataSchema {

    @JsonProperty("column_names")
    private String columnNames;

    private String key;
}
