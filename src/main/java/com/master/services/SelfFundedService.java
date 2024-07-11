package com.master.services;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.jdbi.v3.core.Jdbi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.master.MasterConfiguration;
import com.master.core.constants.Constants;
import com.master.db.model.Miscellaneous;
import com.master.db.model.PrefundedDocument;
import com.master.db.repository.MiscDao;
import com.master.db.repository.SelfFundedDao;

public class SelfFundedService extends BaseService {

    public SelfFundedService(MasterConfiguration configuration, Jdbi jdbi) {
        super(configuration, jdbi);

    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getSelfFundedConstants(String name) {
        try {
            MiscDao miscDao = jdbi.onDemand(MiscDao.class);
            Miscellaneous data = miscDao.getSelfundedDetails(name);

            if (data != null && data.getJson1() != null) {
                ObjectMapper mapper = new ObjectMapper();

                return mapper.readValue(data.getJson1(), Map.class);
            } else {
                return Collections.emptyMap();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyMap();
        }
    }

    public List<PrefundedDocument> getSelfFundedDocumentsForScanned(Long value) {
        try {
            SelfFundedDao selfFundedDao = jdbi.onDemand(SelfFundedDao.class);

            List<PrefundedDocument> data = selfFundedDao.getSelfFundedDataByHsp(value);
            if (data == null || data.isEmpty()) {
                return selfFundedDao.getSelfFundedDataByBranch(Constants.OTHER_HOSPITALS);
            }
            return data;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<PrefundedDocument> getSelfFundedDocumentsByBranch(Long value) {
        try {
            SelfFundedDao selfFundedDao = jdbi.onDemand(SelfFundedDao.class);
            return selfFundedDao.getSelfFundedDataByBranch(value);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    
}
