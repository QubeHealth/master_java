package com.master.utility;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.ext.Provider;

@Provider
public class RequestResponseLoggingFilter implements ContainerRequestFilter, ContainerResponseFilter {

    private static final Logger logger = LoggerFactory.getLogger(RequestResponseLoggingFilter.class);

    @Context
    private HttpServletRequest request;

    @Context
    private HttpServletResponse response;

    @Override
    public void filter(ContainerRequestContext requestContext) {
        try {
            BufferedRequestWrapper requestWrapper = new BufferedRequestWrapper(request);
            logger.info("Request Method: {}", requestWrapper.getMethod());
            logger.info("Request URI: {}", requestWrapper.getRequestURI());
            logger.info("Request Body: {}", requestWrapper.getBody());
        } catch (IOException e) {
            logger.error("Error reading request body", e);
        }
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
            throws IOException {
        // TODO Auto-generated method stub
      //  throw new UnsupportedOperationException("Unimplemented method 'filter'");
    }

    // @Override
    // public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
    //    // BufferedResponseWrapper responseWrapper = new BufferedResponseWrapper(response);
    //     try {
    //      //  responseContext.setEntity(responseWrapper.getBody());
    //         logger.info("Response Status: {}", responseContext.getStatus());
    //         logger.info("Response Body: {}", responseWrapper.getBody());
    //     } catch (Exception e) {
    //         logger.error("Error logging response body", e);
    //     }
    // }
}
