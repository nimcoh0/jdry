package org.softauto.logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;

public class Serializer {

	private static final Logger logger = LoggerFactory.getLogger(Serializer.class);

	private static ObjectMapper objectMapper;

	public ObjectMapper buildObjectMapper() {
		try {
			if (objectMapper == null) {
				objectMapper = new ObjectMapper();
				SimpleModule userModule = new SimpleModule();
				objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
				//userModule.addSerializer(HttpServletRequestWrapper.class, new HttpServletRequestSerializer());
				objectMapper.registerModule(userModule);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return objectMapper;
	}

	public Object serialize(Object arg) {
		objectMapper = buildObjectMapper();
		return _serialize(arg);
	}

	public LinkedList<Object> serialize(Object[] args) {
		objectMapper = buildObjectMapper();
		LinkedList<Object> _args = new LinkedList<>();
		try {
			for (int i = 0; i < args.length; i++) {
				_args.add(_serialize(args[i]));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return _args;
	}

	private Object _serialize(Object arg) {
		try {
			if (arg != null && !(arg instanceof String) && !Util.isPrimitive(arg.getClass().getTypeName())) {
				return objectMapper.writeValueAsString(arg);
			} else {
				return arg;
			}
		} catch (Exception e) {
			logger.warn(e.getCause().toString());
		}
		return arg;
	}

}
