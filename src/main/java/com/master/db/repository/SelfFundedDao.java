package com.master.db.repository;

import java.util.List;
import java.util.Map;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindMap;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import com.master.core.constants.Queries;
import com.master.db.model.PrefundedDocument;
import com.master.db.model.PrefundedEmailers;

public interface SelfFundedDao {

    @SqlQuery(Queries.GET_SELF_FUNDED_DOCUMENTS_BY_HSP)
    @RegisterBeanMapper(PrefundedDocument.class)
    List< PrefundedDocument> getSelfFundedDataByHsp(@Bind("hspId") Long hspId);

    @SqlQuery(Queries.GET_SELF_FUNDED_DOCUMENTS_BY_BRANCH)
    @RegisterBeanMapper(PrefundedDocument.class)
    List<PrefundedDocument> getSelfFundedDataByBranch(@Bind("branchId") Long branch);

    @SqlUpdate(Queries.INSERT_EMAILER_DATA)
    @GetGeneratedKeys("id")
    Long setEmailerData (@BindMap Map<String, Object> insertData);

    @SqlUpdate(Queries.INSERT_EMAILER_ITEMS_BY_TPA_DESK_ID)
    @GetGeneratedKeys("id")
    Long setEmailItems (@BindMap Map<String, Object> insertData);

}
