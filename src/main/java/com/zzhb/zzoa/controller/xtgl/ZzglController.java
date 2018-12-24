package com.zzhb.zzoa.controller.xtgl;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.zzhb.zzoa.domain.User;
import com.zzhb.zzoa.mapper.RoleMapper;
import com.zzhb.zzoa.mapper.UserMapper;
import com.zzhb.zzoa.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.zzhb.zzoa.service.UserService;

//组织管理
@Controller
@RequestMapping("/xtgl/zzgl")
public class ZzglController {

	@Autowired
	UserService userService;

	@Autowired
    RoleService roleService;

	@Autowired
	UserMapper userMapper;

	@Autowired
	RoleMapper roleMapper;

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
		}else if(flag.equals("edit")){
			String username = map.get("username");
			User u = userMapper.getUser(username);
			model.addObject("user",u);
			Integer r_id = roleMapper.getRoleIds(u.getU_id());
			model.addObject("r_id",r_id);
			url = "xtgl/zzgl/yhgl/edit";
		}
		model.setViewName(url);
		return model;
	}


	@PostMapping("/yhgl/addUser")
	@ResponseBody
	public Integer addUser(@RequestParam Map<String,String> map){
		Integer i = userService.addUser(map);
		User user = userService.getUser(String.valueOf(map.get("username")));
		if(map.get("role") != null && !"".equals(map.get("role"))){
			Integer u_id = user.getU_id();
			Integer r_id = Integer.parseInt(String.valueOf(map.get("role")));
			Map<String,Integer> userRoleMap = new HashMap<String,Integer>();
			userRoleMap.put("u_id",u_id);
			userRoleMap.put("r_id",r_id);
			return userService.addUrole(userRoleMap);
		}
		return i;
	}

	@PostMapping("/yhgl/updateUser")
	@ResponseBody
	public Integer updateUser(@RequestParam Map<String,Object> map){
		return userService.updateUser(map);
	}

	@GetMapping("/yhgl/getAllRole")
    @ResponseBody
    public JSONObject getAllRole(@RequestParam Map<String,String> map){

        return roleService.listRoles(1,Integer.MAX_VALUE,map);
    }
}
