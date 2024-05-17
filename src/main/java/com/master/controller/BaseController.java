package com.master.controller;

import org.jdbi.v3.core.Jdbi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.master.MasterConfiguration;

import jakarta.validation.Validator;

public abstract class BaseController {
    protected final MasterConfiguration configuration;
    protected final Validator validator;
    protected final Jdbi jdbi;
    protected static final Logger logger = LoggerFactory.getLogger(BaseController.class);

    protected BaseController(MasterConfiguration configuration, Validator validator, Jdbi jdbi) {
        this.configuration = configuration;
        this.validator = validator;
        this.jdbi = jdbi;

    }
}
