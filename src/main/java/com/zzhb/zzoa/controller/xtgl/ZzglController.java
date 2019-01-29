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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
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

	@GetMapping("/yhgl2")
	public String yhgl2() {
		return "xtgl/zzgl/yhgl/yhgl2";
	}

	@GetMapping("/yhgl/getAllUsers")
	@ResponseBody
	public JSONObject getAllUsers(Integer page, Integer limit, @RequestParam Map<String, Object> params) {
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
	public Integer updateUser(@RequestParam Map<String, String> map) {
		return userService.updateUser(map);
	}

	@GetMapping("/yhgl/getRoleSelect")
	@ResponseBody
	public JSONObject getRoleSelect(@RequestParam Map<String, String> map) {
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

	@GetMapping("/zzjg/ztree/list")
	@ResponseBody
	public JSONArray zzjgZtreeList() {
		return zzjgService.zzjgZtreeList();
	}

	// 组织架构查询
	@GetMapping("/zzjg/list")
	@ResponseBody
	public JSONObject zzjgList(@RequestParam Map<String, String> params) {
		return zzjgService.zzjgList(params);
	}

	// 组织架构新增或修改
	@PostMapping("/zzjg/saveOrUpdate")
	@ResponseBody
	public Integer saveOrUpdate(Org org) {
		return zzjgService.zzjgAdd(org);
	}

	// 组织架构排序修改
	@PostMapping("/zzjg/updateSort")
	@ResponseBody
	public Integer updateZzjgSort(@RequestParam("ids") String ids) {
		JSONArray array = JSON.parseArray(ids);
		System.out.println(array);
		return zzjgService.updateZzjgSort(array);
	}

	// 组织架构删除
	@PostMapping("/zzjg/del")
	@ResponseBody
	public Integer zzjgDel(Org org) {
		return zzjgService.zzjgDel(org);
	}

	@GetMapping("/zwgl")
	public String zwgl() {
		return "xtgl/zzgl/zwgl/zwgl";
	}

	@GetMapping("/zwgl/list")
	@ResponseBody
	public JSONObject zwglList(Integer page, Integer limit, @RequestParam Map<String, String> params) {
		return zzjgService.zwglList(page, limit, params);
	}

	@GetMapping("/zwgl/ztree/list")
	@ResponseBody
	public JSONArray zwglZtreeList() {
		return zzjgService.zwglZtreeList();
	}

	@GetMapping("/zwgl/getJobSelect")
	@ResponseBody
	public JSONObject getJobSelect(@RequestParam Map<String, String> map) {
		return zzjgService.zwglList(1, Integer.MAX_VALUE, map);
	}

	@PostMapping("/zwgl/addOrUpdate")
	@ResponseBody
	public Integer zwglAddOrUpdate(Job job) {
		return zzjgService.zwglAddOrUpdate(job);
	}

	@PostMapping("/zwgl/edit")
	@ResponseBody
	public Integer zwglEdit(Job job) {
		return zzjgService.zwglEdit(job);
	}

	@PostMapping("/zwgl/del")
	@ResponseBody
	public Integer zwglDel(Job job) {
		return zzjgService.zwglDel(job);
	}

	@PostMapping("/zwgl/updateSort")
	@ResponseBody
	public Integer updateZwglSort(@RequestParam("ids") String ids) {
		JSONArray array = JSON.parseArray(ids);
		System.out.println(array);
		return zzjgService.updateZwglSort(array);
	}
}