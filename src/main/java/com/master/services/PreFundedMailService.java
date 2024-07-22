package com.master.services;

import org.jdbi.v3.core.Jdbi;

import com.master.MasterConfiguration;
import com.master.db.repository.PreFundedMailDao;

public class PreFundedMailService extends BaseService {

    public PreFundedMailService(MasterConfiguration configuration, Jdbi jdbi) {
        super(configuration, jdbi);
    }

    public String getEmailMetaData (String claimNo){
        
        final PreFundedMailDao preFundedMailDao = jdbi.onDemand(PreFundedMailDao.class);
        String metadata = preFundedMailDao.getMetaData(claimNo);
        
        return metadata;
    }
}
