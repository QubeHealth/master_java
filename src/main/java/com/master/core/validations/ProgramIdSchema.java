package com.master.core.validations;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class ProgramIdSchema {

    @NotNull(message = "Please enter a valid program id")
    @Positive(message = "Program id must be greater than zero")
    @JsonProperty("program_id")
    private int programId;

    public int getProgramId() {
        return programId;
    }

    public void setProgramId(int programId) {
        this.programId = programId;
    }
}
