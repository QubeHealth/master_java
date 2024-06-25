package com.master.utility.sqs;

public class ExecutionsConstants {

        public static final ExecutionInfo APILOG = new ExecutionInfo("LOGGER.APILOG", "Log api request and response");
        public static final ExecutionInfo ZOHO_UPDATE_CONTACT = new ExecutionInfo("UPDATE.ZOHO.CONTACT",
                        "To update specific contact on Zoho");

        public static final ExecutionInfo SAVE_QR_DATA = new ExecutionInfo("QR.DATA",
                        "Save QR data");

        public static class ExecutionInfo {
                public final String key;
                public final String description;

                public ExecutionInfo(String key, String description) {
                        this.key = key;
                        this.description = description;
                }
        }
}