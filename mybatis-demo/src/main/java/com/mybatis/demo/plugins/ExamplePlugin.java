package com.mybatis.demo.plugins;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Properties;


@Intercepts({@Signature( type= Executor.class,  method = "query", args ={
        MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class
})})
//@Intercepts({@Signature( type= StatementHandler.class,  method = "update", args ={Statement.class})})
    public class ExamplePlugin implements Interceptor {

    //  分页   读写分离    Select  增删改

        public Object intercept(Invocation invocation) throws Throwable {
            System.out.println("代理");
        Object[] args = invocation.getArgs();
        MappedStatement ms= (MappedStatement) args[0];
        // 执行下一个拦截器、直到尽头
        return invocation.proceed();
    }

}