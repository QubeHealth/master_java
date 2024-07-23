package com.master.core.validations;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

public class BillSchema {

    private BillSchema() {
    }

    @Data
    public static class ValidateBillSchema {
        @JsonProperty("search_input")
        private String searchInput;

    }

    @Data
    public static class ValidateInsertSchema {
        @JsonProperty("hospital_name")
        private String hospitalName;

        @JsonProperty("hospital_id")
        private String hospitalId;

    }

}
