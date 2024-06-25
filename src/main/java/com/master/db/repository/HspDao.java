package com.master.db.repository;

import java.util.List;
import java.util.Map;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.customizer.BindList;
import org.jdbi.v3.sqlobject.customizer.BindMap;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import com.master.core.constants.Queries;
import com.master.core.validations.PaymentSchemas.BankSchema;
import com.master.db.model.Hsp;
import com.master.db.model.GetHspBrandName;

public interface HspDao {

    @SqlQuery(Queries.GET_HSP_DETAILS_BY_IDS)
    @RegisterBeanMapper(Hsp.class)
    List<Hsp> gethspDetailsByIds(@BindList("hspIds") List<Integer> hspIds);

    @SqlQuery(Queries.GET_HSP_BRAND_NAME)
    @RegisterBeanMapper(GetHspBrandName.class)
    GetHspBrandName getHspBrandName(@Bind("hsp_id") String hspId);

    @SqlUpdate(Queries.INSERT_HSP_BRAND_NAME)
    void insertHspBrandName(@Bind("hspId") String hspId, @Bind("hspBrandName") String hspBrandName);

    @SqlUpdate(Queries.INSERT_HSP_BY_MOBILE)
    @GetGeneratedKeys("id")
    Long insertHspByMobile(@BindMap Map<String, Object> insertData);

    @SqlUpdate(Queries.INSERT_HSP_BANK_DATA)
    @GetGeneratedKeys("id")
    Long insertHspBankData(@BindMap Map<String, Object> insertData);

    @SqlQuery(Queries.GET_HSP_BY_VPA)
    @RegisterBeanMapper(Hsp.class)
    Hsp getHspbyVpa(@Bind("vpa") String vpa);

    @SqlQuery(Queries.GET_HSP_BY_MOBILE)
    @RegisterBeanMapper(Hsp.class)
    Hsp getHspbyMobile(@Bind("mobile") String mobile);

    @SqlQuery(Queries.GET_HSP_BY_BANK)
    @RegisterBeanMapper(Hsp.class)
    Hsp getHspbyBankDetails(@BindBean BankSchema body);

    @SqlQuery(Queries.GET_HSP_BY_QR_VPA)
    @RegisterBeanMapper(Hsp.class)
    Hsp getHspbyQRVpa(@Bind("vpa") String vpa);

    @SqlQuery(Queries.GET_HSP_BY_QR_MCC)
    @RegisterBeanMapper(Hsp.class)
    Hsp getHspbyQRMcc(@Bind("mccCode") String mccCode);

    @SqlUpdate(Queries.INSERT_HSP_QR)
    @GetGeneratedKeys("id")
    Integer insertHspQr(@BindMap Map<String, Object> insertData);
}
