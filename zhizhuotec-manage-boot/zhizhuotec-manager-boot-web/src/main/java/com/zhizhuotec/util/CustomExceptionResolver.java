package com.zhizhuotec.util;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.zhizhuotec.common.utils.Utils;

/**
 * 全局异常处理器
 * 
 * @author CatalpaFlat
 */
public class CustomExceptionResolver implements HandlerExceptionResolver, Ordered {

	// 系统抛出的异常
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		// handler就是处理器适配器要执行的Handler对象(只有method)
		// 解析出异常类型。
		/* 使用response返回 */
		response.setStatus(HttpStatus.OK.value()); // 设置状态码
		response.setContentType(MediaType.APPLICATION_JSON_VALUE); // 设置ContentType
		response.setCharacterEncoding("UTF-8"); // 避免乱码
		response.setHeader("Cache-Control", "no-cache, must-revalidate");
		// 如果该 异常类型是系统 自定义的异常，直接取出异常信息。
		try {
			JSONObject jObject = new JSONObject();
			jObject.put("flag", "333");
			jObject.put("msg", "Server exception");
			Utils.logger("ERROR", "Server exception：", ex);
			response.getWriter().write(jObject.toString());
		} catch (IOException e) {
			Utils.logger("ERROR", "Exception catch failure：", e);
		}
		ModelAndView modelAndView = new ModelAndView();
		return modelAndView;
	}

	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE;
	}

}