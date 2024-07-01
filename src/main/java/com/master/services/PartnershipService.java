package com.master.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdbi.v3.core.Jdbi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.master.MasterConfiguration;
import com.master.db.model.Miscellaneous;
import com.master.db.model.PartnershipSchema;
import com.master.db.model.SavePartnershipSchema;
import com.master.db.repository.MiscDao;
import com.master.db.repository.PartnershipDao;

public class PartnershipService extends BaseService {

    public PartnershipService(MasterConfiguration configuration, Jdbi jdbi) {
        super(configuration, jdbi);
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
}
