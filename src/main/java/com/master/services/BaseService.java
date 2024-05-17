package com.master.services;

import com.master.MasterConfiguration;

import org.jdbi.v3.core.Jdbi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseService {

    protected final MasterConfiguration configuration;

    protected final Jdbi jdbi;

    protected static final Logger logger = LoggerFactory.getLogger(BaseService.class);

    protected BaseService(MasterConfiguration configuration, Jdbi jdbi) {
        this.configuration = configuration;
        this.jdbi = jdbi;
    }

}
