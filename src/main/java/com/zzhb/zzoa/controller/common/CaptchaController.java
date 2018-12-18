package com.zzhb.zzoa.controller.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.github.bingoohuang.patchca.custom.ConfigurableCaptchaService;
import com.github.bingoohuang.patchca.filter.FilterFactory;
import com.github.bingoohuang.patchca.filter.predefined.CurvesRippleFilterFactory;
import com.github.bingoohuang.patchca.filter.predefined.DiffuseRippleFilterFactory;
import com.github.bingoohuang.patchca.filter.predefined.DoubleRippleFilterFactory;
import com.github.bingoohuang.patchca.filter.predefined.MarbleRippleFilterFactory;
import com.github.bingoohuang.patchca.filter.predefined.WobbleRippleFilterFactory;
import com.github.bingoohuang.patchca.utils.encoder.EncoderHelper;
import com.zzhb.zzoa.utils.CaptchaFactory;

@Controller
public class CaptchaController {
	private static Logger logger = Logger.getLogger(CaptchaController.class);

	private static ConfigurableCaptchaService cs = CaptchaFactory.getInstance();

	private static List<FilterFactory> factories;

	static {
		factories = new ArrayList<FilterFactory>();
		factories.add(new CurvesRippleFilterFactory(cs.getColorFactory()));
		factories.add(new MarbleRippleFilterFactory());
		factories.add(new DoubleRippleFilterFactory());
		factories.add(new WobbleRippleFilterFactory());
		factories.add(new DiffuseRippleFilterFactory());
	}

	@GetMapping("/captcha")
	public void getImage(HttpServletRequest request, HttpServletResponse response) {
		try {
			cs.setFilterFactory(factories.get(new Random().nextInt(5)));
			setResponseHeaders(response);
			Session session = SecurityUtils.getSubject().getSession();
			String token = EncoderHelper.getChallangeAndWriteImage(cs, "png", response.getOutputStream());
			session.setAttribute("CAPTCHA", token);
			logger.info("=========SessionID =========" + session.getId() + "，  验证码 = " + token);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void setResponseHeaders(HttpServletResponse response) {
		response.setContentType("image/png");
		response.setHeader("Cache-Control", "no-cache, no-store");
		response.setHeader("Pragma", "no-cache");
		long time = System.currentTimeMillis();
		response.setDateHeader("Last-Modified", time);
		response.setDateHeader("Date", time);
		response.setDateHeader("Expires", time);
	}
}
