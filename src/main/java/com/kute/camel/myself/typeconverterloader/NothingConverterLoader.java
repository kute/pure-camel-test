package com.kute.camel.myself.typeconverterloader;

import com.kute.camel.myself.typeconverter.NothingConverter;
import org.apache.camel.TypeConverterLoaderException;
import org.apache.camel.spi.TypeConverterLoader;
import org.apache.camel.spi.TypeConverterRegistry;

/**
 * created by kute at 2020/8/2 11:01 上午
 */
public final class NothingConverterLoader implements TypeConverterLoader {

	@Override
	public void load(TypeConverterRegistry registry) throws TypeConverterLoaderException {
		addTypeConverter(registry);
	}

	private void addTypeConverter(TypeConverterRegistry registry) {
		registerTypeConverter(Object.class, Object.class, registry);
	}

	public static void registerTypeConverter(Class<?> from, Class<?> to, TypeConverterRegistry registry) {
		registry.addTypeConverter(from, to, new NothingConverter());
	}
}
