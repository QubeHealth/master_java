package com.master.client;

import java.util.HashMap;
import java.util.Map;

import com.master.MasterConfiguration;
import com.master.api.ApiResponse;

public class LinkageNwService extends BaseServiceClient {

    public LinkageNwService(MasterConfiguration configuration) {
        super(configuration);
    }

    public ApiResponse<Object> getVpaByMobile(Long mobile) {

        String url = configuration.getLinkageJavaUrl() + "befisc/getVpaByMobile";
        Map<String, Long> requestBody = new HashMap<>();
        requestBody.put("mobile", mobile);
        return this.callThirdPartyApi(url, "post", requestBody, null);
    }

    public ApiResponse<Object> validateVpa(String vpa) {

        String url = configuration.getLinkageJavaUrl() + "befisc/getVpaDetails";
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("vpa", vpa);
        return this.callThirdPartyApi(url, "post", requestBody, null);
    }

    public ApiResponse<Object> validateBankDetails(String accountNumber, String ifsc) {

        String url = configuration.getLinkageJavaUrl() + "befisc/getBankDetails";
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("account_number", accountNumber);
        requestBody.put("ifsc_code", ifsc);
        return this.callThirdPartyApi(url, "post", requestBody, null);
    }

}
