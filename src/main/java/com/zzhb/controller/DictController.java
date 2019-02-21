package com.zzhb.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zzhb.domain.common.Dict;
import com.zzhb.service.DictService;

@Controller
@RequestMapping("/dict")
public class DictController {

	@Autowired
	DictService dictService;

	@GetMapping("/get")
	@ResponseBody
	public List<Dict> getDicts(String gtype) {
		return dictService.getDictByGtype(gtype);
	}
}