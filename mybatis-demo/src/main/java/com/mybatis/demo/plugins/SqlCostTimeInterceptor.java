package com.mybatis.demo.plugins;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.sql.Statement;

/**
 * @author yanggy
 * @see Plugin#invoke(Object, Method, Object[])
 */
@Intercepts(
    value = {
        @Signature(type = StatementHandler.class, method = "query", args = {Statement.class, ResultHandler.class}),
        @Signature(type = StatementHandler.class, method = "update", args = {Statement.class}),
        @Signature(type = StatementHandler.class, method = "batch", args = {Statement.class})
    }
)
public class SqlCostTimeInterceptor implements Interceptor {

    private static final Logger log = LoggerFactory.getLogger(SqlCostTimeInterceptor.class);

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 拦截的是 4 大对象之一的 StatementHandler
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        BoundSql boundSql = statementHandler.getBoundSql();
        String sql = boundSql.getSql();
        long start = System.currentTimeMillis();
        Object result = invocation.proceed();
        long end = System.currentTimeMillis();
        log.debug("SQL: {} 执行耗时 {} 毫秒", sql, end - start);
        return result;
    }
}
