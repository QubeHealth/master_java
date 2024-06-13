package com.master.api;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.master.db.model.PrefundedDocument;

public class SelfFundedDocuments {
        public SelfFundedDocuments() {
          
        }

        @JsonProperty("document_info")
        private Map<String, Object> documentInfo;

        @JsonProperty("documents")
        private List< PrefundedDocument>  documents;

        public Map<String, Object> getDocumentInfo() {
                return documentInfo;
        }

        public void setDocumentInfo(Map<String, Object> documentInfo) {
                this.documentInfo = documentInfo;
        }

        public Object getDocuments() {
                return documents;
        }

        public void setDocuments(List< PrefundedDocument>  documents) {
                this.documents = documents;
        }

      

    
}
