package com.zzhb.zzoa.service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zzhb.zzoa.mapper.ZdgzMapper;
import com.zzhb.zzoa.utils.LayUiUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ZdgzService {

    @Autowired
    ZdgzMapper zdgzMapper;

    public JSONObject getAllZdgz(Integer page, Integer limit, Map<String, String> params) {
        PageHelper.startPage(page, limit);
        List<Map<String, String>> zdgzList = zdgzMapper.getAllZdgz(params);
        PageInfo<Map<String, String>> pageInfo = new PageInfo<Map<String, String>>(zdgzList);
        return LayUiUtil.pagination(pageInfo);
    }

    public Integer addZdgz(Map<String, String> params){
        return zdgzMapper.addZdgz(params);
    }
}
