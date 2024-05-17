package com.master.controller;

import org.jdbi.v3.core.Jdbi;

import com.master.MasterConfiguration;

import jakarta.validation.Validator;

public class PaymentController extends BaseController {

    public PaymentController(MasterConfiguration configuration, Validator validator, Jdbi jdbi) {
        super(configuration, validator, jdbi);
    }

}
