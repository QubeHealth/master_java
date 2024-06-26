package com.master.db.repository;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

import com.master.core.constants.Queries;
import com.master.db.model.PrefundedInfo;


    public interface MiscDao {

    @SqlQuery(Queries.GET_SELF_FUNDED_DETAILS)
    @RegisterBeanMapper(PrefundedInfo.class)
    PrefundedInfo getSelfundedDetails(@Bind("data") String data);}

