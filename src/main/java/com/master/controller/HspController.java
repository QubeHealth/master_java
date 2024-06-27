package com.master.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jdbi.v3.core.Jdbi;

import com.master.MasterConfiguration;
import com.master.api.ApiResponse;
import com.master.api.InsertHspBrandName;
import com.master.api.QrData.QrInfo;
import com.master.api.QrData.QrResponse;
import com.master.client.LinkageNwService;
import com.master.core.constants.Constants;
import com.master.core.validations.HspIdSchema;
import com.master.core.validations.SaveHspBrandName;
import com.master.core.validations.PaymentSchemas.BankSchema;
import com.master.core.validations.PaymentSchemas.MobileSchema;
import com.master.core.validations.PaymentSchemas.QrSchema;
import com.master.core.validations.PaymentSchemas.VpaSchemas;
import com.master.db.model.Hsp;
import com.master.services.HspService;
import com.master.utility.Helper;
import com.master.utility.sqs.ExecutionsConstants;
import com.master.utility.sqs.Producer;
import com.master.utility.sqs.QueueConstants;

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

        final Long userId = (Long) request.getAttribute("user_id");

        // parse the normal upi url
        QrInfo parsedQr = Helper.parseUPIUrl(body.getUpiQrUrl());

        // parse emv qr
        if (parsedQr == null) {
            parsedQr = Helper.parseEMVQR(body.getUpiQrUrl());
        }

        if (parsedQr == null || parsedQr.getVpa() == null) {
            return response(Response.Status.FORBIDDEN, new ApiResponse<>(false, "Invalid qr format", null));
        }

        QrResponse qrResponse = new QrResponse();
        qrResponse.setVpa(parsedQr.getVpa());
        qrResponse.setMerchantName(parsedQr.getMerchantName());

        Map<String, Object> rmqMap = new HashMap<>();
        rmqMap.put("upi_qr_url", body.getUpiQrUrl());
        rmqMap.put("level", null);
        rmqMap.put("is_valid", false);
        rmqMap.put("hsp_id", null);
        rmqMap.put("bank_account_name", null);
        rmqMap.put("keyword", null);
        rmqMap.put("user_id", userId);

        try {
            // check if dynamic qr
            if (parsedQr.getAmount() != null) {
                rmqMap.put("level", Constants.QrConstants.DYNAMIC_QR);
                qrResponse.setStatus(Constants.QrConstants.DYNAMIC_QR);

                Producer.addInQueue(QueueConstants.MASTER.exchange, ExecutionsConstants.SAVE_QR_DATA.key,
                        Helper.toJsonString(rmqMap));

                return response(Response.Status.OK, new ApiResponse<>(true, "QR validation success", qrResponse));
            }

            // check if already exist in db
            Hsp hsp = this.hspService.getHspbyQRVpa(parsedQr.getVpa());
            boolean validHsp = false;
            if (hsp != null) {

                validHsp = hsp.getStatus() != null && hsp.getStatus().equals(Constants.QrConstants.VERIFIED);
                rmqMap.put("level", Constants.QrConstants.DB);
                rmqMap.put("is_valid", validHsp);
                rmqMap.put("hsp_id", hsp.getHspId());

                qrResponse.setBankAccountName(hsp.getHspOfficialName());
                qrResponse.setHspId(hsp.getHspId());
                qrResponse.setMerchantName(hsp.getHspName());
                qrResponse.setStatus(validHsp ? Constants.QrConstants.VALID_HSP : Constants.QrConstants.INVALID_HSP);

                Producer.addInQueue(QueueConstants.MASTER.exchange, ExecutionsConstants.SAVE_QR_DATA.key,
                        Helper.toJsonString(rmqMap));

                return response(Response.Status.OK, new ApiResponse<>(true, "Hsp already exist", qrResponse));
            }

            // check if mcc code is valid
            if (parsedQr.getMccCode() != null) {
                Hsp mcc = this.hspService.getHspbyQRMcc(parsedQr.getMccCode());

                if (mcc != null && mcc.getMccCode() != null) {

                    Integer hspId = this.hspService.insertHspQr(parsedQr.getMerchantName(),
                            mcc.getMccCode(), parsedQr.getVpa(), parsedQr.getMerchantName(), true);

                    rmqMap.put("level", Constants.QrConstants.MCC_CODE);
                    rmqMap.put("is_valid", true);
                    rmqMap.put("hsp_id", hspId);

                    qrResponse.setHspId(hspId);
                    qrResponse.setStatus(Constants.QrConstants.VALID_HSP);

                    Producer.addInQueue(QueueConstants.MASTER.exchange, ExecutionsConstants.SAVE_QR_DATA.key,
                            Helper.toJsonString(rmqMap));

                    return response(Response.Status.OK, new ApiResponse<>(true, "Qr validation success", qrResponse));

                }

            }

            String hspAccName = parsedQr.getMerchantName();
            // check if valid on merchant name
            String healthKeyword = checkHspKeyword(hspAccName);
            validHsp = healthKeyword != null;

            rmqMap.put("level", Constants.QrConstants.MERCHANT_NAME);

            // if not valid on merchant name get the bank acc name
            if (Boolean.FALSE.equals(validHsp)) {
                ApiResponse<Object> vpaRes = this.linkageNwService.validateVpaOpen(parsedQr.getVpa());
                if (vpaRes.getStatus()) {
                    Map<String, Object> vpaData = (Map<String, Object>) vpaRes.getData();

                    hspAccName = vpaData.get("name_as_per_bank") != null
                            ? vpaData.get("name_as_per_bank").toString()
                            : null;

                    qrResponse.setBankAccountName(hspAccName);
                    qrResponse.setMerchantName(hspAccName);

                    rmqMap.put("bank_account_name", hspAccName);
                    rmqMap.put("level", Constants.QrConstants.BANK_ACCOUNT_NAME);

                    if (vpaData.get("status").equals("success")) {
                        healthKeyword = checkHspKeyword(hspAccName);
                        validHsp = healthKeyword != null;
                    }
                }
            }

            Integer hspId = this.hspService.insertHspQr(parsedQr.getMerchantName(),
                    null, parsedQr.getVpa(), hspAccName, validHsp);

            qrResponse.setHspId(hspId);
            qrResponse.setStatus(validHsp ? Constants.QrConstants.VALID_HSP : Constants.QrConstants.INVALID_HSP);

            rmqMap.put("keyword", healthKeyword);
            rmqMap.put("is_valid", validHsp);
            rmqMap.put("hsp_id", hspId);

            Producer.addInQueue(QueueConstants.MASTER.exchange, ExecutionsConstants.SAVE_QR_DATA.key,
                    Helper.toJsonString(rmqMap));

            return response(Response.Status.OK, new ApiResponse<>(true, "QR validation success", qrResponse));
        } catch (Exception e) {
            return response(Response.Status.FORBIDDEN,
                    new ApiResponse<>(false, "QR validation failed", e.getMessage()));

        }
    }

    private String checkHspKeyword(String name) {
        String healthKeyword = null;
        for (String key : Constants.HSP_KEYWORDS) {
            if (name.toLowerCase().contains(key.toLowerCase())) {
                healthKeyword = key;
                break;
            }
        }
        return healthKeyword;
    }
}
