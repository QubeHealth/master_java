package com.master.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jdbi.v3.core.Jdbi;
import org.json.JSONObject;

import com.master.MasterConfiguration;
import com.master.api.ApiResponse;
import com.master.api.InsertHspBrandName;
import com.master.api.QrData;
import com.master.client.LinkageNwService;
import com.master.core.validations.HspIdSchema;
import com.master.core.validations.SaveHspBrandName;
import com.master.core.validations.PaymentSchemas.BankSchema;
import com.master.core.validations.PaymentSchemas.MobileSchema;
import com.master.core.validations.PaymentSchemas.QrDataSchema;
import com.master.core.validations.PaymentSchemas.QrSchema;
import com.master.core.validations.PaymentSchemas.VpaSchemas;
import com.master.db.model.Hsp;
import com.master.db.model.HspMetadata;
import com.master.db.model.PartnerCategory;
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

    @POST
    @Path("/validateQr")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response validateQr(@Context HttpServletRequest request,
            QrSchema body) {

        Set<ConstraintViolation<QrSchema>> violations = validator.validate(body);
        if (!violations.isEmpty()) {
            // Construct error message from violations
            String errorMessage = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .reduce("", (acc, msg) -> acc.isEmpty() ? msg : acc + "; " + msg);
            return response(Response.Status.BAD_REQUEST, new ApiResponse<>(false, errorMessage, null));
        }

        // parse the normal upi url
        QrData parsedQr = Helper.parseUPIUrl(body.getUpiQrUrl());

        if (parsedQr == null) {
            parsedQr = Helper.parseEMVQR(body.getUpiQrUrl());
        }

        if (parsedQr == null) {
            return response(Response.Status.FORBIDDEN, new ApiResponse<>(false, "Invalid qr format", null));
        }
        return response(Response.Status.OK, new ApiResponse<>(true, "QR validation success", parsedQr));

    }

    @POST
    @Path("/saveQrData")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveQrData(@Context HttpServletRequest request,
            QrDataSchema body) {

        // parse the normal upi url
        QrData parsedQr = Helper.parseUPIUrl(body.getUpiQrUrl());

        if (parsedQr == null) {
            parsedQr = Helper.parseEMVQR(body.getUpiQrUrl());
        }
        String bankAccountName = null;

        if (body.getLevel().equals("MCC_CODE") || body.getLevel().equals("MERCHANT_NAME_QR")) {
            bankAccountName = hspService.validateOnBankAccountName(parsedQr.getVpa(), body.getHspId());
            if (bankAccountName == null) {
                return response(Response.Status.BAD_REQUEST,
                        new ApiResponse<>(false, "Failed to get the bank account name", null));
            }
        }

        Map<String, Object> qrInsertData = new HashMap<>();

        qrInsertData.put("user_id", body.getUserId());
        qrInsertData.put("hsp_id", body.getHspId());
        qrInsertData.put("qr_url", body.getUpiQrUrl());
        qrInsertData.put("vpa", parsedQr.getVpa());
        qrInsertData.put("mcc_code", parsedQr.getMccCode().isBlank() ? null : parsedQr.getMccCode());
        qrInsertData.put("merchant_name", parsedQr.getMerchantName());

        qrInsertData.put("bank_account_name", bankAccountName == null ? body.getBankAccountName() : bankAccountName);
        qrInsertData.put("keyword", body.getKeyword());

        qrInsertData.put("is_valid", body.isValid());
        qrInsertData.put("merchant_city", parsedQr.getMerchantCity());
        qrInsertData.put("pincode", parsedQr.getMerchantPincode());
        qrInsertData.put("level", body.getLevel());

        qrInsertData.put("amount", parsedQr.getAmount());
        qrInsertData.put("txn_id", parsedQr.getTransactionId());
        qrInsertData.put("qr_expiry", null);

        Long success = hspService.insertHspQrData(qrInsertData);
        if (success == null) {
            return response(Response.Status.FORBIDDEN, new ApiResponse<>(false, "Failed to save qr data", null));
        }

        if (body.isValid() && bankAccountName != null) {

            HspMetadata inTable = hspService.getHspMetadata(body.getHspId());
            if (inTable != null) {
                return response(Response.Status.OK, new ApiResponse<>(true, "Data inserted successfully", null));
            }

            PartnerCategory partnerCategory = hspService.getPartnerCategory("partnership_category");
            JSONObject jsonObject = new JSONObject(partnerCategory.getJson1());
            Map<String, Object> categoryMap = Helper.jsonToMap(jsonObject);

            Map<String, Object> insertData = extractHspCategory(categoryMap, bankAccountName);

        }

        return response(Response.Status.OK, new ApiResponse<>(true, "Data inserted successfully", null));
    }

    private Map<String, Object> extractHspCategory(Map<String, Object> categoryMap, String bankAccountName) {
        Map<String, Object> insertData = new HashMap<>();
        boolean found = false;

        for (Map.Entry<String, Object> primaryEntry : categoryMap.entrySet()) {
            String primaryKey = primaryEntry.getKey();

            Map<String, List<String>> secondaryMap = (Map<String, List<String>>) primaryEntry.getValue();

            for (Map.Entry<String, List<String>> secondaryEntry : secondaryMap.entrySet()) {
                String secondaryKey = secondaryEntry.getKey();
                List<String> valuesList = secondaryEntry.getValue();

                for (String key : valuesList) {
                    if (bankAccountName.contains(key)) {
                        insertData.put("primary_key", primaryKey);
                        insertData.put("secondary_key", secondaryKey);
                        found = true;
                        break;
                    }
                }
                if (found) {
                    break;
                }
            }
            if (found) {
                break;
            }
        }

        return insertData;
    }
}
