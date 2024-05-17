package com.master.client;

import java.util.HashMap;
import java.util.Map;

import com.master.MasterConfiguration;
import com.master.api.ApiResponse;

public class LinkageNwService extends BaseServiceClient {

    public LinkageNwService(MasterConfiguration configuration) {
        super(configuration);
    }

    public ApiResponse<Object> generateOtp(String mobile) {

        String url = configuration.getLinkageUrl() + "sms/generateOtp";
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("mobile", mobile);
        return this.callThirdPartyApi(url, "post", requestBody, null);
    }

}
