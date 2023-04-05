package com.mybatis.demo.mb;

import com.mybatis.demo.entity.User;
import org.apache.ibatis.scripting.xmltags.ExpressionEvaluator;
import org.junit.Test;

/***
 * @Author 徐庶   QQ:1092002729
 * @Slogan 致敬大师，致敬未来的你
 */
public class OgnlTest {

    @Test
    public void ognlPlay() {
        ExpressionEvaluator expressionEvaluator = new ExpressionEvaluator();

        User user = new User();
        user.setId(1L);
        user.setUsername(null);

        boolean b = expressionEvaluator.evaluateBoolean("id>0 and username!=null", user);
        System.out.println(b);
    }



}
