package com.master.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.glassfish.jersey.message.internal.Qualified;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.master.MasterConfiguration;
import com.master.api.ApiRequest;
import com.master.api.ApiResponse;
import com.master.api.InsertHspBrandName;
import com.master.client.LinkageNwService;
import com.master.core.constants.Queries;
import com.master.core.validations.SaveHspBrandName;
import com.master.core.validations.PaymentSchemas.BankSchema;
import com.master.db.model.Hsp;
import com.master.db.model.HspMetadata;
import com.master.db.model.PartnerCategory;
import com.master.db.model.GetHspBrandName;
import com.master.db.repository.HspDao;
import com.master.db.repository.MiscDao;

public class HspService extends BaseService {

    private LinkageNwService linkageNwService; 

    public HspService(MasterConfiguration configuration, Jdbi jdbi) {
        super(configuration, jdbi);
        linkageNwService = new LinkageNwService(configuration);
    }

    public List<Hsp> getHspDataListByIds(List<Integer> hspIds) {

        HspDao hspDao = jdbi.onDemand(HspDao.class);

        return hspDao.gethspDetailsByIds(hspIds);
    }

    public InsertHspBrandName hspBrandName(SaveHspBrandName reqBody) {
        HspDao hspDao = jdbi.onDemand(HspDao.class);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            GetHspBrandName getHspBrandName = hspDao.getHspBrandName(reqBody.getHspId());
            if (getHspBrandName.getHspBrandName() == null) {
                // Create JSON-like String
                String jsonLikeData = String.format("[{\"transaction_id\":\"%s\", \"brand_name\":\"%s\"}]",
                        reqBody.getTransactionId(), reqBody.getHspBrandName());

                hspDao.insertHspBrandName(reqBody.getHspId(), jsonLikeData);
                return new InsertHspBrandName(true, "Successfully added new Hsp Brand Name");
            }

            List<Map<String, String>> brandNameList;

            // Check if existing JSON data is a list or a single object
            String existingJson = getHspBrandName.getHspBrandName();
            if (existingJson.trim().startsWith("[")) {
                // It's a list
                brandNameList = objectMapper.readValue(existingJson,
                        new TypeReference<List<Map<String, String>>>() {
                        });
            } else {
                // It's a single object, convert it to a list
                Map<String, String> singleObject = objectMapper.readValue(existingJson,
                        new TypeReference<Map<String, String>>() {
                        });
                brandNameList = new ArrayList<>();
                brandNameList.add(singleObject);
            }

            // Create a new object to add to the list
            Map<String, String> newBrandName = Map.of(
                    "transaction_id", reqBody.getTransactionId(),
                    "brand_name", reqBody.getHspBrandName());

            // Add the new object to the list
            brandNameList.add(newBrandName);

            // Convert the updated list back to JSON
            String updatedJsonData;
            updatedJsonData = objectMapper.writeValueAsString(brandNameList);
            hspDao.insertHspBrandName(reqBody.getHspId(), updatedJsonData);
            return new InsertHspBrandName(true, "Successfully updated existing Hsp Brand Name List");

        } catch (Exception e) {
            return new InsertHspBrandName(false, "Failed to add Hsp Brand Name");
        }
    }

    public Long insertHspByMobile(Map<String, Object> insertData) {

        Map<String, Object> data = new HashMap<>(insertData);
        String uuid = UUID.randomUUID().toString();
        data.put("uuid", uuid);
        data.put("status", data.get("status").equals("VALID_HSP") ? "VERIFIED" : "PENDING");

        HspDao hspDao = jdbi.onDemand(HspDao.class);
        return hspDao.insertHspByMobile(data);
    }

    public Long insertHspBank(Map<String, Object> insertData) {

        Map<String, Object> data = new HashMap<>(insertData);
        String uuid = UUID.randomUUID().toString();
        data.put("uuid", uuid);
        data.put("status", data.get("status").equals("VALID_HSP") ? "VERIFIED" : "PENDING");

        HspDao hspDao = jdbi.onDemand(HspDao.class);
        return hspDao.insertHspBankData(data);
    }

    public Map<String, Object> getHspByMobile(String mobile) {

        HspDao hspDao = jdbi.onDemand(HspDao.class);

        Hsp hsp = hspDao.getHspbyMobile(mobile);
        if (hsp == null || hsp.getStatus() == null || hsp.getVpa() == null) {
            return null;
        }

        Map<String, Object> data = new HashMap<>();
        data.put("bank_account_name", hsp.getHspOfficialName());
        data.put("vpa", hsp.getVpa());
        data.put("merchant_name", hsp.getHspName());
        data.put("status", hsp.getStatus().equals("VERIFIED") ? "VALID_HSP" : "INVALID_HSP");
        data.put("hsp_id", hsp.getHspId());

        return data;
    }

    public Map<String, Object> getHspByVpa(String vpa) {

        HspDao hspDao = jdbi.onDemand(HspDao.class);

        Hsp hsp = hspDao.getHspbyVpa(vpa);
        if (hsp == null || hsp.getStatus() == null) {
            return null;
        }

        Map<String, Object> data = new HashMap<>();
        data.put("bank_account_name", hsp.getHspOfficialName());
        data.put("vpa", hsp.getVpa());
        data.put("merchant_name", hsp.getHspName());
        data.put("status", hsp.getStatus().equals("VERIFIED") ? "VALID_HSP" : "INVALID_HSP");
        data.put("hsp_id", hsp.getHspId());

        return data;
    }

    public Map<String, Object> getHspByBankDetails(BankSchema body) {

        HspDao hspDao = jdbi.onDemand(HspDao.class);

        Hsp hsp = hspDao.getHspbyBankDetails(body);
        if (hsp == null || hsp.getStatus() == null) {
            return null;
        }

        Map<String, Object> data = new HashMap<>();
        data.put("bank_account_name", hsp.getHspOfficialName());
        data.put("account_number", hsp.getBankAccountNumber());
        data.put("ifsc_code", hsp.getBankIfsc());
        data.put("merchant_name", hsp.getHspName());
        data.put("status", hsp.getStatus().equals("VERIFIED") ? "VALID_HSP" : "INVALID_HSP");
        data.put("hsp_id", hsp.getHspId());

        return data;
    }

    public Long insertHspQrData(Map<String, Object> insertData) {

        HspDao hspDao = jdbi.onDemand(HspDao.class);
        return hspDao.insertHspQrData(insertData);
    }

    public String validateOnBankAccountName(String vpa,String hspId, String name){

        HspDao hspDao = jdbi.onDemand(HspDao.class);

        hspDao.updateHospitalOfficialName(hspId,name);
       
        ApiResponse<Object> x = linkageNwService.validateVpa(vpa);
        Map<String, Object> storer = new HashMap();
        storer = (Map<String,Object>)x.getData();
        String receivedName = storer.get("bank_account_name").toString();

        String givenName = hspDao.getHspBankName(hspId);
        if(givenName.toLowerCase().contains("merchant")){
            hspDao.updateHospitalName(hspId, receivedName);
        }
        return receivedName; 
    }

    public HspMetadata getHspMetadata(String hspId){
        HspDao hspDao = jdbi.onDemand(HspDao.class);
        return hspDao.getHspMetaData(hspId);
    }

    public PartnerCategory getPartnerCategory(String key){
        MiscDao miscDao = jdbi.onDemand(MiscDao.class);
        return miscDao.getCategoryMisc(key);
    }
}