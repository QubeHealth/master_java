package com.master.services;

import org.jdbi.v3.core.Jdbi;

import com.master.MasterConfiguration;
import com.master.db.repository.FamilySchemaDao;

public class FamilySchemaService extends BaseService {

    public FamilySchemaService(MasterConfiguration configuration, Jdbi jdbi) {
        super(configuration, jdbi);
        
    }
 
    public String getAddFamilytSchema( String tempKey){
        
        final FamilySchemaDao familySchemaDao = jdbi.onDemand(FamilySchemaDao.class);
        return familySchemaDao.getAddFamilySchema(tempKey);

    }
}