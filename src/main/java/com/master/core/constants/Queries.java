package com.master.core.constants;

public abstract class Queries {
        private Queries() {
        }

        public static final String GET_HSP_DETAILS_BY_IDS = "SELECT id as hsp_id, hospital_name as hsp_name from tbl_hsp WHERE id in (<hspIds>)";

        public static final String INSERT_HSP_BY_MOBILE = "INSERT INTO tbl_hsp (uuid, hospital_name, vpa, hsp_contact, hsp_official_name, status) "
                        + "VALUES (:uuid, :name, :vpa, :mobile, :accountName, :status);";
        
        public static final String GET_METADATA = "SELECT metadata FROM tbl_emailer_items " 
                        + "WHERE claim_no = :claim_no "
                        + "ORDER BY id DESC;";
}
