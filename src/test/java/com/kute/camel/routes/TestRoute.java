package com.kute.camel.routes;

import com.kute.camel.myself.routes.AbstractRoute;
import org.springframework.stereotype.Component;

/**
 * created by bailong001 on 2020/10/09 13:16
 * 会自动被camel加载
 */
@Component
public class TestRoute extends AbstractRoute {

    @Override
    public void configure() throws Exception {
        super.configure();

        from("direct:start3")
                .to("{{t1route.to}}");
    }
}
