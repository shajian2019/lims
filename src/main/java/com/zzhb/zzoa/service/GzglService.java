package com.zzhb.zzoa.service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zzhb.zzoa.mapper.GzglMapper;
import com.zzhb.zzoa.utils.LayUiUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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


}
