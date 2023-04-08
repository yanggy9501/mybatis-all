package com.mybatis.demo.tools.ognl;

import com.mybatis.demo.entity.User;
import lombok.extern.slf4j.Slf4j;
import ognl.Ognl;
import ognl.OgnlException;
import org.junit.Test;

import java.util.Date;

/**
 * @author yanggy
 */
@Slf4j
public class TestOgnl {

    /**
     * ognl mybatis 已经依赖了
     */
    @Test
    public void testOgnl01() throws OgnlException {
        User user = new User();
        user.setUsername("xxx");
        user.setId(111L);
        user.setCreateTime(new Date());

        Object username = Ognl.getValue("username", user);
        log.info("username -> {}", username);
    }
}
