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

    public static class RabbitMqConfig {
        @NotEmpty
        private String userName;

        @NotEmpty
        private String password;

        @NotEmpty
        private String virtualHost;

        @NotEmpty
        private String hostName;

        private int portNumber;

        private String env;

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getVirtualHost() {
            return virtualHost;
        }

        public void setVirtualHost(String virtualHost) {
            this.virtualHost = virtualHost;
        }

        public String getHostName() {
            return hostName;
        }

        public void setHostName(String hostName) {
            this.hostName = hostName;
        }

        public int getPortNumber() {
            return portNumber;
        }

        public void setPortNumber(int portNumber) {
            this.portNumber = portNumber;
        }

        public String getEnv() {
            return env;
        }

        public void setEnv(String env) {
            this.env = env;
        }
    }

    @Valid
    @NotNull
    @JsonProperty("rabbitmq")
    private RabbitMqConfig rabbitMqConfig = new RabbitMqConfig();

    public RabbitMqConfig getRabbitMqConfig() {
        return rabbitMqConfig;
    }

    public void setRabbitMqConfig(RabbitMqConfig rabbitMqConfig) {
        this.rabbitMqConfig = rabbitMqConfig;
    }

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