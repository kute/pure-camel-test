package com.kute.camel;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.kute.camel.beans.HelloBean;
import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.support.SimpleRegistry;
import org.junit.jupiter.api.Test;

/**
 * created by bailong001 on 2020/09/14 20:21
 */
public class CamleContextTest {

    @Test
    public void test() {

        SimpleRegistry registry = new SimpleRegistry();
        registry.put("helloBean", Maps.newHashMap(ImmutableMap.of(HelloBean.class, new HelloBean())));

        CamelContext context = new DefaultCamelContext(registry);




        context.getRegistry().lookupByNameAndType("helloBean", HelloBean.class);

    }

}
