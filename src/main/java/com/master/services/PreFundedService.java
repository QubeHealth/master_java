package com.master.services;

import org.jdbi.v3.core.Jdbi;

import com.master.MasterConfiguration;
import com.master.db.repository.PreFundedDao;

public class PreFundedService extends BaseService {

    public PreFundedService(MasterConfiguration configuration, Jdbi jdbi) {
        super(configuration, jdbi);
    }

    public String getEmailMetaData (String claimNo){
        
        final PreFundedDao preFundedDao = jdbi.onDemand(PreFundedDao.class);
        String metadata = preFundedDao.getMetaData(claimNo);
        
        return metadata;
    }
}
