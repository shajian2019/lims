package com.zzhb.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zzhb.mapper.GzglMapper;
import com.zzhb.utils.LayUiUtil;

@Service
public class GzglService {


    @Autowired
    GzglMapper gzglMapper;


    public JSONObject getStamperList(Integer page, Integer limit, Map<String, String> params){
        PageHelper.startPage(page, limit);
        List<Map<String,String>> stamperList = gzglMapper.getStamperList(params);
        List<Map<String,String>> stamperlist = new ArrayList<>();
        //根据印章类型查询到印章的类型名称
        for(Map<String,String> m : stamperList){
            Map<String,String> m1 = new HashMap<>();
            m1.put("id",m.get("stamper_type"));
            List<Map<String,String>> list = gzglMapper.getStamperTypeList(m1);
            String stamperTypeInfo = list.get(0).get("type_info");
            m.put("stamper_type_info",stamperTypeInfo);
            stamperlist.add(m);
        }

        PageInfo<Map<String, String>> pageInfo = new PageInfo<Map<String, String>>(stamperlist);
        return LayUiUtil.pagination(pageInfo);

    }

    public JSONObject getStamperType(Integer page, Integer limit, Map<String, String> params){
        PageHelper.startPage(page, limit);
        List<Map<String,String>> stamperTypeList = gzglMapper.getStamperTypeList(params);
        PageInfo<Map<String, String>> pageInfo = new PageInfo<Map<String, String>>(stamperTypeList);
        return LayUiUtil.pagination(pageInfo);

    }

    public Integer addNewStamperType(Map<String, String> params){
        return gzglMapper.addNewStamperType(params);
    }

    public Integer deleteStamperType(Map<String, String> params){
        return gzglMapper.deleteStamperType(params);
    }

    public Integer editNewStamperTypeInfo(Map<String, String> params){
        return gzglMapper.editNewStamperTypeInfo(params);
    }

    public Integer addNewStamperInfo(Map<String, String> params){
        return gzglMapper.addNewStamperInfo(params);
    }


}
