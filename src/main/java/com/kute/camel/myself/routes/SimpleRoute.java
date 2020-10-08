package com.kute.camel.myself.routes;

/**
 * created by kute at 2020/8/16 11:10 上午
 */
public class SimpleRoute extends AbstractRoute {
    @Override
    public void configure() throws Exception {
        // 显示调用，复用error handler
        super.configure();

        from("")
                // 配置此 route对应的 错误处理器
                .errorHandler(defaultErrorHandler())
                .to("");
    }
}
