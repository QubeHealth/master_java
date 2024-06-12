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

                        public static final String GET_SELFUNDED_DETAILS = " SELECT json_1 as data FROM masters.tbl_miscellaneous WHERE `key` = :data";

}
