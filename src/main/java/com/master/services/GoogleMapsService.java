package com.master.services;
import java.util.ArrayList;
import java.util.Map;
import org.jdbi.v3.core.Jdbi;

import com.fasterxml.jackson.databind.JsonNode;
import com.master.MasterConfiguration;
import com.master.api.ApiResponse;
import com.master.client.LinkageNwService;
import com.master.db.model.HspNameData;
import com.master.db.repository.HspDao;
import com.master.utility.Helper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;
public class GoogleMapsService extends BaseService {

    private LinkageNwService linkageNwService;
    public GoogleMapsService(MasterConfiguration configuration, Jdbi jdbi) {
        super(configuration, jdbi);
        this.linkageNwService = new LinkageNwService(configuration);
    }

    public Long getNearbySearch(Map<String, Object> get) {
        HspDao hspDao = jdbi.onDemand(HspDao.class);
        return hspDao.insertNearbySearch(get);
    }


    public String headCall(String latitude, String longitude)
    {
        //So first step is to get the lat, long, radius. With this, make the first linkage call and do the validations
         ApiResponse<Object> nearRes = this.linkageNwService.nearbySearch((latitude),
                longitude, "25");
        
        if((Helper.toJsonString(nearRes.getData())==null)){
            //Here the call to the second case
            return secondCall(latitude,longitude);
        }
        return Helper.toJsonString(nearRes.getData());

    }

    public String secondCall(String latitude, String longitude){
        ApiResponse<Object> nearRes = this.linkageNwService.nearbySearch((latitude),
        longitude, "25");
        if((Helper.toJsonString(nearRes.getData())==null)){
            return thirdCall(latitude,longitude);

        }
        return Helper.toJsonString(nearRes.getData());

    }
    public String thirdCall(String latitude, String longitude){
        ApiResponse<Object> nearRes = this.linkageNwService.nearbySearch((latitude),
        longitude, "25");
        if((Helper.toJsonString(nearRes.getData())==null)){
            //Here the call to the second case
            return fourthCall(latitude,longitude);

        }
        return Helper.toJsonString(nearRes.getData());
    }
    public String fourthCall(String latitude, String longitude){
        ApiResponse<Object> nearRes = this.linkageNwService.nearbySearch((latitude),
        longitude, "25");
        if((Helper.toJsonString(nearRes.getData())==null)){
            //Here the call to the second case
            return(null);
        }
        return Helper.toJsonString(nearRes.getData());
    }

    //Service for an analysis on the name of the 
    public String nameAnalysis(String response,int id) throws IOException{
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
                if(displayName.equals(officialName)){
                    //same then return the whole lot of stuff
                }
                else if(displayName.equals(name)){
                 //Then return the json file with the whole lot of stuff
                 return null;
                }
    }
    return null; 
}
}