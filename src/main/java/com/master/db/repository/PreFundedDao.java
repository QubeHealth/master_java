package com.master.db.repository;

import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import com.master.core.constants.Queries;

public interface PreFundedDao {
    
    @SqlQuery(Queries.GET_METADATA)
    String getMetaData(@Bind("claim_no") String claimNo);
}
