/**
 *    Copyright 2009-2024 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.ibatis.scripting.xmltags;

import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.session.Configuration;

/**
 * 动态sql:主要是封装动态SQL标签解析之后的SQL语句和带有${}的SQL语句
 *
 * @author Clinton Begin
 */
public class DynamicSqlSource implements SqlSource {

  private final Configuration configuration;
  /**
   * SQL Node 根节点，一般是 MixedSqlNode
   */
  private final SqlNode rootSqlNode;

  public DynamicSqlSource(Configuration configuration, SqlNode rootSqlNode) {
    this.configuration = configuration;
    this.rootSqlNode = rootSqlNode;
  }

  /**
   * 动态sql：带有标签如 if where 等等标签或者待用mybatis占位符 ${} #{}
   * 解析：1.解析标签，2.mybatis占位符替换位statement占位符 ？
   *
   * 这个方法主要做2件事：
   * 1. 解析所有sqlNode  解析成一条完整sql语句
   * 2. 将sql语句中的#{} 替换成问号 ?， 并且把#{}中的参数解析成ParameterMapping （里面包含了typeHandler)
   * @param parameterObject:参数对象
   * @return
   */
  @Override
  public BoundSql getBoundSql(Object parameterObject) {
    DynamicContext context = new DynamicContext(configuration, parameterObject);
    // 1.责任链 处理一个个SqlNode 编译出一个完整sql
    rootSqlNode.apply(context);
    SqlSourceBuilder sqlSourceParser = new SqlSourceBuilder(configuration);
    // 2.接下来处理 处理sql中的#{...}
    Class<?> parameterType = parameterObject == null ? Object.class : parameterObject.getClass();
    // 怎么处理呢？很简单，就是拿到#{}中的内容封装为 parameterMapper， 替换成?
    SqlSource sqlSource = sqlSourceParser.parse(context.getSql(), parameterType, context.getBindings());
    BoundSql boundSql = sqlSource.getBoundSql(parameterObject);
    context.getBindings().forEach(boundSql::setAdditionalParameter);
    return boundSql;
  }

}
