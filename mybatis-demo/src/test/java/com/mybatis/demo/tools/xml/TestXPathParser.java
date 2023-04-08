package com.mybatis.demo.tools.xml;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.builder.xml.XMLMapperEntityResolver;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.parsing.XPathParser;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author yanggy
 */
@Slf4j
public class TestXPathParser {

    @Test
    public void testXPathParser() throws IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        XPathParser xPathParser = new XPathParser(inputStream, true, null, new XMLMapperEntityResolver());
        // 通过 xpath 路径查询节点
        XNode xNode = xPathParser.evalNode("/configuration/properties");
        log.info("node -> {}", xNode);
    }
}
