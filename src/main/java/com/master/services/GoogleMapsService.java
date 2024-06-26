package com.master.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.jdbi.v3.core.Jdbi;
import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.JsonNode;
import com.master.MasterConfiguration;
import com.master.api.ApiResponse;
import com.master.client.LinkageNwService;
import com.master.db.model.HspNameData;
import com.master.db.repository.HspDao;
import com.master.utility.Helper;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

//Fields to pick up are address components 

public class GoogleMapsService extends BaseService {

    private LinkageNwService linkageNwService;

    public GoogleMapsService(MasterConfiguration configuration, Jdbi jdbi) {
        super(configuration, jdbi);
        this.linkageNwService = new LinkageNwService(configuration);
    }

    // Method to add the results of the search to the table hsp
    public Long getTextSearch(Map<String, Object> textSearchResult) {
        HspDao hspDao = jdbi.onDemand(HspDao.class);
        return hspDao.insertTextSearch(textSearchResult);
    }

    // Service for an analysis on the name of the place returned
    public String nameAnalysis(String response, int id) throws IOException {
        HspDao hspDao = jdbi.onDemand(HspDao.class);
        HspNameData names = hspDao.getNamebyId(id);
        String officialName = names.getHspOfficialName();
        String name = names.getHospitalName();
        ObjectMapper objectMapper = new ObjectMapper();

        // Read the JSON file into a JsonNode
        JsonNode rootNode = objectMapper.readTree(new File(Helper.toJsonString(response)));
        JsonNode placesNode = rootNode.path("places");

        // Create a list to store the display names
        List<String> displayNames = new ArrayList<>();

        // Iterate through the places array and extract the display names
        if (placesNode.isArray()) {
            for (JsonNode placeNode : placesNode) {
                JsonNode displayNameNode = placeNode.path("displayName").path("text");
                if (!displayNameNode.isMissingNode()) {
                    displayNames.add(displayNameNode.asText());
                }
            }
        }

        // Print the display names
        for (String displayName : displayNames) {
            if (displayName.equals(officialName)) {
                // same then return the whole lot of stuff
            } else if (displayName.equals(name)) {
                // Then return the json file with the whole lot of stuff
                return null;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> textSearchResponse(String latitude, String longitude, String keyword) {
        ApiResponse<Object> responseFromLinkageId = linkageNwService.textSearchId(latitude, longitude, "25", keyword);
        Map<String, Object> dataId = new HashMap();
        dataId = (Map<String, Object>) responseFromLinkageId.getData();
        System.out.println(dataId);
        if (dataId.isEmpty()) {
            return null;
        }
        ApiResponse<Object> responseFromLinkage = linkageNwService.textSearch(latitude, longitude, "25", keyword);
        Map<String, Object> data = new HashMap();
        data = (Map<String, Object>) responseFromLinkage.getData();
        System.out.println(data);

        // Now send for analysis

        JSONObject jsonObject = new JSONObject(data.toString());
        JSONArray placesArray = jsonObject.getJSONArray("places");

        List<Map<String, Object>> placesList = new ArrayList<>();

        for (int i = 0; i < placesArray.length(); i++) {
            JSONObject placeObject = placesArray.getJSONObject(i);
            Map<String, Object> placeMap = new HashMap<>();

            placeMap.put("types", Helper.jsonArrayToList(placeObject.getJSONArray("types")));
            placeMap.put("nationalPhoneNumber", placeObject.getString("nationalPhoneNumber"));
            placeMap.put("formattedAddress", placeObject.getString("formattedAddress"));
            placeMap.put("addressComponents", extractAddressComponents(placeObject.getJSONArray("addressComponents")));
            placeMap.put("location", extractLocation(placeObject.getJSONObject("location")));
            placeMap.put("websiteUri", placeObject.getString("websiteUri"));
            placeMap.put("displayName", extractText(placeObject.getJSONObject("displayName")));
            placeMap.put("primaryTypeDisplayName", extractText(placeObject.getJSONObject("primaryTypeDisplayName")));

            placesList.add(placeMap);
        }
        analyzeDataCountry(placesList);
        Map<String, Object> cleanedData = new HashMap<>();
        cleanedData = cleandata(placesList);
        return cleanedData;

    }

    @SuppressWarnings("unchecked")
    private void analyzeDataCountry(List<Map<String, Object>> googleResponse) {
        // This is for checking which countries the responses are from 

        for (int i = 0; i < googleResponse.size(); i++) {
            Map<String, Object> singlePlaceMap = googleResponse.get(i);
                @SuppressWarnings("rawtypes")
                Map<String, Object> addressComponents = new HashMap();
                addressComponents = (Map<String, Object>) singlePlaceMap.get("addressComponents");
                if (!addressComponents.get("country").equals("India")) {
                    googleResponse.remove(i);
                }
           

                

        }
    }

    private Map<String, Object> extractAddressComponents(JSONArray jsonArray) {
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> particularComponent = new HashMap();
        for (int i = 0; i < jsonArray.length(); i++) {

            JSONObject addressObject = jsonArray.getJSONObject(i);
            particularComponent.put(addressObject.getJSONArray("types").get(0).toString(),
                    addressObject.getString("longText"));
        }
        return particularComponent;
    }

    private Map<String, Double> extractLocation(JSONObject jsonObject) {
        Map<String, Double> locationMap = new HashMap<>();
        locationMap.put("latitude", jsonObject.getDouble("latitude"));
        locationMap.put("longitude", jsonObject.getDouble("longitude"));
        return locationMap;
    }

    private Map<String, String> extractText(JSONObject jsonObject) {
        Map<String, String> textMap = new HashMap<>();
        textMap.put("text", jsonObject.getString("text"));
        textMap.put("languageCode", jsonObject.getString("languageCode"));
        return textMap;
    }

    private Map<String, Object> cleandata(List<Map<String, Object>> googleResponse){
        Map<String, Object> data = new HashMap<>();
        data.put("name", googleResponse.get(6));
        data.put("address", googleResponse.get(2));
        data.put("state",googleResponse.get(3).get("administrative_area_level_1"));
        data.put("state",googleResponse.get(3).get("administrative_area_level_3"));
        data.put("state",googleResponse.get(3).get("postal_code"));
        data.put("mobile_number", googleResponse.get(1));
        data.put("website", googleResponse.get(5));

        return data;
    }

}