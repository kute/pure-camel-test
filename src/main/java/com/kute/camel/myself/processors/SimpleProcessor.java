package com.kute.camel.myself.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 * created by kute at 2020/8/16 10:39 上午
 *
 * exchange.getOut() 会丢失掉message的header和attachment
 */
public class SimpleProcessor implements Processor {
	@Override
	public void process(Exchange exchange) throws Exception {

		String message = exchange.getIn().getBody(String.class);

	}
}
