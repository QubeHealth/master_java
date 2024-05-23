package com.master.db.repository;

import java.util.List;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindList;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import com.master.core.constants.Queries;
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
}
