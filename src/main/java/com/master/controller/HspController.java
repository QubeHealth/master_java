package com.master.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.jdbi.v3.core.Jdbi;

import com.master.MasterConfiguration;
import com.master.api.ApiResponse;
import com.master.client.LinkageNwService;
import com.master.core.constants.Constants;
import com.master.core.validations.GetVpaByMobileSchema;
import com.master.core.validations.HspIdSchema;
import com.master.db.model.Hsp;
import com.master.services.HspService;
import com.master.utility.Helper;

import jakarta.validation.Validator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/hsp")
@Produces(MediaType.APPLICATION_JSON)
public class HspController extends BaseController {
    private LinkageNwService linkageNwService;
    private HspService hspService;

    public HspController(MasterConfiguration configuration, Validator validator, Jdbi jdbi) {
        super(configuration, validator, jdbi);

        this.linkageNwService = new LinkageNwService(configuration);
        this.hspService = new HspService(configuration, jdbi);
    }

    private Response response(Response.Status status, Object data) {
        return Response.status(status).entity(data).build();
    }

    @POST
    @Path("/getVpaByMobile")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getVpaByMobile(@Context HttpServletRequest request,
            GetVpaByMobileSchema body) {

        Set<ConstraintViolation<GetVpaByMobileSchema>> violations = validator.validate(body);
        if (!violations.isEmpty()) {
            // Construct error message from violations
            String errorMessage = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .reduce("", (acc, msg) -> acc.isEmpty() ? msg : acc + "; " + msg);
            return response(Response.Status.BAD_REQUEST, new ApiResponse<>(false, errorMessage, null));
        }

        ApiResponse<Object> befiscRes = this.linkageNwService.getVpaByMobile(Long.parseLong(body.getMobile()));
        logger.info("BEFISC RESPONSE : {}", Helper.toJsonString(befiscRes));

        if (!befiscRes.getStatus()) {
            return response(Response.Status.NOT_FOUND, befiscRes);
        }

        Map<String, Object> data = (Map<String, Object>) befiscRes.getData();

        String accountName = data.get("accountName").toString().toLowerCase();
        String merchantName = data.get("name").toString().toLowerCase();

        boolean validHsp = false;

        for (String candidateKeyword : Constants.hspKeywords) {
            candidateKeyword = candidateKeyword.toLowerCase();
            if (accountName.contains(candidateKeyword) || merchantName.contains(candidateKeyword)) {
                validHsp = true;
                break;
            }
        }

        String uuid = UUID.randomUUID().toString();
        data.put("uuid", uuid);
        data.put("mobile", body.getMobile());
        data.put("status", validHsp ? "VERIFIED" : "PENDING");

        Long hspId = this.hspService.insertHspByMobile(data);

        if (hspId == null) {
            return response(Response.Status.FORBIDDEN,
                    new ApiResponse<>(false, "Failed to add hsp", null));

        }

        return response(Response.Status.OK,
                new ApiResponse<>(validHsp, validHsp ? "Valid Healthcase" : "Invalid Healthcare",
                        Map.of("vpa", data.get("vpa"), "merchant_name", merchantName, "hsp_id", hspId)));

    }

    @POST
    @Path("/getHspListByIds")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getHspListByIds(HspIdSchema body) {

        Set<ConstraintViolation<HspIdSchema>> violations = validator.validate(body);
        if (!violations.isEmpty()) {
            // Construct error message from violations
            String errorMessage = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .reduce("", (acc, msg) -> acc.isEmpty() ? msg : acc + "; " + msg);
            return response(Response.Status.BAD_REQUEST, new ApiResponse<>(false, errorMessage, null));
        }

        List<Hsp> hspList = this.hspService.getHspDataListByIds(body.getHspIds());

        if (hspList == null || hspList.isEmpty()) {
            return response(Response.Status.NOT_FOUND, new ApiResponse<>(false, "No hsp data found", null));
        }

        return response(Response.Status.OK, new ApiResponse<>(true, "Hsp details fetch success", hspList));

    }

}
