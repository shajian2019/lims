package com.zzhb.zzoa.controller.xtgl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zzhb.zzoa.domain.common.Icon;
import com.zzhb.zzoa.domain.common.Menu;
import com.zzhb.zzoa.mapper.IconMapper;
import com.zzhb.zzoa.mapper.MenuMapper;
import com.zzhb.zzoa.service.MenuService;

//系统设置
@Controller
@RequestMapping("/xtgl/xtsz")
public class XtszController {

	@GetMapping("/cdgl")
	public String cdgl() {
		return "xtgl/xtsz/cdgl/cdgl";
	}

	@Autowired
	MenuService menuService;

	@Autowired
	MenuMapper menuMapper;

	@Autowired
	IconMapper iconMapper;

	@GetMapping("/cdgl/getall")
	@ResponseBody
	public JSONObject getCd(@RequestParam Map<String, String> params) {
		return menuService.getAllMenus(params);
	}

	@GetMapping("/cdgl/edit")
	public ModelAndView edit(@RequestParam Map<String, String> params) {
		ModelAndView mv = new ModelAndView();
		if (params.get("flag") != null) {
			mv.setViewName("xtgl/xtsz/cdgl/addone");
			return mv;
		}
		Menu menu = menuMapper.getMenu(params);
		mv.addObject("menu", menu);
		mv.addObject("params", params);
		mv.setViewName("xtgl/xtsz/cdgl/edit");
		return mv;
	}

	@GetMapping("/cdgl/icon")
	public ModelAndView icon(@RequestParam Map<String, String> params) {
		ModelAndView mv = new ModelAndView();
		List<Icon> icons = iconMapper.getIcons(params);
		mv.addObject("icons", icons);
		mv.addObject("type", params.get("type"));
		mv.setViewName("xtgl/xtsz/cdgl/icon");
		return mv;
	}

	@PostMapping("/cdgl/updateSort")
	@ResponseBody
	public Integer updateSort(@RequestParam("ids") String ids) {
		JSONArray array = JSON.parseArray(ids);
		System.out.println(array);
		return menuService.updateSort(array);
	}
	
	
	@GetMapping("/cssz")
	public String cssz() {
		return "xtgl/xtsz/cssz/cssz";
	}
	
	@GetMapping("/zdgl")
	public String zdgl() {
		return "xtgl/xtsz/zdgl/zdgl";
	}
}
