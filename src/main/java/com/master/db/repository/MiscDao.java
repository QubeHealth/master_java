package com.master.db.repository;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

import com.master.core.constants.Queries;
import com.master.db.model.SelfFundedData;


    public interface MiscDao {

    @SqlQuery(Queries.GET_SELFUNDED_DETAILS)
    @RegisterBeanMapper(SelfFundedData.class)
    SelfFundedData getSelfundedDetails(@Bind("data") String data);


}

