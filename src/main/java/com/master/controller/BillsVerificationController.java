package com.master.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jdbi.v3.core.Jdbi;

import com.master.MasterConfiguration;
import com.master.api.ApiResponse;
import com.master.core.validations.BillSchema;
import com.master.db.model.HospitalDropdownBillsVerification;
import com.master.db.model.SavePartnershipSchema;
import com.master.services.BillsVerificationService;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/billsVerification")
@Produces(MediaType.APPLICATION_JSON)
public class BillsVerificationController extends BaseController {
    public BillsVerificationService billsVerificationService;

    public BillsVerificationController(MasterConfiguration configuration, Validator validator, Jdbi jdbi) {
        super(configuration, validator, jdbi);
        this.billsVerificationService = new BillsVerificationService(configuration, jdbi);

    }

    private Response response(Response.Status status, Object data) {
        return Response.status(status).entity(data).build();
    }

    @POST
    @Path("/getHospitalDropdown")
    public Response getHospitalDropdown(BillSchema.ValidateBillSchema reqBody) {
        Set<ConstraintViolation<BillSchema.ValidateBillSchema>> violations = validator.validate(reqBody);
        if (!violations.isEmpty()) {
            // Construct error message from violations
            String errorMessage = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .reduce("", (acc, msg) -> acc.isEmpty() ? msg : acc + "; " + msg);
            return response(Response.Status.BAD_REQUEST, new ApiResponse<>(false, errorMessage, null));
        }
        List<HospitalDropdownBillsVerification> data = this.billsVerificationService.getHospitalList(reqBody.getSearchInput());
        if (data == null || data.isEmpty()) {
            return response(Response.Status.NOT_FOUND,
                    new ApiResponse<>(false, "No hospital details found", new ArrayList<>()));
        }
        Set<String> uniqueHospitalNames = new HashSet<>();
        for (HospitalDropdownBillsVerification hospital : data) {
            uniqueHospitalNames.add(hospital.getHospitalName());
        }
        List<String> hospitalNames = new ArrayList<>(uniqueHospitalNames);
        return response(Response.Status.OK, new ApiResponse<>(true, "Hospital details fetch success", hospitalNames));
    }


    @POST
    @Path("/updateHospitalDetails")
    public Response savePartnershipDetails(BillSchema.ValidateInsertSchema reqBody) {
        Set<ConstraintViolation<BillSchema.ValidateInsertSchema>> violations = validator.validate(reqBody);
        if (!violations.isEmpty()) {
            // Construct error message from violations
            String errorMessage = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .reduce("", (acc, msg) -> acc.isEmpty() ? msg : acc + "; " + msg);
            return response(Response.Status.BAD_REQUEST, new ApiResponse<>(false, errorMessage, null));
        }
        Integer data = this.billsVerificationService.saveHospitalName(reqBody);
        if (data == null) {
            return response(Response.Status.NOT_FOUND,
                    new ApiResponse<>(false, "Couldn't save data", null));
        }
        return response(Response.Status.OK, new ApiResponse<>(true, "Save data success", data));
    }
}
