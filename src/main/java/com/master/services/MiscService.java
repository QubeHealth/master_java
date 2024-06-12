package com.master.services;

import java.util.Collections;
import java.util.Map;

import org.jdbi.v3.core.Jdbi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.master.MasterConfiguration;
import com.master.db.model.SelfFundedData;
import com.master.db.repository.MiscDao;

public class MiscService extends BaseService {

    public MiscService(MasterConfiguration configuration, Jdbi jdbi) {
        super(configuration, jdbi);

    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getSelfFundedConstants(String name) {
        try {
            MiscDao miscDao = jdbi.onDemand(MiscDao.class);
            SelfFundedData data = miscDao.getSelfundedDetails(name);

            if (data != null && data.getData() != null) {
                ObjectMapper mapper = new ObjectMapper();

                return mapper.readValue(data.getData(), Map.class);
            } else {
                return Collections.emptyMap();
            }
        } catch (Exception e) {
            e.printStackTrace(); 
            return Collections.emptyMap();
        }
    }
}
