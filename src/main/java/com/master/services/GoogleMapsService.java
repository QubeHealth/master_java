package com.master.services;
import java.util.Map;
import org.jdbi.v3.core.Jdbi;

import com.master.MasterConfiguration;
import com.master.db.repository.HspDao;

public class GoogleMapsService extends BaseService {

    public GoogleMapsService(MasterConfiguration configuration, Jdbi jdbi) {
        super(configuration, jdbi);
    }

    public Long getNearbySearch(Map<String, Object> get) {
        HspDao hspDao = jdbi.onDemand(HspDao.class);
        return hspDao.insertNearbySearch(get);
    }
}
