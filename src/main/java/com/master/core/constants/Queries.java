package com.master.core.constants;

public abstract class Queries {
        private Queries() {
        }

        public static final String GET_USER_DETAILS_BY_MOBILE = "SELECT  id, mobile, first_name, last_name, whatsapp_number FROM tbl_users WHERE mobile = :mobile";

        public static final String GET_HSP_DETAILS_BY_IDS = "SELECT id as hsp_id, hospital_name as hsp_name from tbl_hsp WHERE id in (<hspIds>)";

        public static final String GET_HSP_BRAND_NAME = "SELECT hsp_brand_name FROM tbl_hsp WHERE id = :hsp_id";

        public static final String INSERT_HSP_BRAND_NAME = "UPDATE tbl_hsp SET hsp_brand_name = :hspBrandName WHERE id = :hspId";

}
