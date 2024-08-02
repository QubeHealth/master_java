package com.master.db.model;

import java.util.List;

import lombok.Data;

public class FraudModel {
    
    @Data
    public static class Rule {
        private String name;
        private List<Condition> conditions;
    
        // Getters and Setters
    }
    
    @Data
    public static class Condition {
        private List<String> transaction_type;
        private Integer minimum_amount;
        private Integer divisible_by;
        private Integer min_count;
        private Integer time_hours;
        private Integer time_days;
        private Integer min_no_of_cashback;
    
        // Getters and Setters
    }
    
}
