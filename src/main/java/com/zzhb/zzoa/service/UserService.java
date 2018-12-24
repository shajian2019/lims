package com.zzhb.zzoa.service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zzhb.zzoa.domain.User;
import com.zzhb.zzoa.mapper.UserMapper;
import com.zzhb.zzoa.shiro.ShiroRealm;
import com.zzhb.zzoa.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    UserMapper userMapper;

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

    public Integer delUserById(Map<String,Object> map){
        return userMapper.delUserById(map);
    }

    public Integer addUser(Map<String,Object> map){
        Object secretPass = (Object) ShiroRealm.md5Password(String.valueOf(map.get("username")),String.valueOf(map.get("passwprd")) );
        map.put("password",String.valueOf(secretPass));
        map.put("createtime",DateUtil.dateToString());
        map.put("status",3);
        return userMapper.addUser(map);
    }

    public Integer updateUser(Map<String,Object> map){
        map.put("updatetime",DateUtil.dateToString());
        return userMapper.updateUser(map);
    }
}
