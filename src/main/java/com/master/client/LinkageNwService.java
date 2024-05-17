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

        String url = configuration.getLinkageUrl() + "befisc/getVpaByMobile";
        Map<String, Long> requestBody = new HashMap<>();
        requestBody.put("mobile", mobile);
        return this.callThirdPartyApi(url, "post", requestBody, null);
    }

}
