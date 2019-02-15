package com.zzhb.zzoa.controller.xtgl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zzhb.zzoa.domain.common.Dict;
import com.zzhb.zzoa.domain.common.Icon;
import com.zzhb.zzoa.domain.common.Menu;
import com.zzhb.zzoa.domain.common.Param;
import com.zzhb.zzoa.mapper.DictMapper;
import com.zzhb.zzoa.mapper.IconMapper;
import com.zzhb.zzoa.mapper.MenuMapper;
import com.zzhb.zzoa.mapper.ParamMapper;
import com.zzhb.zzoa.service.DictService;
import com.zzhb.zzoa.service.IconService;
import com.zzhb.zzoa.service.MenuService;
import com.zzhb.zzoa.service.ParamService;

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

	@Autowired
	DictService dictService;

	@Autowired
	DictMapper dictMapper;

	@GetMapping("/zdgl")
	public String zdgl() {
		return "xtgl/xtsz/zdgl/zdgl";
	}

	@GetMapping("/zdgl/list")
	@ResponseBody
	public JSONObject listDicts(Integer page, Integer limit, @RequestParam Map<String, String> params) {
		JSONObject listDicts = dictService.listDicts(page, limit, params);
		return listDicts;
	}

	@GetMapping("/zdgl/pop")
	public ModelAndView zdglpop(@RequestParam Map<String, String> params) {
		ModelAndView mv = new ModelAndView();
		mv.addObject("params", params);
		String flag = params.get("flag");
		if ("edit".equals(flag)) {
			Dict dict = dictMapper.getDict(params);
			mv.addObject("dict", dict);
		}
		mv.setViewName("xtgl/xtsz/zdgl/pop");
		return mv;
	}

	@PostMapping("/zdgl/saveorupdate")
	@ResponseBody
	public Integer saveorupdate(Dict dict, String flag) {
		return dictService.saveorupdateDict(dict, flag);
	}

	@PostMapping("/zdgl/del")
	@ResponseBody
	public Integer del(@RequestParam Map<String, String> params) {
		return dictService.delDict(params.get("d_id"));
	}

	@GetMapping("/zdgl/zdlist")
	public ModelAndView zdlist(@RequestParam Map<String, String> params) {
		ModelAndView mv = new ModelAndView();
		mv.addObject("params", params);
		mv.setViewName("xtgl/xtsz/zdgl/zdlist");
		return mv;
	}

	@GetMapping("/zdgl/zdpop")
	public ModelAndView zdpop(@RequestParam Map<String, String> params) {
		ModelAndView mv = new ModelAndView();
		mv.addObject("params", params);
		String flag = params.get("flag");
		if ("edit".equals(flag)) {
			Dict dict = dictMapper.getDict(params);
			mv.addObject("dict", dict);
		}
		mv.setViewName("xtgl/xtsz/zdgl/zdpop");
		return mv;
	}

	@Autowired
	ParamMapper paramMapper;

	@Autowired
	ParamService paramService;

	@GetMapping("/cssz/getAllParams")
	@ResponseBody
	public JSONObject getAllParams(Integer page, Integer limit, @RequestParam Map<String, String> params) {
		return paramService.getAllParams(page, limit, params);
	}

	@GetMapping("/cssz/editpage")
	public ModelAndView goEditPage(@RequestParam Map<String, String> map) {
		ModelAndView model = new ModelAndView();
		String flag = map.get("flag");
		String url = "xtgl/xtsz/cssz/add";
		model.setViewName(url);
		if (flag.equals("edit")) {
			Param param = paramMapper.getParamById(Integer.parseInt(map.get("p_id")));
			model.addObject("param", param);
		}
		model.addObject("map", map);
		return model;
	}

	@PostMapping("/cssz/saveParam")
	@ResponseBody
	public Integer saveParam(Param param, String flag) {
		return paramService.saveParam(param, flag);
	}

	@PostMapping("/cssz/delParamById")
	@ResponseBody
	public Integer delParamById(@RequestParam Map<String, String> map) {
		return paramService.delParam(map);
	}

	@Autowired
	IconService iconService;

	@GetMapping("/tbgl")
	public String tbgl() {
		return "xtgl/xtsz/tbgl/tbgl";
	}

	@GetMapping("/tbgl/list")
	@ResponseBody
	public JSONObject tbglList(Integer page, Integer limit, @RequestParam Map<String, String> params) {
		return iconService.tbglList(page, limit, params);
	}

	@GetMapping("/tbgl/pop")
	public String tbglPop(@RequestParam Map<String, String> params, ModelMap model) {
		String i_id = params.get("i_id");
		if (i_id != null) {
			Icon icon = iconMapper.getIcon(params);
			model.put("icon", icon);
		}
		return "xtgl/xtsz/tbgl/pop";
	}

	@PostMapping("/tbgl/pop/add")
	@ResponseBody
	public Integer tbglPopAdd(Icon icon) {
		return iconService.tbglPodAdd(icon);
	}

	@PostMapping("/tbgl/pop/del")
	@ResponseBody
	public Integer tbglPopDel(Icon icon) {
		return iconService.tbglPodDel(icon.getId() + "");
	}
}
