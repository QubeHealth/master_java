package com.master.db.repository;

import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

import com.master.core.constants.Queries;
public interface FamilySchemaDao {

    @SqlQuery(Queries.GET_ADD_FAMILY_SCHEMA_JSON)
    String getAddFamilySchema(@Bind("key") String key);
} 
