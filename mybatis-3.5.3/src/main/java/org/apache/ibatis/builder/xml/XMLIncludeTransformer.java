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
package org.apache.ibatis.builder.xml;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

import org.apache.ibatis.builder.BuilderException;
import org.apache.ibatis.builder.IncompleteElementException;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.parsing.PropertyParser;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.session.Configuration;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Frank D. Martinez [mnesarco]
 */
public class XMLIncludeTransformer {

  private final Configuration configuration;
  private final MapperBuilderAssistant builderAssistant;

  public XMLIncludeTransformer(Configuration configuration, MapperBuilderAssistant builderAssistant) {
    this.configuration = configuration;
    this.builderAssistant = builderAssistant;
  }

  public void applyIncludes(Node source) {
    Properties variablesContext = new Properties();
    // 拿到之前配置文件解析的<properties>
    Properties configurationVariables = configuration.getVariables();
    // 放入到variablesContext中
    Optional.ofNullable(configurationVariables).ifPresent(variablesContext::putAll);
    // 替换Includes标签为对应的sql标签里面的值
    applyIncludes(source, variablesContext, false);
  }

  /**
   * Recursively apply includes through all SQL fragments.
   * @param source Include node in DOM tree. 如 select|insert|delete|update|include等标签
   * @param variablesContext Current context for static variables with values
   */
  private void applyIncludes(Node source, final Properties variablesContext, boolean included) {
    // 如果是 <include> 标签，开始时是 select 等标签走，included=false 走 else
    if (source.getNodeName().equals("include")) {
      // <include refid= "${sqlKey}"/> 或 <include refid= "sql1}"/> 解析，拿到之前解析的 <sql> 标签
      Node toInclude = findSqlFragment(getStringAttribute(source, "refid"), variablesContext);
      // 解析 include 中的 <key value = "value" /> 标签成 toIncludeContext
      Properties toIncludeContext = getVariablesContext(source, variablesContext);
      // 递归 <sql><sql/></sql> sql 标签包含sql标签， 所以included=true
      applyIncludes(toInclude, toIncludeContext, true);
      if (toInclude.getOwnerDocument() != source.getOwnerDocument()) {
        toInclude = source.getOwnerDocument().importNode(toInclude, true);
      }
      // <include的父节点=select 。  将<select>里面的<include/>替换成 <sql> 标签 ，那<include>.getParentNode就为Null了
      source.getParentNode().replaceChild(toInclude, source);
      while (toInclude.hasChildNodes()) { // sql 标签包含标签
        // 接下来<sql>.getParentNode()=select.  在<sql>的前面插入<sql> 中的sql语句   ,
        toInclude.getParentNode().insertBefore(toInclude.getFirstChild(), toInclude);
      }
      // <sql>.getParentNode()=select  , 移除select中的<sql> Node 。
      //  不知道为什么不直接replaceChild呢？还做2步 先插再删，
      toInclude.getParentNode().removeChild(toInclude);
      int i=0;
    } else if (source.getNodeType() == Node.ELEMENT_NODE) { // 0
      if (included && !variablesContext.isEmpty()) {
        // replace variables in attribute values
        NamedNodeMap attributes = source.getAttributes();
        for (int i = 0; i < attributes.getLength(); i++) {
          Node attr = attributes.item(i);
          attr.setNodeValue(PropertyParser.parse(attr.getNodeValue(), variablesContext));
        }
      }
      NodeList children = source.getChildNodes();
      // 解析每个 Include 子标签
      for (int i = 0; i < children.getLength(); i++) {
        // 递归
         applyIncludes(children.item(i), variablesContext, included);
      }
      // included=true 说明是从include递归进来的
    } else if (included && (source.getNodeType() == Node.TEXT_NODE || source.getNodeType() == Node.CDATA_SECTION_NODE)
        && !variablesContext.isEmpty()) {
      // 替换sql片段中的 ${<properties解析到的内容>}
      source.setNodeValue(PropertyParser.parse(source.getNodeValue(), variablesContext));
    }
  }

  private Node findSqlFragment(String refid, Properties variables) {
    // 解析refid 的表达式，若存在 ${sqlName} 则返回 variables.get(sqlName)的值
    refid = PropertyParser.parse(refid, variables);
    // 获取refid的命名空间点.上refid
    refid = builderAssistant.applyCurrentNamespace(refid, true);
    try {
      // 拿到之前解析的<sql>
      XNode nodeToInclude = configuration.getSqlFragments().get(refid);
      return nodeToInclude.getNode().cloneNode(true);
    } catch (IllegalArgumentException e) {
      throw new IncompleteElementException("Could not find SQL statement to include with refid '" + refid + "'", e);
    }
  }

  private String getStringAttribute(Node node, String name) {
    return node.getAttributes().getNamedItem(name).getNodeValue();
  }

  /**
   * Read placeholders and their values from include node definition.
   * @param node Include node instance
   * @param inheritedVariablesContext Current context used for replace variables in new variables values
   * @return variables context from include instance (no inherited values)
   */
  private Properties getVariablesContext(Node node, Properties inheritedVariablesContext) {
    Map<String, String> declaredProperties = null;
    NodeList children = node.getChildNodes();
    // include 标签node中可以包含任意多个 <anyTagName任意标签 value="值" /> 的标签，解析成 k-v 的 Properties并与全局的合并
    for (int i = 0; i < children.getLength(); i++) {
      Node n = children.item(i);
      if (n.getNodeType() == Node.ELEMENT_NODE) {
        String name = getStringAttribute(n, "name");
        // Replace variables inside
        String value = PropertyParser.parse(getStringAttribute(n, "value"), inheritedVariablesContext);
        if (declaredProperties == null) {
          declaredProperties = new HashMap<>();
        }
        if (declaredProperties.put(name, value) != null) {
          throw new BuilderException("Variable " + name + " defined twice in the same include definition");
        }
      }
    }
    if (declaredProperties == null) {
      return inheritedVariablesContext;
    } else {
      Properties newProperties = new Properties();
      newProperties.putAll(inheritedVariablesContext);
      newProperties.putAll(declaredProperties);
      return newProperties;
    }
  }
}
