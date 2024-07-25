package com.master.db.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Miscellaneous {
    private int id;

    private String key;

    @JsonProperty("decimal_value1")
    private BigDecimal decimalValue1;

    @JsonProperty("decimal_value2")
    private BigDecimal decimalValue2;

    private String string1;
    private String string2;

    @JsonProperty("json_1")
    private String json1;

}
