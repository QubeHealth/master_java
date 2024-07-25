package com.master.core.constants;

public abstract class Queries {
        private Queries() {
        }

        public static final String GET_HSP_DETAILS_BY_IDS = "SELECT id as hsp_id, hospital_name as hsp_name from tbl_hsp WHERE id in (<hspIds>)";

        public static final String GET_HSP_BRAND_NAME = "SELECT hsp_brand_name FROM tbl_hsp WHERE id = :hsp_id";

        public static final String INSERT_HSP_BRAND_NAME = "UPDATE tbl_hsp SET hsp_brand_name = :hspBrandName WHERE id = :hspId";

        public static final String INSERT_HSP_BY_MOBILE = "INSERT INTO tbl_hsp (uuid, hospital_name, vpa, hsp_contact, hsp_official_name, status) "
                        + "VALUES (:uuid, :merchant_name, :vpa, :mobile, :bank_account_name, :status);";

        public static final String INSERT_HSP_BANK_DATA = "INSERT INTO tbl_hsp (uuid, hospital_name, bank_account_number, bank_ifsc, hsp_official_name, status) "
                        + "VALUES (:uuid, :bank_account_name, :account_number, :ifsc_code, :bank_account_name, :status);";

        public static final String GET_HSP_BY_VPA = "SELECT id as hsp_id, hospital_name as hsp_name, hsp_official_name, vpa, status from tbl_hsp where vpa = :vpa order by id desc limit 1";

        public static final String GET_HSP_BY_MOBILE = "SELECT id as hsp_id, hospital_name as hsp_name, hsp_official_name, vpa, status from tbl_hsp where hsp_contact = :mobile order by id desc limit 1";

        public static final String GET_HSP_BY_BANK = "SELECT id as hsp_id, hospital_name as hsp_name, hsp_official_name, bank_account_number, bank_ifsc, status "
                        + " from tbl_hsp  where bank_account_number = :accountNumber and bank_ifsc =:ifscCode order by id desc limit 1";

        public static final String GET_SELF_FUNDED_DETAILS = " SELECT json_1 FROM masters.tbl_miscellaneous WHERE `key` = :data";

        public static final String GET_SELF_FUNDED_DOCUMENTS_BY_HSP = "SELECT d.id, d.type, d.name, d.preview_link as link FROM masters.tbl_prefunded_documents d JOIN masters.tbl_prefunded_branches b ON d.pf_branch_id = b.id WHERE b.hsp_id = :hspId";

        public static final String GET_SELF_FUNDED_DOCUMENTS_BY_BRANCH = "SELECT  d.id, d.type, d.name, d.preview_link AS link FROM masters.tbl_prefunded_documents d JOIN masters.tbl_prefunded_branches b ON d.pf_branch_id = b.id WHERE d.pf_branch_id = :branchId";

        public static final String GET_HSP_BY_QR_VPA = "SELECT h.id as hsp_id, h.hospital_name as hsp_name, h.hsp_official_name, h.vpa, h.status, qr.level "
                        + " FROM tbl_hsp h LEFT JOIN tbl_qr_data qr ON qr.hsp_id = h.id WHERE h.vpa = :vpa ORDER BY h.id DESC LIMIT 1";

        public static final String GET_HSP_BY_QR_MCC = "SELECT mcc_code FROM masters.tbl_mcc_code WHERE mcc_code = :mccCode;";

        public static final String INSERT_HSP_QR = "INSERT INTO tbl_hsp (hospital_name, uuid, mcc, vpa, hsp_official_name, status) "
                        + " values (:hospitalName, :uuid, :mcc, :vpa, :bankAccountName, :status)";

        public static final String UPDATE_HSP_LOCATION = "UPDATE tbl_hsp SET address = COALESCE(:location, address), hsp_contact = COALESCE(:hspContact, hsp_contact) where id = :hspId;";

        public static final String INSERT_HSP_QR_DATA = "INSERT INTO tbl_qr_data (user_id,hsp_id,qr_url,vpa,mcc_code,merchant_name,bank_account_name,keyword,is_valid,merchant_city,pincode,level,amount,txn_id) "
                        + " VALUES (:user_id,:hsp_id,:qr_url,:vpa,:mcc_code,:merchant_name,:bank_account_name,:keyword,:is_valid,:merchant_city,:pincode,:level,:amount,:txn_id)";

        public static final String GET_HSP_NAME = "SELECT hospital_name as hsp_name FROM tbl_hsp WHERE id = :hspId";

        public static final String UPDATE_HOSPITAL_NAME = "UPDATE tbl_hsp SET hospital_name = :hospitalName WHERE id = :hspId";

        public static final String UPDATE_HSP_OFFCICIAL_NAME = "UPDATE tbl_hsp SET hsp_official_name = :hospitalOfficialName WHERE id = :hspId";

        public static final String GET_HSP_METADATA = "SELECT * FROM masters.tbl_hsp_metadata WHERE hsp_id = :hspId ;";

        public static final String UPDATE_HSP_METADATA = "INSERT INTO masters.tbl_hsp_metadata (hsp_id, partner_category, partner_sub_category, keyword) "
                        + " VALUES(:hspId, :partnerCategory, :partnerSubCategory, :keyword)";

        public static final String GET_CATEGORY_MISC = " SELECT json_1 FROM masters.tbl_miscellaneous WHERE `key` = :key ";

        public static final String GET_PARTNERSHIP_HOSPITAL_DETAILS = "SELECT tbl_hsp.hospital_name, tbl_hsp.id, tbl_hsp.city_name,tbl_hsp.bank_ifsc, tbl_hsp.bank_account_number, tbl_hsp.address, tbl_hsp.vpa, tbl_hsp.pincode, tbl_hsp.state, tbl_hsp_metadata.status, tbl_hsp_metadata.partner_category, tbl_hsp_metadata.partner_sub_category FROM tbl_hsp INNER JOIN tbl_hsp_metadata ON tbl_hsp.id=tbl_hsp_metadata.hsp_id; ";

        public static final String UPDATE_PARTNERSHIP_HOSPITAL_DETAILS = "UPDATE tbl_hsp_metadata SET partner_category = CASE WHEN hsp_id IN (<hspIds>) THEN COALESCE(:category, partner_category) ELSE partner_category END, partner_sub_category = CASE WHEN hsp_id IN (<hspIds>) THEN COALESCE(:subCategory, partner_sub_category) ELSE partner_sub_category END, status = CASE WHEN hsp_id IN (<hspIds>) THEN COALESCE(:partner_status, status) ELSE status END WHERE hsp_id IN (<hspIds>)";

        public static final String HSP_PARTNERSHIP_SCRIPT = "SELECT hsp.id AS hsp_id, hsp.hospital_name as hsp_name, hsp.hsp_official_name, hsp.vpa, hsp.bank_account_number, hsp.bank_ifsc FROM "
                        + " (SELECT *, ROW_NUMBER() OVER (PARTITION BY vpa ORDER BY id DESC) AS vpa_row_num, "
                        + "  ROW_NUMBER() OVER (PARTITION BY bank_ifsc, bank_account_number ORDER BY id DESC) AS bank_row_num "
                        + "  FROM masters.tbl_hsp "
                        + "  WHERE status = 'VERIFIED') AS hsp "
                        + " WHERE (hsp.vpa_row_num = 1 OR hsp.bank_row_num = 1) "
                        + " ORDER BY TRIM(BOTH ' ' FROM hsp.hospital_name); ";

        public static final String GET_SELF_FUNDED_INSTRUCTIONS = " SELECT <column_names> FROM masters.tbl_miscellaneous WHERE `key` = :data";

        public static final String GET_SELF_FUNDED_BANK_DETAILS = "SELECT hospital_name, city_name, state, pincode, bank_ifsc, bank_account_number, bank_name, vpa, address, hsp_id FROM masters.tbl_prefunded_branches tpb "
                        + "INNER JOIN masters.tbl_hsp th " 
                        + "ON tpb.hsp_id = th.id ;" ;
        public static final String GET_HSP_DROPDOWN_FOR_BILLS = "SELECT distinct hospital_name FROM tbl_hsp where hospital_name is not null and hospital_name != '' and hospital_name LIKE CONCAT('%', :searchInput, '%') ORDER BY hospital_name LIMIT 100 ";

        public static final String UPDATE_HSP_NAME_BILLS ="UPDATE tbl_hsp SET hospital_name = :hospitalName WHERE id = :hospitalId";
                
        public static final String GET_METADATA = "SELECT metadata FROM tbl_emailer_items " 
                        + "WHERE claim_no = :claim_no "
                        + "ORDER BY id DESC;";
}
