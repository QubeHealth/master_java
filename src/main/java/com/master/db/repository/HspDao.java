package com.master.db.repository;

import java.util.List;


import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.BindList;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

import com.master.core.constants.Queries;
import com.master.db.model.Hsp;

public interface HspDao {

    @SqlQuery(Queries.GET_HSP_DETAILS_BY_IDS)
    @RegisterBeanMapper(Hsp.class)
    List<Hsp> gethspDetailsByIds(@BindList("hspIds") List<Integer> hspIds);

}
