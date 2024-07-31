package com.master.controller;

import org.jdbi.v3.core.Jdbi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.master.MasterConfiguration;
import com.master.api.ApiResponse;
import com.master.services.FamilySchemaService;
import com.master.utility.Helper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Validator;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/familySchema")
public class FamilySchemaController extends BaseController {
    FamilySchemaService familySchemaService;

    public FamilySchemaController(MasterConfiguration configuration, Validator validator, Jdbi jdbi) {
        super(configuration, validator, jdbi);
        this.familySchemaService = new FamilySchemaService(configuration, jdbi);
    }
    
    @POST
    @Path("/FamilyBenefitSchema")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getFamilyBenefitSchema(@Context HttpServletRequest request) {
        String tempKey = "add_family_schema";

        String jsonData = familySchemaService.getAddFamilytSchema(tempKey);

        if (jsonData == null) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(new ApiResponse<>(false, "Data Not Exist", null))
                    .build();
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode parsedJson = objectMapper.readTree(jsonData);

            return Response.status(Response.Status.OK)
                    .entity(new ApiResponse<>(true, "JSON sent successfully", parsedJson))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ApiResponse<>(false, "Failed to process JSON data", null))
                    .build();
        }
    }
}
