package com.master;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.dropwizard.core.Configuration;
import io.dropwizard.db.DataSourceFactory;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class MasterConfiguration extends Configuration {
    @NotEmpty
    private String jwtTokenSignature;
    @NotEmpty
    private String linkageUrl;
    @NotEmpty
    private String authorizationKey;
    @NotEmpty
    private String xApiKey;

    @NotEmpty
    private String linkageJavaUrl;

    public String getxApiKey() {
        return xApiKey;
    }

    public void setxApiKey(String xApiKey) {
        this.xApiKey = xApiKey;
    }

    public String getAuthorizationKey() {
        return authorizationKey;
    }

    public void setAuthorizationKey(String authorizationKey) {
        this.authorizationKey = authorizationKey;
    }

    public String getLinkageUrl() {
        return linkageUrl;
    }

    public void setLinkageUrl(String linkageUrl) {
        this.linkageUrl = linkageUrl;
    }

    public String getJwtTokenSignature() {
        return jwtTokenSignature;
    }

    public void setJwtTokenSignature(String jwtTokenSignature) {
        this.jwtTokenSignature = jwtTokenSignature;
    }

     @Valid
    @NotNull
    @JsonProperty("database")
    private DataSourceFactory database = new DataSourceFactory();

    public DataSourceFactory getDataSourceFactory() {
        return database;
    }

    public void setDatabase(DataSourceFactory database) {
        this.database = database;
    }

    public String getLinkageJavaUrl() {
        return linkageJavaUrl;
    }

    public void setLinkageJavaUrl(String linkageJavaUrl) {
        this.linkageJavaUrl = linkageJavaUrl;
    }
}
