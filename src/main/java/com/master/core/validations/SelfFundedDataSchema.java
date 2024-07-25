package com.master.core.validations;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SelfFundedDataSchema {

    @Getter
    @Setter
    public static class Constants {
        @NotNull(message = "type cannot be empty")
        @JsonProperty("type")
        private String type;

    }

  
}
