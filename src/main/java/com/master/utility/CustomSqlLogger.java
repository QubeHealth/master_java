package com.master.utility;

import java.sql.SQLException;

import org.jdbi.v3.core.statement.Slf4JSqlLogger;
import org.jdbi.v3.core.statement.StatementContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomSqlLogger extends Slf4JSqlLogger {
    private static final Logger log = LoggerFactory.getLogger(CustomSqlLogger.class);

    @Override
    public void logAfterExecution(StatementContext context) {
        String query = context.getRenderedSql();
        String parameters = context.getBinding().toString();

        log.debug("Executing SQL: {}\nParameters: {}", query, parameters);
        super.logAfterExecution(context);
    }
   
   @Override
    public void logException(StatementContext context,SQLException ex) {
        log.error("QUERY_FAILED:"+context.getRenderedSql(), ex);
        super.logException(context, ex);
    }
}
