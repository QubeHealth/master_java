package com.master.services;

import java.util.List;
import java.util.Map;

import org.jdbi.v3.core.Jdbi;

import com.master.MasterConfiguration;
import com.master.db.model.Hsp;
import com.master.db.repository.HspDao;

public class HspService extends BaseService {

    public HspService(MasterConfiguration configuration, Jdbi jdbi) {
        super(configuration, jdbi);
    }

    public List<Hsp> getHspDataListByIds(List<Integer> hspIds) {

        HspDao hspDao = jdbi.onDemand(HspDao.class);

        return hspDao.gethspDetailsByIds(hspIds);
    }

    public Long insertHspByMobile(Map<String, Object> insertData) {

        HspDao hspDao = jdbi.onDemand(HspDao.class);
        return hspDao.insertHspByMobile(insertData);
    }

}
