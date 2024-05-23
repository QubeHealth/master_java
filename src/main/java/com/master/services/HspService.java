package com.master.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jdbi.v3.core.Jdbi;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.master.MasterConfiguration;
import com.master.api.IntsertHspBrandName;
import com.master.core.validations.SaveHspBrandName;
import com.master.db.model.Hsp;
import com.master.db.model.GetHspBrandName;
import com.master.db.repository.HspDao;

public class HspService extends BaseService {

    public HspService(MasterConfiguration configuration, Jdbi jdbi) {
        super(configuration, jdbi);
    }

    public List<Hsp> getHspDataListByIds(List<Integer> hspIds) {

        HspDao hspDao = jdbi.onDemand(HspDao.class);

        return hspDao.gethspDetailsByIds(hspIds);
    }

    public IntsertHspBrandName hspBrandName(SaveHspBrandName reqBody) {
        HspDao hspDao = jdbi.onDemand(HspDao.class);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            GetHspBrandName getHspBrandName = hspDao.getHspBrandName(reqBody.getHspId());
            if (getHspBrandName.getHspBrandName() == null) {
                // Create JSON-like String
                String jsonLikeData = String.format("[{\"transaction_id\":\"%s\", \"brand_name\":\"%s\"}]",
                        reqBody.getTransactionId(), reqBody.getHspBrandName());

                hspDao.insertHspBrandName(reqBody.getHspId(), jsonLikeData);
                return new IntsertHspBrandName(false, "Successfully added new Hsp Brand Name");
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
            String updatedJsonLikeData;
            updatedJsonLikeData = objectMapper.writeValueAsString(brandNameList);
            hspDao.insertHspBrandName(reqBody.getHspId(), updatedJsonLikeData);
            return new IntsertHspBrandName(false, "Successfully updated existing Hsp Brand Name List");

        } catch (Exception e) {
            return new IntsertHspBrandName(false, "Failed to added Hsp Brand Name");
        }
    }
    public Long insertHspByMobile(Map<String, Object> insertData) {

        HspDao hspDao = jdbi.onDemand(HspDao.class);
        return hspDao.insertHspByMobile(insertData);
    }

}
