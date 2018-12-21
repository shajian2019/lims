package com.zzhb.zzoa.service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zzhb.zzoa.domain.User;
import com.zzhb.zzoa.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public void delUserById(Integer id){
        userMapper.delUserById(id);
    }

    public void addUser(User user){

        userMapper.addUser(user);
    }
}
