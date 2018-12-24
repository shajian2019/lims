package com.zzhb.zzoa.controller.xtgl;

import com.alibaba.fastjson.JSONObject;
import com.zzhb.zzoa.domain.User;
import com.zzhb.zzoa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

//组织管理
@Controller
@RequestMapping("/xtgl/zzgl")
public class ZzglController {

	@Autowired
	UserService userService;

	@GetMapping("/yhgl")
	public String yhgl() {
		return "xtgl/zzgl/yhgl/yhgl";
	}
	@GetMapping("/yhgl/getAllUsers")
	@ResponseBody
	public JSONObject getAllUsers(Integer page,Integer limit,@RequestParam Map<String, String> params){
		return userService.getAllUsers(page,limit,params);
	}

	@PostMapping("/yhgl/delUserByid")
	@ResponseBody
	public Integer delUserById(@RequestParam Map<String,Object> map){
		System.out.println(map.get("id"));
		return userService.delUserById(map);
	}

	@GetMapping("/yhgl/editPage")
	public ModelAndView goEditPage(@RequestParam Map<String,String> map){
		ModelAndView model = new ModelAndView();
		String flag = (String) map.get("flag");
		String url = "";
		if(flag.equals("add")){
			url = "xtgl/zzgl/yhgl/add";
			model.setViewName(url);
		}else if(flag.equals("edit")){

			JSONObject userJson = userService.getAllUsers(1,10,map);
			System.out.println(userJson.toString());
			model.addObject("user",userJson);
			url = "xtgl/zzgl/yhgl/edit";
			model.setViewName(url);
		}
		return model;
	}


	@PostMapping("/yhgl/addUser")
	@ResponseBody
	public Integer addUser(@RequestParam Map<String,Object> map){
		return userService.addUser(map);
	}

	@PostMapping("/yhgl/updateUser")
	@ResponseBody
	public Integer updateUser(@RequestParam Map<String,Object> map){
		return userService.updateUser(map);
	}
}
