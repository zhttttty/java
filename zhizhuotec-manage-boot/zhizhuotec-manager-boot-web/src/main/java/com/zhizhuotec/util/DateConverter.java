package com.zhizhuotec.util;

import java.sql.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;

public class DateConverter implements Converter<String, Date> {

	@Override
	public Date convert(String arg0) {
		if (StringUtils.isNotBlank(arg0)) {
			Date date = Date.valueOf(arg0);
			return date;
		}
		return null;
	}
}
