package com.master.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jdbi.v3.core.Jdbi;
import com.master.MasterConfiguration;
import com.master.api.ApiResponse;
import com.master.db.model.PartnershipSchema;
import com.master.db.model.SavePartnershipSchema;
import com.master.services.PartnershipService;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/category")
@Produces(MediaType.APPLICATION_JSON)
public class PartnershipController extends BaseController {
    public PartnershipService partnershipService;

    public PartnershipController(MasterConfiguration configuration, Validator validator, Jdbi jdbi) {
        super(configuration, validator, jdbi);
        this.partnershipService = new PartnershipService(configuration, jdbi);
    }

    private Response response(Response.Status status, Object data) {
        return Response.status(status).entity(data).build();
    }

    @POST
    @Path("/categoryList")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getcatlist() {
        Map<String, List<String>> catList = this.partnershipService.getCategoryDetails();
        Set<String> keySet = catList.keySet();
        List<String> categoryList = new ArrayList<>(keySet);
        Map<String, List<String>> subCatList = this.partnershipService.getSubCategoryDetails();
        Set<String> keySetSub = subCatList.keySet();
        List<String> SubCategoryList = new ArrayList<>(keySetSub);
        if (catList == null || catList.isEmpty()) {
            return response(Response.Status.NOT_FOUND, new ApiResponse<>(false, "No category data found", null));
        }
        Map<String, Object> data = new HashMap<>();
        data.put("category", categoryList);
        data.put("sub_category", SubCategoryList);
        return response(Response.Status.OK, new ApiResponse<>(true, "Category details fetch success", data));
    }

    @POST
    @Path("/getPartnershipHospitalDetails")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getHspDetails() {
        List<PartnershipSchema> data = this.partnershipService.getHspDetails();
        if (data == null || data.isEmpty()) {
            return response(Response.Status.NOT_FOUND,
                    new ApiResponse<>(false, "No hospital details found", new ArrayList<>()));
        }
        return response(Response.Status.OK, new ApiResponse<>(true, "Hospital details fetch success", data));
    }

    @POST
    @Path("/savePartnershipDetails")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response savePartnershipDetails(SavePartnershipSchema reqBody) {
        Set<ConstraintViolation<SavePartnershipSchema>> violations = validator.validate(reqBody);
        if (!violations.isEmpty()) {
            // Construct error message from violations
            String errorMessage = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .reduce("", (acc, msg) -> acc.isEmpty() ? msg : acc + "; " + msg);
            return response(Response.Status.BAD_REQUEST, new ApiResponse<>(false, errorMessage, null));
        }
        Integer data = this.partnershipService.saveHospitalDetails(reqBody);
        if (data == null) {
            return response(Response.Status.NOT_FOUND,
                    new ApiResponse<>(false, "Couldn't save data", null));
        }
        return response(Response.Status.OK, new ApiResponse<>(true, "Save data success", data));
    }
}
