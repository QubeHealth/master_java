package com.master.core.constants;

public abstract class Queries {
        private Queries() {
        }

        public static final String GET_HSP_DETAILS_BY_IDS = "SELECT id as hsp_id, hospital_name as hsp_name from tbl_hsp WHERE id in (<hspIds>)";

        public static final String GET_HSP_BRAND_NAME = "SELECT hsp_brand_name FROM tbl_hsp WHERE id = :hsp_id";

        public static final String INSERT_HSP_BRAND_NAME = "UPDATE tbl_hsp SET hsp_brand_name = :hspBrandName WHERE id = :hspId";

        public static final String INSERT_HSP_BY_MOBILE = "INSERT INTO tbl_hsp (uuid, hospital_name, vpa, hsp_contact, hsp_official_name, status) "
                        + "VALUES (:uuid, :merchant_name, :vpa, :mobile, :bank_account_name, :status);";


}
