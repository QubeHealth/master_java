package com.master.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jdbi.v3.core.Jdbi;

import com.master.MasterConfiguration;
import com.master.api.ApiResponse;
import com.master.api.InsertHspBrandName;
import com.master.client.LinkageNwService;
import com.master.core.validations.HspIdSchema;
import com.master.core.validations.SaveHspBrandName;
import com.master.core.validations.PaymentSchemas.BankSchema;
import com.master.core.validations.PaymentSchemas.MobileSchema;
import com.master.core.validations.PaymentSchemas.VpaSchemas;
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
            MobileSchema body) {

        Set<ConstraintViolation<MobileSchema>> violations = validator.validate(body);
        if (!violations.isEmpty()) {
            // Construct error message from violations
            String errorMessage = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .reduce("", (acc, msg) -> acc.isEmpty() ? msg : acc + "; " + msg);
            return response(Response.Status.BAD_REQUEST, new ApiResponse<>(false, errorMessage, null));
        }

        Map<String, Object> hspRes = this.hspService.getHspByMobile(body.getMobile());
        if (hspRes != null) {
            return response(Response.Status.OK, new ApiResponse<>(true, "HSP already exist", hspRes));
        }

        ApiResponse<Object> befiscRes = this.linkageNwService.getVpaByMobile(Long.parseLong(body.getMobile()));
        logger.info("BEFISC RESPONSE : {}", Helper.toJsonString(befiscRes));

        if (!befiscRes.getStatus()) {
            return response(Response.Status.NOT_FOUND, befiscRes);
        }

        Map<String, Object> data = (Map<String, Object>) befiscRes.getData();
        data.put("mobile", body.getMobile());

        Long hspId = this.hspService.insertHspByMobile(data);

        if (hspId == null) {
            return response(Response.Status.FORBIDDEN,
                    new ApiResponse<>(false, "Failed to add hsp", null));
        }
        data.put("hsp_id", hspId);
        data.remove("keyword");
        data.remove("mobile");

        return response(Response.Status.OK, new ApiResponse<>(true, "Vpa fetch success", data));

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

    @POST
    @Path("/saveHspBrandName")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response hspBrandName(SaveHspBrandName reqBody) {
        Set<ConstraintViolation<SaveHspBrandName>> violations = validator.validate(reqBody);
        if (!violations.isEmpty()) {
            // Construct error message from violations
            String errorMessage = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .reduce("", (acc, msg) -> acc.isEmpty() ? msg : acc + "; " + msg);
            return response(Response.Status.BAD_REQUEST, new ApiResponse<>(false, errorMessage, null));
        }

        InsertHspBrandName response = this.hspService.hspBrandName(reqBody);
        return Response.status(response.getStatus() ? Response.Status.OK : Response.Status.INTERNAL_SERVER_ERROR)
                .entity(response)
                .build();
    }

    @POST
    @Path("/validateVpa")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response validateVpa(@Context HttpServletRequest request,
            VpaSchemas body) {

        Set<ConstraintViolation<VpaSchemas>> violations = validator.validate(body);
        if (!violations.isEmpty()) {
            // Construct error message from violations
            String errorMessage = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .reduce("", (acc, msg) -> acc.isEmpty() ? msg : acc + "; " + msg);
            return response(Response.Status.BAD_REQUEST, new ApiResponse<>(false, errorMessage, null));
        }

        Map<String, Object> hspRes = this.hspService.getHspByVpa(body.getVpa());
        if (hspRes != null) {
            return response(Response.Status.OK, new ApiResponse<>(true, "Hsp already exist", hspRes));
        }

        ApiResponse<Object> befiscRes = this.linkageNwService.validateVpa((body.getVpa()));
        logger.info("BEFISC RESPONSE : {}", Helper.toJsonString(befiscRes));

        if (!befiscRes.getStatus()) {
            return response(Response.Status.NOT_FOUND, befiscRes);
        }

        Map<String, Object> data = (Map<String, Object>) befiscRes.getData();
        data.put("mobile", null);

        Long hspId = this.hspService.insertHspByMobile(data);

        if (hspId == null) {
            return response(Response.Status.FORBIDDEN,
                    new ApiResponse<>(false, "Failed to add hsp", null));
        }
        data.put("hsp_id", hspId);
        data.remove("keyword");
        data.remove("mobile");

        return response(Response.Status.OK, new ApiResponse<>(true, "Vpa validation success", data));

    }

    @POST
    @Path("/validateBankAccount")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response validateBankAccount(@Context HttpServletRequest request,
            BankSchema body) {

        Set<ConstraintViolation<BankSchema>> violations = validator.validate(body);
        if (!violations.isEmpty()) {
            // Construct error message from violations
            String errorMessage = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .reduce("", (acc, msg) -> acc.isEmpty() ? msg : acc + "; " + msg);
            return response(Response.Status.BAD_REQUEST, new ApiResponse<>(false, errorMessage, null));
        }

        Map<String, Object> hspRes = this.hspService.getHspByBankDetails(body);
        if (hspRes != null) {
            return response(Response.Status.OK, new ApiResponse<>(true, "HSP already exist", hspRes));
        }

        ApiResponse<Object> befiscRes = this.linkageNwService.validateBankDetails((body.getAccountNumber()),
                body.getIfscCode());
        logger.info("BEFISC RESPONSE : {}", Helper.toJsonString(befiscRes));

        if (!befiscRes.getStatus()) {
            return response(Response.Status.NOT_FOUND, befiscRes);
        }

        Map<String, Object> data = (Map<String, Object>) befiscRes.getData();

        Long hspId = this.hspService.insertHspBank(data);

        if (hspId == null) {
            return response(Response.Status.FORBIDDEN,
                    new ApiResponse<>(false, "Failed to add hsp", null));
        }
        data.put("hsp_id", hspId);
        data.put("merchant_name", data.get("bank_account_name"));
        data.remove("keyword");

        return response(Response.Status.OK, new ApiResponse<>(true, "Bank validation success", data));

    }
}
