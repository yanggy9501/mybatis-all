package com.mybatis.demo.tools;

import com.mybatis.demo.entity.User;
import org.apache.ibatis.type.TypeAliasRegistry;
import org.junit.Test;

/**
 * 别名测试
 *
 * @author yanggy
 */
public class TestTypeAlias {

    @Test
    public void testTypeAliasRegistry() {
        TypeAliasRegistry typeAliasRegistry = new TypeAliasRegistry();
        // 单一注册: 注意，都会首字母小写
        typeAliasRegistry.registerAlias("user", User.class);
        // 批量注册
        typeAliasRegistry.registerAliases("com.mybatis.demo.entity");
    }
}
