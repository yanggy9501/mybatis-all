package com.mybatis.demo.plugins;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Invocation;


//@Intercepts({@Signature( type= Executor.class,  method = "query", args = {
//        MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class
//})})
// @Intercepts({@Signature( type= StatementHandler.class,  method = "update", args ={Statement.class})})
public class ExamplePlugin implements Interceptor {

    public Object intercept(Invocation invocation) throws Throwable {
        // 执行下一个拦截器、直到尽头
        return invocation.proceed();
    }

}