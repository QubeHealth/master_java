package com.master;

import org.jdbi.v3.core.Jdbi;

import com.master.controller.HspController;
import com.master.controller.MiscController;
import com.master.utility.AuthFilter;
import com.master.utility.JwtAuthenticationFilter;

import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import io.dropwizard.jdbi3.JdbiFactory;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class MasterApplication extends Application<MasterConfiguration> {

    public static void main(final String[] args) throws Exception {
        new MasterApplication().run(args);
    }

    @Override
    public String getName() {
        return "Master";
    }

    @Override
    public void initialize(final Bootstrap<MasterConfiguration> bootstrap) {
        // pass
    }

    @Override
    public void run(final MasterConfiguration configuration,
            final Environment environment) {
        final JdbiFactory factory = new JdbiFactory();
        final Jdbi jdbi = factory.build(environment, configuration.getDataSourceFactory(), "mysql");

        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();

        JwtAuthenticationFilter jwtauthenticationFilter = new JwtAuthenticationFilter(
                configuration.getJwtTokenSignature());

        AuthFilter cAuthFilter = new AuthFilter(configuration.getxApiKey(), configuration.getAuthorizationKey());
        environment.servlets().addFilter("jwt-token", jwtauthenticationFilter)
                .addMappingForUrlPatterns(null, true, "/api/master/*");

        environment.servlets().addFilter("auth-filter", cAuthFilter)
                .addMappingForUrlPatterns(null, true, "/api/master/*");

        HspController hspController = new HspController(configuration, validator, jdbi);
        MiscController miscController = new MiscController(configuration, validator, jdbi);

        environment.jersey().register(hspController);
        environment.jersey().register(miscController);
    }

}
