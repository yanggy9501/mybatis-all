package com.mybatis.demo.tools;

import com.mybatis.demo.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.builder.xml.XMLConfigBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.StringTypeHandler;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author yanggy
 */
@Slf4j
public class TestMappedStatement {

    @Test
    public void testMappedStatement() throws IOException {
        String resource = "mybatis-config.xml";
        InputStream stream = Resources.getResourceAsStream(resource);
        XMLConfigBuilder xmlConfigBuilder = new XMLConfigBuilder(stream);
        Configuration configuration = xmlConfigBuilder.parse();

        String sql = "select * from user";
        // 静态sql
        StaticSqlSource staticSqlSource = new StaticSqlSource(configuration, sql);

        MappedStatement.Builder builder = new MappedStatement.Builder(configuration, "selectUser", staticSqlSource, SqlCommandType.SELECT);

        // 标签中的 resultMap: 封装结果集
        ArrayList<ResultMapping> resultMappings = new ArrayList<>();
        ResultMapping mapping1 = new ResultMapping.Builder(configuration, "id", "id", Long.class).build();
        ResultMapping mapping2 = new ResultMapping.Builder(configuration, "username", "username", new StringTypeHandler()).build();
        resultMappings.add(mapping1);
        resultMappings.add(mapping2);
        ResultMap.Builder rsBuilder = new ResultMap.Builder(configuration, "userMap", User.class, resultMappings);
        ResultMap resultMap = rsBuilder.build();

        MappedStatement mappedStatement = builder.resultMaps(Arrays.asList(resultMap)).build();

        log.info("mappedStatement -> {}", mappedStatement);

        // 获取sql，需要传递 parameter，parameterMap 即需要传递的参数
        BoundSql boundSql = mappedStatement.getBoundSql(null);
        String sql1 = boundSql.getSql();
        log.info("sql: -> {}", sql1);
    }
}

