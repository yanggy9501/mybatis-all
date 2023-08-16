package com.mybatis.demo.handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 带分号的 String 和 List<String> 的类型转换器
 * 实现 s1,s2,s3 --> list=[s1, s2, s3]
 * 使用：
 * 1. mybatis 配置文件中添加类型转换器
 * 2. orm 映射中声明使用的类型转换器，如：
 * 查询：
 * <resultMap>
 *     <result property="content" column="CONTENT" typeHandler="com.mybatis.demo.handler.StringWithCommaToListTypeHandler"/>
 * </resultMap>
 * 插入：
 * #{content, typeHandler=com.mybatis.demo.handler.StringWithCommaToListTypeHandler}
 *
 * 泛型：转换的类型
 *
 * @MappedJdbcTypes: 表示SQL语句中查出来的类型
 * @MappedTypes: 表示要转成 Java 对象的类型
 * DELIM：表示字符串的分隔符，如果你是用空格分开的就赋值为空格
 *
 * 泛型：转成 Java 对象的类型
 *
 * @author yanggy
 */
@MappedJdbcTypes(JdbcType.VARCHAR)
@MappedTypes({List.class})
public class StringWithCommaToListTypeHandler extends BaseTypeHandler<List<String>> {
    /**
     * 字符串分隔符
     */
    private static final String DELIMIT = ",";

    /**
     * 当参数 List<String> 不为 null 时，List<String> 转换为带分号的 String
     *
     * @param ps
     * @param i
     * @param parameter
     * @param jdbcType
     * @throws SQLException
     */
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<String> parameter, JdbcType jdbcType) throws SQLException {
        String value = String.join(DELIMIT, parameter);
        ps.setString(i, value);
    }

    /**
     * 获取该字段的结果，即带分号的 String 转换为 List<String>
     *
     * @param rs
     * @param columnName Colunm name, when configuration <code>useColumnLabel</code> is <code>false</code>
     * @return
     * @throws SQLException
     */
    @Override
    public List<String> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String value = rs.getString(columnName);
        return stringToList(value);
    }

    /**
     * 带分号的 String 转换为 List<String>
     *
     * @param rs
     * @param columnIndex
     * @return
     * @throws SQLException
     */
    @Override
    public List<String> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String value = rs.getString(columnIndex);
        return stringToList(value);
    }

    /**
     * 带分号的 String 转换为 List<String>
     *
     * @param cs
     * @param columnIndex
     * @return
     * @throws SQLException
     */
    @Override
    public List<String> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String value = cs.getString(columnIndex);
        return stringToList(value);
    }

    private static List<String> stringToList(String value) {
        List<String> strList = new ArrayList<>();
        if (value != null) {
            String[] array = value.split(DELIMIT);
            strList.addAll(Arrays.asList(array));
        }
        return strList;
    }
}
