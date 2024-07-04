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

        public static final String GET_SELF_FUNDED_DETAILS = " SELECT json_1 as data FROM masters.tbl_miscellaneous WHERE `key` = :data";

        public static final String GET_SELF_FUNDED_DOCUMENTS_BY_HSP = "SELECT d.id, d.type, d.name, d.preview_link as link FROM masters.tbl_prefunded_documents d JOIN masters.tbl_prefunded_branches b ON d.pf_branch_id = b.id WHERE b.hsp_id = :hspId";

        public static final String GET_SELF_FUNDED_DOCUMENTS_BY_BRANCH = "SELECT  d.id, d.type, d.name, d.preview_link AS link FROM masters.tbl_prefunded_documents d JOIN masters.tbl_prefunded_branches b ON d.pf_branch_id = b.id WHERE d.pf_branch_id = :branchId";

        public static final String INSERT_EMAILER_DATA = "INSERT INTO tbl_prefunded_emailers (type,subject,is_active,claim_no,pf_request_id) "
                        + " VALUES(:type, :subject, :is_active, :claim_no, :pf_request_id)";

        public static final String INSERT_EMAILER_ITEMS_BY_TPA_DESK_ID = "INSERT INTO tbl_emailer_items (tpa_desk_id, claim_no, policy_no, initial_amt_req, initial_amt_approved, final_adj_amt_req, final_adj_amt_approved, patient_name, metadata) "
                        + " VALUES(:tpa_desk_id, :claim_no, :policy_no, :initial_amt_req, :initial_amt_approved, :final_adj_amt_req, :final_adj_amt_approved, :patient_name, :metadata)";
                        
        public static final String INSERT_ADJUDICATION_DATA = "INSERT INTO tbl_adjudication_data (user_id, offer_id, hsp_id, document_id, associated_user_id, status, created_by, requested_by, created_at, updated_at) "
                        + "VALUES (:userId, :offerId, :hspId, :documentId, :associatedUserId, :status, :createdBy, :requestedBy, NOW(), NOW())";

}
