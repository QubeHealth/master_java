package com.master.services;

import org.jdbi.v3.core.Jdbi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.master.MasterConfiguration;
import com.master.api.ApiResponse;
import com.master.db.model.Miscellaneous;
import com.master.db.repository.MiscDao;

public class FraudService extends BaseService {


    public FraudService(MasterConfiguration configuration, Jdbi jdbi) {
        super(configuration, jdbi);
    }

    public JsonNode getRuleJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        MiscDao miscDao = jdbi.onDemand(MiscDao.class);
        Miscellaneous a = miscDao.getSelfundedDetails("fraud_detection");
        String json = a.getJson1();
        try {
            // Parse the JSON string to a JsonNode
            JsonNode jsonNode = objectMapper.readTree(json);
            return jsonNode;
        } catch (JsonProcessingException e) {
            // Log the exception and handle it accordingly
            e.printStackTrace();
            return null;
        }
    }

    public ApiResponse<Object> getNeighborhoodHspJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        MiscDao miscDao = jdbi.onDemand(MiscDao.class);
        Miscellaneous a = miscDao.getSelfundedDetails("neighborhood_hsp");
        String json = a.getJson1();
        try {
            // Parse the JSON string to a JsonNode
            JsonNode jsonNode = objectMapper.readTree(json);
            return new ApiResponse<Object>(true, "Data fetched succesfully", jsonNode);
        } catch (JsonProcessingException e) {
            // Log the exception and handle it accordingly
            e.printStackTrace();
            return null;
        }
    }


    public JsonNode getNeighborhoodHsp() {
        ObjectMapper objectMapper = new ObjectMapper();
        MiscDao miscDao = jdbi.onDemand(MiscDao.class);
        Miscellaneous a = miscDao.getSelfundedDetails("neighborhood_hsp");
        String json = a.getJson1();
        try {
            // Parse the JSON string to a JsonNode
            JsonNode jsonNode = objectMapper.readTree(json);
            return jsonNode;
        } catch (JsonProcessingException e) {
            // Log the exception and handle it accordingly
            e.printStackTrace();
            return null;
        }
    }

}

