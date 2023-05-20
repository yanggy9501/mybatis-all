package com.mybatis.demo.tools;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.builder.xml.XMLConfigBuilder;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.logging.slf4j.Slf4jImpl;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author yanggy
 */
@Slf4j
public class TestConfiguration {

    /**
     * mybatis 配置文件的扫描结果如下
     */
    @Test
    public void testConfiguration() throws IOException {
        Configuration configuration = new Configuration();

        // properties 文件属性配置
        Properties properties = new Properties();
        properties.load(Resources.getResourceAsReader("db.properties"));
        configuration.setVariables(properties);

        // settings 配置
        // 设置日志：<setting name="logImpl" value="SLF4J"/> 可以去看一下别名对应的类
        configuration.setLogImpl(Slf4jImpl.class);
        // 驼峰式命名
        configuration.setMapUnderscoreToCamelCase(true);

        // 别名注册器: 单一指定，包名的批量指定，
        configuration.getTypeAliasRegistry().registerAliases("com.mybatis.demo.entity");
        
        // 配置环境
        JdbcTransactionFactory transactionFactory = new JdbcTransactionFactory();
        PooledDataSource dataSource = new PooledDataSource();
        Environment environment = new Environment("development", transactionFactory, dataSource);
        dataSource.setDriver(configuration.getVariables().getProperty("driver"));
        dataSource.setUrl(configuration.getVariables().getProperty("url"));
        dataSource.setUsername(configuration.getVariables().getProperty("username"));
        dataSource.setPassword(configuration.getVariables().getProperty("password"));
        configuration.setEnvironment(environment);

        // mapper 接口的配置
        configuration.getMapperRegistry().addMappers("com.mybatis.demo.mapper");

        // 每条sql语句封装
        StaticSqlSource sqlSource = new StaticSqlSource(configuration, "select * from user");
        MappedStatement mappedStatement = new MappedStatement.Builder(
            configuration,
            "sql_id",
            sqlSource,
            SqlCommandType.INSERT).build();
        configuration.addMappedStatement(mappedStatement);
    }

    @Test
    public void testXMLConfigBuilder() throws IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        // mybatis xml配置文件的构造器（里面有xml的解析，解析成Configuration对象）
        XMLConfigBuilder xmlConfigBuilder = new XMLConfigBuilder(inputStream);
        Configuration configuration = xmlConfigBuilder.parse();
        log.info("configuration -> {}", configuration);
    }

}
