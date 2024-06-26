package com.master.controller;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jdbi.v3.core.Jdbi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.master.MasterConfiguration;

import jakarta.validation.Validator;

public abstract class BaseController {
    protected final MasterConfiguration configuration;
    protected final Validator validator;
    protected final Jdbi jdbi;
    protected final Logger logger;
    protected final ExecutorService executorService;

    protected BaseController(MasterConfiguration configuration, Validator validator, Jdbi jdbi) {
        this.configuration = configuration;
        this.validator = validator;
        this.jdbi = jdbi;
        this.logger = LoggerFactory.getLogger(BaseController.class);
        this.executorService = Executors.newFixedThreadPool(10);

    }

}
