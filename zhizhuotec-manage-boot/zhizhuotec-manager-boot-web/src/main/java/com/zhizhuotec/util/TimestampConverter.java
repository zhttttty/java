package com.zhizhuotec.util;

import java.sql.Timestamp;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;

public class TimestampConverter implements Converter<String, Timestamp> {

	@Override
	public Timestamp convert(String arg0) {
		if (StringUtils.isNotBlank(arg0)) {
			Timestamp timestamp = Timestamp.valueOf(arg0);
			return timestamp;
		}
		return null;
	}

}
