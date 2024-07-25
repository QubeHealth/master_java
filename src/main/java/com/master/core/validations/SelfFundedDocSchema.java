package com.master.core.validations;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SelfFundedDocSchema {
     @NotNull(message = "hsp_id cannot be empty")
        @JsonProperty("hsp_id")
        private Long hspId;

        @JsonProperty("show_info")
        private boolean showInfo;
}
