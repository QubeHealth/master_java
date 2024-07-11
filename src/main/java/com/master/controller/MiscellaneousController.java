package com.master.controller;

import java.util.HashMap;
import java.util.Map;

import org.jdbi.v3.core.Jdbi;

import com.master.MasterConfiguration;
import com.master.api.ApiResponse;
import com.master.core.validations.MiscDataSchema;
import com.master.db.model.Miscellaneous;
import com.master.services.MiscService;
import com.master.services.SelfFundedService;

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
        public Response MiscellaneousData(MiscDataSchema reqBody) {
                Miscellaneous response = service.getSelfFundedInstructions(reqBody.getKey());
                Map<String, Object> responseMap = new HashMap<>();
                responseMap.put("decimal_value1", response.getDecimalValue1());
                responseMap.put("decimal_value2", response.getDecimalValue2());
                responseMap.put("string1", response.getString1());
                responseMap.put("string2", response.getString2());
                responseMap.put("json_1", response.getJson1());
                return Response.status(Response.Status.OK)
                                .entity(new ApiResponse<>(true,
                                                "Successfully fetched",
                                                responseMap.get(reqBody.getColumn())))
                                .build();
        }
}
