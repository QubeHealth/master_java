package com.master.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QrData {

    private String mccCode;
    private String merchantCity;
    private String vpa;
    private String merchantPincode;
    private String amount;
    private String transactionId;
    private String merchantName;

}
