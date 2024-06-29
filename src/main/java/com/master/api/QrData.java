package com.master.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

public class QrData {
    private QrData() {

    }

    @Data
    public static final class QrInfo {

        private String mccCode;
        private String merchantCity;
        private String vpa;
        private String merchantPincode;
        private String amount;
        private String transactionId;
        private String merchantName;

    }

    @Data
    public static final class QrResponse {

        @JsonProperty("bank_account_name")
        private String bankAccountName;

        @JsonProperty("hsp_id")
        private Integer hspId;

        private String vpa;

        @JsonProperty("merchant_name")
        private String merchantName;

        private String status;

    }

}
