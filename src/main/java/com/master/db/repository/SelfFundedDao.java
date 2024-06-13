package com.master.db.repository;

import java.util.List;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

import com.master.core.constants.Queries;
import com.master.db.model.PrefundedDocument;

public interface SelfFundedDao {

    @SqlQuery(Queries.GET_SELF_FUNDED_DOCUMENTS_BY_HSP)
    @RegisterBeanMapper(PrefundedDocument.class)
    List< PrefundedDocument> getSelfFundedDataByHsp(@Bind("hspId") Long hspId);

    @SqlQuery(Queries.GET_SELF_FUNDED_DOCUMENTS_BY_BRANCH)
    @RegisterBeanMapper(PrefundedDocument.class)
    List<PrefundedDocument> getSelfFundedDataByBranch(@Bind("branchId") Long branch);
}
