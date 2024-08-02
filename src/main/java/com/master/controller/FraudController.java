package com.master.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.jdbi.v3.core.Jdbi;

import com.fasterxml.jackson.databind.JsonNode;
import com.master.MasterConfiguration;
import com.master.api.ApiResponse;
import com.master.core.validations.HspIdSchema;
import com.master.db.model.Hsp;
import com.master.services.FraudService;
import com.master.services.HspService;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/fraud")
@Produces(MediaType.APPLICATION_JSON)
public class FraudController extends BaseController {
    public FraudService fraudService;
    private HspService hspService;

    public FraudController(MasterConfiguration configuration, Validator validator, Jdbi jdbi) {
        super(configuration, validator, jdbi);
        this.fraudService = new FraudService(configuration, jdbi);
        this.hspService = new HspService(configuration, jdbi);
    }

    private Response response(Response.Status status, Object data) {
        return Response.status(status).entity(data).build();
    }

    @POST
    @Path("/getRules")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getRules() {
        JsonNode data = this.fraudService.getRuleJson();
        if (data == null || data.isEmpty()) {
            return response(Response.Status.NOT_FOUND, new ApiResponse<>(false, "Could not fetch the rules", null));
        }
        return response(Response.Status.OK, new ApiResponse<>(true, "Rule Fetch Success", data));
    }

    @POST
    @Path("/getNeighborhoodHspJson")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getNeighborhoodHspJson(HspIdSchema body) {
        
        // Validating input
        Set<ConstraintViolation<HspIdSchema>> violations = validator.validate(body);
        if (!violations.isEmpty()) {
            String errorMessage = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining("; "));
            return response(Response.Status.BAD_REQUEST, new ApiResponse<>(false, errorMessage, null));
        }
    
        List<Hsp> hspList = this.hspService.getHspDataListByIds(body.getHspIds());
        if (hspList.isEmpty()) {
            return response(Response.Status.NOT_FOUND, new ApiResponse<>(false, "Enter a valid HSP", false));
        }
    
        List<String> hspNames = hspList.stream()
                .map(Hsp::getHspName)
                .collect(Collectors.toList());
    
        ApiResponse<Object> data = this.fraudService.getNeighborhoodHspJson();
        JsonNode jsonNode = (JsonNode) data.getData();
    
        List<String> pharmacyList = new ArrayList<>();
        if (jsonNode != null && jsonNode.isObject()) {
            Iterator<Map.Entry<String, JsonNode>> fields = jsonNode.fields();
            if (fields.hasNext()) {
                JsonNode pharmaciesNode = fields.next().getValue();
                if (pharmaciesNode.isArray()) {
                    for (JsonNode node : pharmaciesNode) {
                        pharmacyList.add(node.asText());
                    }
                }
            }
            // Use the first HSP name
            if (!hspNames.isEmpty()) {
                String lowerCaseHspName = hspNames.get(0).toLowerCase();
                boolean hasMatch = pharmacyList.stream()
                        .map(String::toLowerCase)
                        .anyMatch(lowerCaseHspName::contains);
    
                Map<String, Object> result = new HashMap<>();
                if (hasMatch) {
                    result.put("hsp_name", lowerCaseHspName); 
                    result.put("neighborhood_hsp", true);
                    return response(Response.Status.OK, new ApiResponse<>(true, "Neighborhood HSP", result));
                } else {
                    result.put("hsp_name", lowerCaseHspName);
                    result.put("neighborhood_hsp", false);
                    return response(Response.Status.NOT_FOUND, new ApiResponse<>(false, "Not a Neighborhood HSP", result));
                }
            }
        }
    
        return response(Response.Status.INTERNAL_SERVER_ERROR, new ApiResponse<>(false, "Invalid JSON format", null));
    }
}    