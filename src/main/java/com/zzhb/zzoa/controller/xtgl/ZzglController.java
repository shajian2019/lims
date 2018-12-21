package com.zzhb.zzoa.controller.xtgl;

import com.alibaba.fastjson.JSONObject;
import com.zzhb.zzoa.domain.User;
import com.zzhb.zzoa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

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

	@DeleteMapping("/yhgl/delUserByid")
	public String delUserById(@RequestParam("id")Integer id){
		userService.delUserById(id);
		return "success";
	}

	@GetMapping("/yhgl/editPage")
	public ModelAndView goEditPage(@RequestParam("flag")String flag){
		ModelAndView model = new ModelAndView();
		String url = "";
		if(flag.equals("add")){
			url = "xtgl/zzgl/yhgl/yhgl";
			model.setViewName(url);
		}else if(flag.equals("edit")){
			url = "";
		}
		return model;
	}


	@PutMapping("/addUser")
	public String addUser(User user){
		userService.addUser(user);
		return "success";
	}
}
