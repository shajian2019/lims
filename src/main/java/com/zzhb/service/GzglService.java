package com.zzhb.service;

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
        PageInfo<Map<String, String>> pageInfo = new PageInfo<Map<String, String>>(stamperList);
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


}
