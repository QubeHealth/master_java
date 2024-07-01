package com.master.db.repository;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

import com.master.core.constants.Queries;
import com.master.db.model.Miscellaneous;
import com.master.db.model.PartnerCategory;


    public interface MiscDao {

    @SqlQuery(Queries.GET_SELF_FUNDED_DETAILS)
    @RegisterBeanMapper(Miscellaneous.class)
    Miscellaneous getSelfundedDetails(@Bind("data") String data);

    @SqlQuery(Queries.GET_CATEGORY_MISC)
    @RegisterBeanMapper(PartnerCategory.class)
    PartnerCategory getCategoryMisc(@Bind("key") String key);


}

