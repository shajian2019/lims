package com.zzhb.zzoa.service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zzhb.zzoa.domain.Car;
import com.zzhb.zzoa.mapper.JournalMapper;
import com.zzhb.zzoa.utils.LayUiUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class JournalService {

    @Autowired
    JournalMapper journalMapper;


    public JSONObject getMyJournalInfo(Integer page, Integer limit, Map<String, String> params) {
        PageHelper.startPage(page, limit);
        List<Map<String,String>> carList = journalMapper.getMyJournalInfo(params);
        PageInfo<Map<String,String>> pageInfo = new PageInfo<Map<String,String>>(carList);
        return LayUiUtil.pagination(pageInfo);
    }
}
