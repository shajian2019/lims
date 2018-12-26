package com.zzhb.zzoa.controller.xtgl;

import java.util.HashMap;
import java.util.Map;

import com.zzhb.zzoa.utils.Constant;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
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

import javax.servlet.http.HttpSession;

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
	@Autowired
	HttpSession session;

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
	public Integer addUser(User user, String role,String flag) {
		return userService.addUser(user, role,flag);
	}

	@PostMapping("/yhgl/updateUser")
	@ResponseBody
	public Integer updateUser(@RequestParam Map<String, Object> map) {
		if(map.containsKey("flag") && map.get("flag").equals("grzx")){//个人中心修改信息,需要刷新session
			User user = mapToUser(map);
			session.setAttribute("user",user);
		}
		return userService.updateUser(map);
	}

	@GetMapping("/yhgl/getAllRole")
	@ResponseBody
	public JSONObject getAllRole(@RequestParam Map<String, String> map) {
		return roleService.listRoles(1, Integer.MAX_VALUE, map);
	}

	@PostMapping("/yhgl/resetPassword")
	@ResponseBody
	public Integer resetPass(@RequestParam Map<String, String> map){
		return userService.resetPass(map);
	}

	@PostMapping("/yhgl/getUserByName")
	@ResponseBody
	public Integer getUserByName(@RequestParam("username") String username){
		return userService.getAllUname(username);
	}

	@GetMapping("/yhgl/editPassPage")
	public String goEditPassPage(){
		return "grzx/xgmm";
	}

	@PostMapping("yhgl/checkOldPass")
	@ResponseBody
	public Integer checkOldPass(@RequestParam Map<String,String> map){
		User user = userMapper.getUser(map.get("username"));
		String oldPass = map.get("password");
		return userService.checkOldPass(user,oldPass);
	}

	public static User mapToUser(Map<String,Object> map){
		User user = new User();
		user.setU_id(Integer.parseInt(String.valueOf(map.get("u_id"))));
		user.setUsername((String) map.get("username"));
		user.setPassword((String) map.get("password"));
		user.setNickname((String) map.get("nickname"));
		user.setCreatetime((String) map.get("createtime"));
		user.setUpdatetime((String) map.get("updatetime"));
		user.setRemark((String) map.get("remark"));
		user.setEmail((String) map.get("email"));
		user.setPhone((String) map.get("phone"));
		user.setStatus((String) map.get("status"));
		user.setRecentlogin((String) map.get("recentlogin"));
		return user;
	}

}
