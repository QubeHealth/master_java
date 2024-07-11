package com.master.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jdbi.v3.core.Jdbi;

import com.master.MasterConfiguration;
import com.master.api.ApiResponse;
import com.master.api.SelfFundedDocuments;
import com.master.core.validations.MiscDataSchema;
import com.master.core.validations.SelfFundedDataSchema;
import com.master.db.model.Miscellaneous;
import com.master.db.model.PrefundedDocument;
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
        @Path("/instructions")
        @Produces(MediaType.APPLICATION_JSON)
        @Consumes(MediaType.APPLICATION_JSON)
        public Response getInstructions(MiscDataSchema reqBody) {
                Miscellaneous response = service.getSelfFundedInstructions(reqBody.getKey());
                Map<String, Object> responseMap = new HashMap<>();
                responseMap.put("decimal_value1", response.getDecimalValue1());
                responseMap.put("decimal_value2", response.getDecimalValue2());
                responseMap.put("string1", response.getString1());
                responseMap.put("string2", response.getString2());
                responseMap.put("json_1", response.getJson1());
                return Response.status(Response.Status.OK)
                                .entity(new ApiResponse<>(true,
                                                "Successfully fetched",
                                                responseMap.get(reqBody.getColumn())))
                                .build();
        }
}
