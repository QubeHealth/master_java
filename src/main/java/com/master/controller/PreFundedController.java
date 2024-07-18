package com.master.controller;

import com.master.services.PreFundedService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;

import com.master.MasterConfiguration;
import com.master.api.ApiResponse;
import com.master.core.validations.PreFundedSchema;
import com.master.db.repository.PreFundedDao;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/prefunded")
public class PreFundedController extends BaseController {
        private PreFundedService service;

        public PreFundedController(MasterConfiguration configuration, Validator validator, Jdbi jdbi) {
                super(configuration, validator, jdbi);
                this.service = new PreFundedService(configuration, jdbi);
        }

        @POST
        @Path("/getEmailMetaData")
        @Produces(MediaType.APPLICATION_JSON)
        @Consumes(MediaType.APPLICATION_JSON)
        public Response getEmailMetaData(PreFundedSchema body) {
                
        final PreFundedDao preFundedDao = jdbi.onDemand(PreFundedDao.class);
                String metadata = preFundedDao.getMetaData(body.getClaimNo());
        
                if (metadata == null) {
                        return Response.status(Response.Status.OK)
                                        .entity(new ApiResponse<>(true,
                                                        "Data Not Exist",
                                                        metadata))
                                        .build();
                }
                return Response.status(Response.Status.OK)
                                .entity(new ApiResponse<>(true,
                                                "Successfully fetched", metadata))
                                .build();
        }
}
