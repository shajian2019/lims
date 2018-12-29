package com.zzhb.zzoa.exception;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class Global500ExceptionHandler {

	private static Logger logger = LoggerFactory.getLogger(Global500ExceptionHandler.class);

	@ExceptionHandler(value = Exception.class)
	public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e) {
		ModelAndView mv = new ModelAndView("error/500");
		e.printStackTrace();
		logger.error(req.getRequestURI() + "========" + e.toString());
		mv.addObject("msg", e.toString());
		return mv;
	}
}
