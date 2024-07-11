package com.master.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jdbi.v3.core.Jdbi;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.JsonObject;
import com.master.MasterConfiguration;
import com.master.api.ApiResponse;
import com.master.core.validations.MiscDataSchema;
import com.master.core.validations.SelfFundedDataSchema;
import com.master.db.model.Miscellaneous;
import com.master.services.MiscService;
import com.master.services.SelfFundedService;
import com.master.utility.Helper;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/miscellaneous")
public class MiscellaneousController extends BaseController {
    private MiscService service;

    public MiscellaneousController(MasterConfiguration configuration, Validator validator, Jdbi jdbi) {
        super(configuration, validator, jdbi);
        this.service = new MiscService(configuration, jdbi);
    }

    @POST
    @Path("/data")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response miscellaneousData(MiscDataSchema reqBody) {
        Set<ConstraintViolation<MiscDataSchema>> violations = validator.validate(reqBody);
        if (!violations.isEmpty()) {
            // Construct error message from violations
            String errorMessage = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .reduce("", (acc, msg) -> acc.isEmpty() ? msg : acc + "; " + msg);
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiResponse<>(false, errorMessage, null))
                    .build();
        }

        Miscellaneous response = service.getMiscData(reqBody.getKey(), reqBody.getColumnNames());

        if (reqBody.getColumnNames().equals("json_1")) {
            
            Object jsonContent = parseJson(response.getJson1());
            JSONObject jsonWrapper = new JSONObject();
            jsonWrapper.put("json_1", jsonContent);

            return Response.status(Response.Status.OK)
                .entity(new ApiResponse<>(true,
                        "Successfully fetched",
                        jsonWrapper))
                .build();
        }
        return Response.status(Response.Status.OK)
                .entity(new ApiResponse<>(true,
                        "Successfully fetched",
                        response))
                .build();
    }
    private static Object parseJson(String jsonString) {
        try {
            // Try to parse as JSONObject
            return new JSONObject(jsonString);
        } catch (Exception e) {
            try {
                // Try to parse as JSONArray
                return new JSONArray(jsonString);
            } catch (Exception ex) {
                // Handle parse errors or return null as needed
                ex.printStackTrace();
                return null;
            }
        }
    }

}
