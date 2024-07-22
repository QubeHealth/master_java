package com.master.controller;

import com.master.services.PreFundedMailService;
import com.master.services.PreFundedMailService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;

import com.master.MasterConfiguration;
import com.master.api.ApiResponse;
import com.master.core.validations.PreFundedMailSchema;
import com.master.core.validations.PreFundedMailSchema;
import com.master.db.repository.PreFundedMailDao;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/prefunded")
public class PreFundedMailController extends BaseController {
        PreFundedMailService preFundedMailService;

        public PreFundedMailController(MasterConfiguration configuration, Validator validator, Jdbi jdbi) {
                super(configuration, validator, jdbi);
                this.preFundedMailService = new PreFundedMailService(configuration, jdbi);
        }

        @POST
        @Path("/getEmailMetaData")
        @Produces(MediaType.APPLICATION_JSON)
        @Consumes(MediaType.APPLICATION_JSON)
        public Response getEmailMetaData(PreFundedMailSchema body) {
        
                String metadata = this.preFundedMailService.getEmailMetaData(body.getClaimNo());
                
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
