package com.master.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.jdbi.v3.core.Jdbi;

import com.google.protobuf.Api;
import com.master.MasterConfiguration;
import com.master.api.ApiResponse;
import com.master.client.LinkageNwService;
import com.master.core.validations.TextSearchSchema;
import com.master.services.GoogleMapsService;
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

@Path("/api/googleMaps")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GoogleMapsController extends BaseController {
    private LinkageNwService linkageNwService;
    private GoogleMapsService googleMapsService;

    public GoogleMapsController(MasterConfiguration configuration, Validator validator, Jdbi jdbi) {
        super(configuration, validator, jdbi);

        this.linkageNwService = new LinkageNwService(configuration);
        this.googleMapsService = new GoogleMapsService(configuration, jdbi);
    }

    private Response response(Response.Status status, Object data) {
        return Response.status(status).entity(data).build();
    }

    @POST
    @Path("/getMapsResponse")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getMapsResponse(@Context HttpServletRequest request, TextSearchSchema body) {

        // Schema validation

        Set<ConstraintViolation<TextSearchSchema>> violations = validator.validate(body);
        if (!violations.isEmpty()) {
            // Construct error message from violations
            String errorMessage = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .reduce("", (acc, msg) -> acc.isEmpty() ? msg : acc + "; " + msg);
            return response(Response.Status.BAD_REQUEST, new ApiResponse<>(false, errorMessage, null));
        }

        // Making the call to the linkage service and saving the data response to the
        // nearRes object
        ApiResponse<Object> nearRes = this.linkageNwService.textSearch((body.getLatitude()),
                body.getLongitude(), "25",body.getKeyword());

        // Logging the response from the service
        System.out.println(Helper.toJsonString(nearRes));

        if (!nearRes.getStatus()) {
            return response(Response.Status.NOT_FOUND, nearRes);
        }

        // Now make the db call and save the response to the database along with the hsp
        // id

        Map<String, Object> sender = new HashMap<>();
        // sender.put("hsp_id", body.getHspId());
        // sender.put("search_response", Helper.toJsonString(nearRes.getData()));
        // sender.put("partner_category", null);
        // sender.put("partner_sub_category", "");
        // sender.put("keyword", "");
        // sender.put("status", "NON_PARTNERED");

        Long g = this.googleMapsService.getTextSearch(sender);
        if (g != null) {
            return response(Response.Status.OK, new ApiResponse<String>(true, "", "Data addition success"));
        } else
            return response(Response.Status.NOT_FOUND, nearRes);
    }

    @POST
    @Path("/adressFinder")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response mapListControl(@Context HttpServletRequest request, TextSearchSchema body) {

        // Here you validate the lat, long and radius
        Set<ConstraintViolation<TextSearchSchema>> violations = validator.validate(body);
        if (!violations.isEmpty()) {
            // Construct error message from violations
            String errorMessage = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .reduce("", (acc, msg) -> acc.isEmpty() ? msg : acc + "; " + msg);
            return response(Response.Status.BAD_REQUEST, new ApiResponse<>(false, errorMessage, null));
        }

        Map<String, Object> data = new HashMap<>();
        data = this.googleMapsService.textSearchResponse(body.getLatitude(), body.getLongitude(), body.getKeyword());

        if (data == null) {
        Map<String, Object> sender = new HashMap<>();
        sender.put("hsp_id", body.getHspId());
        sender.put("search_response", null);
        sender.put("partner_category", null);
        sender.put("partner_sub_category", null);
        sender.put("keyword", "");
        sender.put("status", "NON_PARTNERED");
        this.googleMapsService.getTextSearch(sender);
        return response(Response.Status.ACCEPTED, new ApiResponse<Object>(false, "Records Not Found", null));
        }

        //String can then be analysed for the search results
        Map<String, Object> sender = new HashMap<>();
        sender.put("hsp_id", body.getHspId());
        sender.put("search_response", data);
        sender.put("partner_category", null);
        sender.put("partner_sub_category", null);
        sender.put("keyword", "");
        sender.put("status", "NON_PARTNERED");
        this.googleMapsService.getTextSearch(sender);
        return response(Response.Status.ACCEPTED, new ApiResponse<Object>(true, "Records Found", data));

    }
}