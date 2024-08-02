package com.master;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.jdbi.v3.core.Jdbi;

import com.master.controller.BillsVerificationController;
import com.master.controller.FraudController;
import com.master.controller.HspController;
import com.master.controller.MiscellaneousController;
import com.master.controller.PartnershipController;
import com.master.controller.SelfFundedController;
import com.master.utility.AuthFilter;
import com.master.utility.CustomSqlLogger;
import com.master.utility.JwtAuthenticationFilter;
import com.master.utility.sqs.QueueConnection;

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
        jdbi.setSqlLogger(new CustomSqlLogger());

        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();

        JwtAuthenticationFilter jwtauthenticationFilter = new JwtAuthenticationFilter(
                configuration.getJwtTokenSignature());
        environment.servlets().addFilter("jwt-token", jwtauthenticationFilter)
                .addMappingForUrlPatterns(null, true, "/api/master/*", "/api/hsp/saveHspBank");
       
        AuthFilter cAuthFilter = new AuthFilter(configuration.getxApiKey(), configuration.getAuthorizationKey());

        environment.servlets().addFilter("auth-filter", cAuthFilter)
                .addMappingForUrlPatterns(null, true, "/api/master/*", "/api/hsp/*");

        HspController hspController = new HspController(configuration, validator, jdbi);
        SelfFundedController selfFundedController = new SelfFundedController(configuration, validator, jdbi);
        QueueConnection queueConnection = new QueueConnection(configuration);
        PartnershipController partnershipController= new PartnershipController(configuration, validator, jdbi);
        MiscellaneousController miscellaneousController = new MiscellaneousController(configuration, validator, jdbi);
        FraudController fraudController = new FraudController(configuration, validator, jdbi);
        
        BillsVerificationController billsVerificationController = new BillsVerificationController(configuration,
                validator, jdbi);
        //environment.jersey().register(new RequestResponseLoggingFilter());
        environment.jersey().register(hspController);
        environment.jersey().register(selfFundedController);
        environment.jersey().register(queueConnection);
        environment.jersey().register(MultiPartFeature.class);

        environment.jersey().register(partnershipController);
        environment.jersey().register(miscellaneousController);
        environment.jersey().register(billsVerificationController);
        environment.jersey().register(fraudController);
    }

}
