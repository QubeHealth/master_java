package com.master.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jdbi.v3.core.Jdbi;

import com.master.MasterConfiguration;
import com.master.api.ApiResponse;
import com.master.api.SelfFundedDocuments;
import com.master.core.validations.SelfFundedDataSchema;
import com.master.db.model.EmailerItems;
import com.master.db.model.PrefundedDocument;
import com.master.db.model.PrefundedEmailers;
import com.master.db.repository.SelfFundedDao;
import com.master.services.SelfFundedService;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/prefunded")
public class SelfFundedController extends BaseController {
        private SelfFundedService service;

        public SelfFundedController(MasterConfiguration configuration, Validator validator, Jdbi jdbi) {
                super(configuration, validator, jdbi);
                this.service = new SelfFundedService(configuration, jdbi);

        }

        @POST
        @Path("/options")
        @Produces(MediaType.APPLICATION_JSON)
        @Consumes(MediaType.APPLICATION_JSON)
        public Response getSelfFundedDetails(SelfFundedDataSchema.Constants reqBody) {

                Map<String, Object> data = service.getSelfFundedConstants("self-funded-options");
                return Response.status(data != null ? Response.Status.OK : Response.Status.INTERNAL_SERVER_ERROR)
                                .entity(new ApiResponse<>(data != null,
                                                data != null ? "Successfully fetched" : "Failed to fetch",
                                                data))
                                .build();
        }

        @POST
        @Path("/documents")
        @Produces(MediaType.APPLICATION_JSON)
        @Consumes(MediaType.APPLICATION_JSON)
        public Response getDocuments(SelfFundedDataSchema.Documents reqBody) {
                Set<ConstraintViolation<SelfFundedDataSchema.Documents>> violations = validator.validate(reqBody);
                if (!violations.isEmpty()) {
                        // Construct error message from violations
                        String errorMessage = violations.stream()
                                        .map(ConstraintViolation::getMessage)
                                        .reduce("", (acc, msg) -> acc.isEmpty() ? msg : acc + "; " + msg);
                        return Response.status(Response.Status.BAD_REQUEST)
                                        .entity(new ApiResponse<>(false, errorMessage, null))
                                        .build();
                }
                SelfFundedDocuments response = new SelfFundedDocuments();
                if (reqBody.isShowInfo()) {
                        Map<String, Object> data = service.getSelfFundedConstants("self-funded-docs-info");
                        response.setDocumentInfo(data);
                }

                List<PrefundedDocument> documents = null;

                documents = service.getSelfFundedDocumentsForScanned(reqBody.getHspId());

                response.setDocuments(documents);

                return Response.status(Response.Status.OK)
                                .entity(new ApiResponse<>(true,
                                                "Successfully fetched",
                                                response))
                                .build();
        }

        @POST
        @Path("/prefundedEmailer")
        @Produces(MediaType.APPLICATION_JSON)
        @Consumes(MediaType.APPLICATION_JSON)
        public Response emailInsert(PrefundedEmailers body){
                Set<ConstraintViolation<PrefundedEmailers>> violations = validator.validate(body);
                if (!violations.isEmpty()) {
                        // Construct error message from violations
                        String errorMessage = violations.stream()
                                        .map(ConstraintViolation::getMessage)
                                        .reduce("", (acc, msg) -> acc.isEmpty() ? msg : acc + "; " + msg);
                        return Response.status(Response.Status.BAD_REQUEST)
                                        .entity(new ApiResponse<>(false, errorMessage, null))
                                        .build();
                }

                SelfFundedDao selfFundedDao = jdbi.onDemand(SelfFundedDao.class);

                Map<String, Object> bodyMap = new HashMap<>();
                bodyMap.put("type",body.getType());
                bodyMap.put("subject",body.getSubject());
                bodyMap.put("is_active",true);
                bodyMap.put("claim_no",body.getClaimNo());
                bodyMap.put("pf_request_id", body.getPfRequestId()==null?null:body.getPfRequestId());
                Integer getEmailInsert = selfFundedDao.setEmailerData(bodyMap);

                return Response.status(Response.Status.OK)
                                .entity(new ApiResponse<>(true,
                                                "Successfully updated",
                                                getEmailInsert))
                                .build();
        }

        @POST
        @Path("/emailItems")
        @Produces(MediaType.APPLICATION_JSON)
        @Consumes(MediaType.APPLICATION_JSON)
        public Response emailInsert(EmailerItems body){
                Set<ConstraintViolation<EmailerItems>> violations = validator.validate(body);
                if (!violations.isEmpty()) {
                        // Construct error message from violations
                        String errorMessage = violations.stream()
                                        .map(ConstraintViolation::getMessage)
                                        .reduce("", (acc, msg) -> acc.isEmpty() ? msg : acc + "; " + msg);
                        return Response.status(Response.Status.BAD_REQUEST)
                                        .entity(new ApiResponse<>(false, errorMessage, null))
                                        .build();
                }

                SelfFundedDao selfFundedDao = jdbi.onDemand(SelfFundedDao.class);

                Map<String, Object> bodyMap = new HashMap<>();
                bodyMap.put("tpa_desk_id",body.getTpaDeskId());
                bodyMap.put("claim_no",body.getClaimNo()==null?null:body.getClaimNo());
                bodyMap.put("policy",body.getPolicyNo()==null?null:body.getPolicyNo());
                bodyMap.put("initial_amt_req",body.getInitialAmtReq()==null?null:body.getInitialAmtReq());
                bodyMap.put("initial_amt_approved",body.getInitialAmtApproved()==null?null:body.getInitialAmtApproved());
                bodyMap.put("final_adj_amt_req",body.getFinalAdjAmtReq()==null?null:body.getFinalAdjAmtReq());
                bodyMap.put("final_adj_amt_approved",body.getFinalAdjAmtApproved()==null?null:body.getFinalAdjAmtApproved());
                bodyMap.put("patient_name",body.getPatientName()==null?null:body.getPatientName());
                bodyMap.put("metadata", body.getMetadata());

                Long getEmailItems = selfFundedDao.setEmailItems(bodyMap);

                if(getEmailItems == null){
                        return Response.status(Response.Status.OK)
                                .entity(new ApiResponse<>(true,
                                                "Data insertion failed",
                                                getEmailItems))
                                .build();
                }
                return Response.status(Response.Status.OK)
                                .entity(new ApiResponse<>(true,
                                                "Successfully updated",
                                                getEmailItems))
                                .build();
        }

}
