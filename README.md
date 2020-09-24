# pure-camel-test

https://github.com/apache/camel
https://camel.apache.org/
https://github.com/apache/camel-examples/
https://www.javadoc.io/doc/org.apache.camel/camel-api/latest/index.html
https://plugins.jetbrains.com/plugin/9371-apache-camel-idea-plugin








4、endpoints
4.1、file
file:data/inbox?noop=true
可选参数见：org.apache.camel.component.file.FileEndpointConfigurer
4.2、jms
4.3、bean
bean:helloBean?method=hello
org.apache.camel.component.bean.BeanEndpointConfigurer
4.4、

3、multicast
加了multicast发送时会消息会被复制多份，可以指定线程池来并发，不加则消息是 依次逐个发送到 各个 endpoint

2、InOnly 、 InOut ?
InOnly: a one way message(like event message)
InOut: a request-response message

1、wireTap ？

from("jms:incomingOrders").wireTap("jms:orderAudit")，固定大小的recipient-list, 会将incomingOrders的exchange复制一份并发送到 orderAudit，而不影响后续的route，
即 允许将整个消息路由到一个独立的目的地，消息类型为 InOnly，即 send-and-forget，不会等待orderAudit响应

