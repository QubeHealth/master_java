package com.master.services;

import org.jdbi.v3.core.Jdbi;

import com.master.MasterConfiguration;
import com.master.db.model.Miscellaneous;
import com.master.db.repository.MiscDao;

public class MiscService extends BaseService{

    public MiscService(MasterConfiguration configuration, Jdbi jdbi) {
        super(configuration, jdbi);

    }

    public Miscellaneous getMiscData(String key, String columnNames) {
        try {
            MiscDao miscDao = jdbi.onDemand(MiscDao.class);
            return miscDao.getSelfFundedInstructions(key, columnNames);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}