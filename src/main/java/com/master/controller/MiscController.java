package com.master.controller;

import java.util.Map;
import java.util.Set;

import org.jdbi.v3.core.Jdbi;

import com.master.MasterConfiguration;
import com.master.api.ApiResponse;
import com.master.core.validations.SelfFundedDataSchema;
import com.master.services.MiscService;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/self-funded")
public class MiscController extends BaseController {
    private MiscService service;

    public MiscController(MasterConfiguration configuration, Validator validator, Jdbi jdbi) {
        super(configuration, validator, jdbi);
        this.service = new MiscService(configuration, jdbi);

    }

    @POST
    @Path("/details")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response hspBrandName(SelfFundedDataSchema reqBody) {
        Set<ConstraintViolation<SelfFundedDataSchema>> violations = validator.validate(reqBody);
        if (!violations.isEmpty()) {
            // Construct error message from violations
            String errorMessage = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .reduce("", (acc, msg) -> acc.isEmpty() ? msg : acc + "; " + msg);
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiResponse<>(false, errorMessage, null))
                    .build();
        }
        Map<String,Object> data = service.getSelfFundedConstants(reqBody.getType());
        return Response.status(data != null ? Response.Status.OK : Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ApiResponse<>(data != null, data != null ? "Successfully fetched" : "Failed to fetch",
                        data))
                .build();
    }

}
