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

/**
 * @author Clinton Begin
 */
public class IfSqlNode implements SqlNode {
  /**
   * ognl 解析器
   */
  private final ExpressionEvaluator evaluator;

  /**
   * 条件：a != null and a !=''
   */
  private final String test;

  /**
   * if 标签中的 “内容”
   */
  private final SqlNode contents;

  public IfSqlNode(SqlNode contents, String test) {
    this.test = test;
    this.contents = contents;
    this.evaluator = new ExpressionEvaluator();
  }

  @Override
  public boolean apply(DynamicContext context) {
    // ognl 解析
    if (evaluator.evaluateBoolean(test, context.getBindings())) {
      // 处理完<if 递归回去继续处理
      contents.apply(context);
      return true;
    }
    return false;
  }

}
