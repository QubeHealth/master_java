package com.master.db.repository;

import java.util.List;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import com.master.core.constants.Queries;
import com.master.db.model.HospitalDropdownBillsVerification;

public interface BillsDao {

    @SqlQuery(Queries.GET_HSP_DROPDOWN_FOR_BILLS)
    @RegisterBeanMapper(HospitalDropdownBillsVerification.class)
    List<HospitalDropdownBillsVerification> getHspListForBillsVerification(@Bind("searchInput") String searchInput);

    @SqlUpdate(Queries.UPDATE_HSP_NAME_BILLS)
    Integer updateHospitalName(@Bind("hospitalName") String hospitalName, @Bind("hospitalId") String hospitalId);

}
