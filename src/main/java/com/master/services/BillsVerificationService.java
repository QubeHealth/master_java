package com.master.services;

import java.util.List;

import org.jdbi.v3.core.Jdbi;

import com.master.MasterConfiguration;
import com.master.core.validations.BillSchema;
import com.master.db.model.HospitalDropdownBillsVerification;
import com.master.db.repository.BillsDao;

public class BillsVerificationService extends BaseService {

    public BillsVerificationService(MasterConfiguration configuration, Jdbi jdbi) {
        super(configuration, jdbi);
    }

    public List<HospitalDropdownBillsVerification> getHospitalList(String searchInput) {
        BillsDao billsDao = jdbi.onDemand(BillsDao.class);
        return billsDao.getHspListForBillsVerification(searchInput);
    }

    public Integer saveHospitalName(BillSchema.ValidateInsertSchema schema) {
        BillsDao billsDao= jdbi.onDemand(BillsDao.class);
        return billsDao.updateHospitalName(schema.getHospitalName(), schema.getHospitalId());
    }
}
