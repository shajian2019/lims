package com.zzhb.zzoa.controller.xtgl;

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

import com.alibaba.fastjson.JSONObject;
import com.zzhb.zzoa.domain.Job;
import com.zzhb.zzoa.domain.Org;
import com.zzhb.zzoa.domain.User;
import com.zzhb.zzoa.mapper.RoleMapper;
import com.zzhb.zzoa.mapper.UserMapper;
import com.zzhb.zzoa.service.RoleService;
import com.zzhb.zzoa.service.UserService;
import com.zzhb.zzoa.service.ZzjgService;

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
	public JSONObject getAllUsers(Integer page, Integer limit, @RequestParam Map<String, String> params) {
		return userService.getAllUsers(page, limit, params);
	}

	@PostMapping("/yhgl/delUserByid")
	@ResponseBody
	public Integer delUserById(String u_id) {
		return userService.delUserById(u_id);
	}

	@GetMapping("/yhgl/editPage")
	public ModelAndView goEditPage(@RequestParam Map<String, String> map) {
		ModelAndView model = new ModelAndView();
		String flag = map.get("flag");
		String url = "xtgl/zzgl/yhgl/add";
		if ("edit".equals(flag)) {
			String username = map.get("username");
			User user = userMapper.getUser(username);
			model.addObject("echouser", user);
			Integer r_id = roleMapper.getRoleIds(user.getU_id());
			model.addObject("echor_id", r_id);
		}
		model.addObject("map", map);
		model.setViewName(url);
		return model;
	}

	@PostMapping("/yhgl/addUser")
	@ResponseBody
	public Integer addUser(User user, String role, String flag) {
		return userService.addUser(user, role, flag);
	}

	@PostMapping("/yhgl/updateUser")
	@ResponseBody
	public Integer updateUser(@RequestParam Map<String, Object> map) {
		return userService.updateUser(map);
	}

	@GetMapping("/yhgl/getAllRole")
	@ResponseBody
	public JSONObject getAllRole(@RequestParam Map<String, String> map) {
		return roleService.listRoles(1, Integer.MAX_VALUE, map);
	}

	@PostMapping("/yhgl/resetPassword")
	@ResponseBody
	public Integer resetPass(@RequestParam Map<String, String> map) {
		return userService.resetPass(map);
	}

	@PostMapping("/yhgl/getUserByName")
	@ResponseBody
	public Integer getUserByName(@RequestParam("username") String username) {
		return userService.getAllUname(username);
	}

	@GetMapping("/zzjg")
	public String zzjg() {
		return "xtgl/zzgl/zzjg/zzjg";
	}

	@Autowired
	ZzjgService zzjgService;

	@GetMapping("/zzjg/list")
	@ResponseBody
	public JSONObject zzjgList() {
		return zzjgService.zzjgList();
	}

	@PostMapping("/zzjg/add")
	@ResponseBody
	public Integer zzjgAdd(Org org) {
		return zzjgService.zzjgAdd(org);
	}

	@PostMapping("/zzjg/edit")
	@ResponseBody
	public Integer zzjgEdit(Org org) {
		return zzjgService.zzjgEdit(org);
	}

	@PostMapping("/zzjg/del")
	@ResponseBody
	public Integer zzjgDel(String o_id) {
		return zzjgService.zzjgDel(o_id);
	}

	@GetMapping("/zzjg/user/list")
	@ResponseBody
	public JSONObject zzjgUserList(Integer page, Integer limit, @RequestParam Map<String, String> params) {
		return zzjgService.zzjgUserList(page, limit, params);
	}

	@GetMapping("/zzjg/adduser")
	public String zzjgAddUser(String o_id, ModelMap map) {
		map.put("o_id", o_id);
		return "xtgl/zzgl/zzjg/pop";
	}

	@GetMapping("/zzjg/adduser/list")
	@ResponseBody
	public JSONObject zzjgAddUserList(Integer page, Integer limit, @RequestParam Map<String, String> params) {
		return zzjgService.zzjgAddUserList(page, limit, params);
	}

	@PostMapping("/zzjg/user/del")
	@ResponseBody
	public Integer zzjgUserDel(String u_id) {
		return zzjgService.zzjgUserDel(u_id);
	}

	@PostMapping("/zzjg/user/add")
	@ResponseBody
	public Integer zzjgUserAdd(String o_id, String u_ids) {
		return zzjgService.zzjgUserAdd(o_id, u_ids);
	}
	
	@GetMapping("/zwgl")
	public String zwgl() {
		return "xtgl/zzgl/zwgl/zwgl";
	}
	
	@GetMapping("/zwgl/list")
	@ResponseBody
	public JSONObject zwglList() {
		return zzjgService.zwglList();
	}
	
	@PostMapping("/zwgl/add")
	@ResponseBody
	public Integer zwglAdd(Job job) {
		return zzjgService.zwglAdd(job);
	}
	
	@PostMapping("/zwgl/edit")
	@ResponseBody
	public Integer zwglEdit(Job job) {
		return zzjgService.zwglEdit(job);
	}
	
	@GetMapping("/zwgl/user/list")
	@ResponseBody
	public JSONObject zwglUserList(Integer page, Integer limit, @RequestParam Map<String, String> params) {
		return zzjgService.zwglUserList(page, limit, params);
	}
	
	@PostMapping("/zwgl/user/del")
	@ResponseBody
	public Integer zwglUserDel(String u_id) {
		return zzjgService.zwglUserDel(u_id);
	}
	
	@GetMapping("/zwgl/adduser/list")
	@ResponseBody
	public JSONObject zwglAddUserList(Integer page, Integer limit, @RequestParam Map<String, String> params) {
		return zzjgService.zwglAddUserList(page, limit, params);
	}
	
	@GetMapping("/zwgl/adduser")
	public String zwglAddUser(String j_id, ModelMap map) {
		map.put("j_id", j_id);
		return "xtgl/zzgl/zwgl/pop";
	}
	
	@PostMapping("/zwgl/user/add")
	@ResponseBody
	public Integer zwglUserAdd(String j_id, String u_ids) {
		return zzjgService.zwglUserAdd(j_id, u_ids);
	}
	
	@PostMapping("/zwgl/del")
	@ResponseBody
	public Integer zwglDel(String j_id) {
		return zzjgService.zwglDel(j_id);
	}
}