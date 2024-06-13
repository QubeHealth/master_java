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

    @Getter
    @Setter
    public static class Documents {
        @NotNull(message = "hsp_id cannot be empty")
        @JsonProperty("hsp_id")
        private Long hspId;

        @JsonProperty("show_info")
        private boolean showInfo;
        
    }
}
