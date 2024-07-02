package com.master.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdbi.v3.core.Jdbi;
import org.json.JSONArray;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.master.MasterConfiguration;
import com.master.core.validations.PaymentSchemas.QrDataSchema;
import com.master.db.model.Hsp;
import com.master.db.model.HspMetadata;
import com.master.db.model.Miscellaneous;
import com.master.db.model.PartnershipSchema;
import com.master.db.model.SavePartnershipSchema;
import com.master.db.repository.MiscDao;
import com.master.db.repository.PartnershipDao;

public class PartnershipService extends BaseService {

    private HspService hspService;

    public PartnershipService(MasterConfiguration configuration, Jdbi jdbi) {
        super(configuration, jdbi);
        this.hspService = new HspService(configuration, jdbi);
    }

    public Map<String, List<String>> getCategoryDetails() {

        MiscDao miscDao = jdbi.onDemand(MiscDao.class);

        Miscellaneous a = miscDao.getSelfundedDetails("partnership_category");
        String catandsubcat = a.getJson1();
        Map<String, List<String>> categoriesMap = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // Read JSON string and get the root node
            JsonNode rootNode = objectMapper.readTree(catandsubcat);
            // Iterate over the primary categories
            rootNode.fieldNames().forEachRemaining(primaryCategory -> {
                // Add primary category to map
                categoriesMap.put(primaryCategory, null);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return categoriesMap;
    }

    public Map<String, List<String>> getSubCategoryDetails() {
        MiscDao miscDao = jdbi.onDemand(MiscDao.class);

        Miscellaneous a = miscDao.getSelfundedDetails("partnership_category");
        String catandsubcat = a.getJson1();
        Map<String, List<String>> subCategoriesMap = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // Read JSON string and get the root node
            JsonNode rootNode = objectMapper.readTree(catandsubcat);

            // Iterate over each primary category
            rootNode.fieldNames().forEachRemaining(primaryCategory -> {
                // Get the node containing secondary categories
                JsonNode secondaryCategoriesNode = rootNode.get(primaryCategory);

                // Iterate over secondary category names
                secondaryCategoriesNode.fieldNames().forEachRemaining(secondaryCategoryName -> {
                    // Add secondary category name to map
                    subCategoriesMap.put(secondaryCategoryName, null);
                });
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        return subCategoriesMap;

    }

    public List<PartnershipSchema> getHspDetails() {
        PartnershipDao partnershipDao = jdbi.onDemand(PartnershipDao.class);
        List<PartnershipSchema> partnershipSchema = partnershipDao.getHospitalDetails();
        if (partnershipSchema != null) {
            return partnershipSchema;
        } else {
            return null;
        }

    }

    public Integer saveHospitalDetails(SavePartnershipSchema savePartnershipSchema) {
        PartnershipDao partnershipDao = jdbi.onDemand(PartnershipDao.class);
        return partnershipDao.savePartnershipDetails(savePartnershipSchema.getHspIds(),
                savePartnershipSchema.getCategory(), savePartnershipSchema.getSubCategory(),
                savePartnershipSchema.getStatus());

    }

    public List<Hsp> getHspListForPartnership() {
        PartnershipDao partnershipDao = jdbi.onDemand(PartnershipDao.class);
        return partnershipDao.getHspListForPartnership();
    }

    public Boolean addPartnershipDetails(String hspId, String hspName, Map<String, Object> categoryMap) {
        HspMetadata inTable = this.hspService.getHspMetadata(hspId);
        if (inTable != null) {
            return true;
        }

        Map<String, Object> insertData = extractHspCategory(categoryMap, hspName);

        Integer checkForDataInsertedIntblHsp = this.hspService.insertDataInHspMetadata(hspId,
                insertData.get("primary_key").toString(), insertData.get("secondary_key").toString(),
                insertData.get("keyword").toString());

        return checkForDataInsertedIntblHsp != null;
    }

    private Map<String, Object> extractHspCategory(Map<String, Object> categoryMap, String bankAccountName) {
        Map<String, Object> insertData = new HashMap<>();
        insertData.put("primary_key", "Others");
        insertData.put("secondary_key", "Others");
        insertData.put("keyword", "");

        boolean found = false;

        for (Map.Entry<String, Object> primaryEntry : categoryMap.entrySet()) {
            String primaryKey = primaryEntry.getKey();

            Map<String, Object> secondaryMap = (Map<String, Object>) primaryEntry.getValue();

            for (Map.Entry<String, Object> secondaryEntry : secondaryMap.entrySet()) {
                String secondaryKey = secondaryEntry.getKey();
                JSONArray valuesList = (JSONArray) secondaryEntry.getValue();

                for (Object key : valuesList) {
                    if (bankAccountName.contains(key.toString())) {
                        insertData.put("primary_key", primaryKey);
                        insertData.put("secondary_key", secondaryKey);
                        insertData.put("keyword", key.toString());
                        found = true;
                        break;
                    }
                }
                if (found) {
                    break;
                }
            }
            if (found) {
                break;
            }
        }

        return insertData;
    }
}
