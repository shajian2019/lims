package com.zzhb.zzoa.controller.xtgl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
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
	public Integer delUserById(@RequestParam Map<String, Object> map) {
		return userService.delUserById(map);
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

	@GetMapping("/zzjg/user/list")
	@ResponseBody
	public JSONObject zzjgUserList() {
		return zzjgService.zzjgList();
	}

	@PostMapping("/zzjg/add")
	@ResponseBody
	public Integer zzjgAdd(String groupName) {
		return zzjgService.zzjgAdd(groupName);
	}

	@PostMapping("/zzjg/edit")
	@ResponseBody
	public Integer zzjgEdit(String groupId, String groupName) {
		return zzjgService.zzjgEdit(groupId, groupName);
	}

	@PostMapping("/zzjg/del")
	@ResponseBody
	public Integer zzjgDel(String groupId) {
		return zzjgService.zzjgDel(groupId);
	}

}