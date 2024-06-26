package com.master.db.model;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SavePartnershipSchema {

    @JsonProperty("sub_category")
    private String subCategory;
    private String category;
    private String status;

    @NotEmpty(message = "HSP IDs should not be empty ")
    @JsonProperty("hsp_ids")
    private List<Integer> hspIds;

    // Custom validation method to ensure at least one of category, subCategory, or
    // status is not null
    @AssertTrue(message = "Category/Sub-Category/Status is mandatory")
    public boolean isAtLeastOneFieldChanged() {
        return (category != null && category != "") || (subCategory != null && subCategory != "")
                || (status != null && status != "");
    }
}
