package com.zzhb.zzoa.service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zzhb.zzoa.domain.User;
import com.zzhb.zzoa.mapper.RoleMapper;
import com.zzhb.zzoa.mapper.UserMapper;
import com.zzhb.zzoa.shiro.ShiroRealm;
import com.zzhb.zzoa.utils.DateUtil;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    RoleMapper roleMapper;

    public User getUser(String username){
        return userMapper.getUser(username);
    }

    public JSONObject getAllUsers(Integer page,Integer limit,Map<String, String> params){
        JSONObject result = new JSONObject();
        PageHelper.startPage(page,limit);
        List<User> userList = userMapper.getAllUsers(params);
        PageInfo<User> pageInfo = new PageInfo<User>(userList);
        result.put("code", 0);
        result.put("msg", "");
        result.put("count", pageInfo.getTotal());
        result.put("data", pageInfo.getList());
        return result;
    }
    @Transactional
    public Integer delUserById(Map<String,Object> map){
        return userMapper.delUserById(map);
    }

    @Transactional
    public Integer addUser(Map<String,String> map){
		// 盐值加密
		Object result = new SimpleHash("MD5", String.valueOf(map.get("password")), String.valueOf(map.get("username")), 1);
        map.put("password",String.valueOf(result));
        map.put("createtime",DateUtil.dateToString());
        if(map.get("role") == null || map.get("role").equals("")){
            map.put("status","3");
        }else{
            map.put("status","0");
        }
        return userMapper.addUser(map);
    }

    @Transactional
    public Integer updateUser(Map<String,String> map){
        map.put("updatetime",DateUtil.dateToString());
        return userMapper.updateUser(map);
    }

    @Transactional
    public Integer addUrole(Map<String,Integer> map){
        return userMapper.addUrole(map);
    }

}
