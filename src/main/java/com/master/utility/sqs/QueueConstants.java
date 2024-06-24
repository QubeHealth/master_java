package com.master.utility.sqs;

public class QueueConstants {
    public static final QueueInfo LOGS = new QueueInfo("logger_queue", "common_exchange");
    public static final QueueInfo MASTER = new QueueInfo("master_queue", "common_exchange");

    public static class QueueInfo {
        public final String queue;
        public final String exchange;

        public QueueInfo(String queue, String exchange) {
            this.queue = queue;
            this.exchange = exchange;
        }
    }
}