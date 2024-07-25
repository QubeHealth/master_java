package com.master.db.repository;

import java.util.List;
import java.util.Map;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindMap;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.transaction.Transaction;

import com.master.core.constants.Queries;
import com.master.db.model.PrefundedBankDetails;
import com.master.db.model.EmailerTemplates;
import com.master.db.model.PrefundedDocument;

public interface SelfFundedDao {

    @SqlQuery(Queries.GET_SELF_FUNDED_DOCUMENTS_BY_HSP)
    @RegisterBeanMapper(PrefundedDocument.class)
    List<PrefundedDocument> getSelfFundedDataByHsp(@Bind("hspId") Long hspId);

    @SqlQuery(Queries.GET_SELF_FUNDED_DOCUMENTS_BY_BRANCH)
    @RegisterBeanMapper(PrefundedDocument.class)
    List<PrefundedDocument> getSelfFundedDataByBranch(@Bind("branchId") Long branch);

    @SqlQuery(Queries.GET_SELF_FUNDED_BANK_DETAILS)
    @RegisterBeanMapper(PrefundedBankDetails.class)
    List<PrefundedBankDetails> getPrefundedBankDetails();

    @Transaction
    @SqlUpdate(Queries.INSERT_EMAILER_DATA)
    @GetGeneratedKeys("id")
    Long setEmailerData(@BindMap Map<String, Object> insertData);

    @Transaction
    @SqlUpdate(Queries.INSERT_EMAILER_ITEMS_BY_TPA_DESK_ID)
    @GetGeneratedKeys("id")
    Long setEmailItems(@BindMap Map<String, Object> insertData);

    @SqlQuery(Queries.GET_EMAIL_TEMPLATE)
    @RegisterBeanMapper(EmailerTemplates.class)
    String getEmailTemplate(@Bind("types") String types);

}
