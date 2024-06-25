package com.master.db.repository;

import java.util.List;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindList;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import com.master.core.constants.Queries;
import com.master.db.model.PartnershipSchema;

    public interface PartnershipDao {

    @SqlQuery(Queries.GET_PARTNERSHIP_HOSPITAL_DETAILS) 
    @RegisterBeanMapper(PartnershipSchema.class)
    List< PartnershipSchema >getHospitalDetails();

    @SqlUpdate(Queries.UPDATE_PARTNERSHIP_HOSPITAL_DETAILS)  
    Integer savePartnershipDetails(@BindList("hspIds") List<Integer> hspIds, @Bind("category") String category, @Bind("subCategory") String subCategory, @Bind("partner_status") String status);
}

