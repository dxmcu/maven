package com.calculatedfun.err;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.calculatedfun.util.DateFactory;
import com.calculatedfun.err.ParameterException;
import com.calculatedfun.err.SystemBussException;

public class MyExceptionHandler implements HandlerExceptionResolver {

	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("ex", ex);
		// 是否异步请求
		if (!(request.getHeader("accept").indexOf("application/json") > -1
				|| (request.getHeader("X-Requested-With") != null
						&& request.getHeader("X-Requested-With").indexOf("XMLHttpRequest") > -1))) {
			// 根据不同错误转向不同页面
			if (ex instanceof SystemBussException) {
				response.setStatus(1001);// 业务异常返回 1001
				return new ModelAndView("WEB-INF/jsp/exception/error-system", model);
			} else if (ex instanceof ParameterException) {
				response.setStatus(1002);// 参数异常返回 1002
				return new ModelAndView("WEB-INF/jsp/exception/error-parameter", model);
			} else {
				response.setStatus(1000);// 其他异常返回 1000
				return new ModelAndView("WEB-INF/error", model);
			}
		} else {
			try {
				if (ex instanceof SystemBussException) {
					response.setStatus(1001);// 业务异常返回 1001
				} else if (ex instanceof ParameterException) {
					response.setStatus(1002);// 参数异常返回 1002
				} else {
					response.setStatus(1000);// 其他异常返回 1000
				}
				System.err.println("统一异常处理:" + DateFactory.getCurrTime() + ":" + ex.getMessage());
			} catch (Exception e) {
				model.put("ex", e);
				return new ModelAndView("WEB-INF/error", model);
			}
			return null;
		}
	}
}
