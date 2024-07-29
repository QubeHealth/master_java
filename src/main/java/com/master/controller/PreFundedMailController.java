package com.master.controller;

import com.master.services.PreFundedMailService;

import java.util.Set;

import org.jdbi.v3.core.Jdbi;

import com.master.MasterConfiguration;
import com.master.api.ApiResponse;
import com.master.core.validations.PreFundedMailSchema;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/prefunded")
public class PreFundedMailController extends BaseController {
        PreFundedMailService preFundedMailService;

        public PreFundedMailController(MasterConfiguration configuration, Validator validator, Jdbi jdbi) {
                super(configuration, validator, jdbi);
                this.preFundedMailService = new PreFundedMailService(configuration, jdbi);
        }

        private Response response(Response.Status status, Object data) {
                return Response.status(status).entity(data).build();
        }

        @POST
        @Path("/getEmailMetaData")
        @Produces(MediaType.APPLICATION_JSON)
        @Consumes(MediaType.APPLICATION_JSON)
        public Response getEmailMetaData(@Context HttpServletRequest request, PreFundedMailSchema body) {
                Set<ConstraintViolation<PreFundedMailSchema>> violations = validator.validate(body);
                if (!violations.isEmpty()) {
                        String errorMessage = violations.stream()
                                        .map(ConstraintViolation::getMessage)
                                        .reduce("", (acc, msg) -> acc.isEmpty() ? msg : acc + "; " + msg);
                        return response(Response.Status.BAD_REQUEST, new ApiResponse<>(false, errorMessage, null));
                }
                try {
                        String metadata = this.preFundedMailService.getEmailMetaData(body.getClaimNo());

                        if (metadata == null) {
                                return Response.status(Response.Status.FORBIDDEN)
                                                .entity(new ApiResponse<>(false, "Data Not Exist", metadata))
                                                .build();
                        }
                        return Response.status(Response.Status.OK)
                                        .entity(new ApiResponse<>(true, "Metadata sended Successfully", metadata))
                                        .build();
                } catch (Exception e) {
                        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                                        .entity(new ApiResponse<>(false, "error while fetching email metadata",
                                                        e.getMessage()))
                                        .build();
                }
        }
}
