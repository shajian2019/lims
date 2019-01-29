package com.zzhb.zzoa.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Joiner;
import com.zzhb.zzoa.mapper.JournalMapper;
import com.zzhb.zzoa.mapper.UserMapper;
import com.zzhb.zzoa.utils.LayUiUtil;
import com.zzhb.zzoa.utils.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class JournalService {

    @Autowired
    JournalMapper journalMapper;

    @Autowired
    UserMapper userMapper;

    public JSONObject getMyJournalInfo(Integer page, Integer limit, Map<String, String> params) {
        PageHelper.startPage(page, limit);
        List<Map<String,String>> journalist = new ArrayList<>();
        //查询到不重复的uniqueid
        List<Map<String,String>> uniqueidList = journalMapper.getDistinctId();
        //根据uniqueid查到日志信息
        if(uniqueidList.size()>0){
            for(Map<String,String> uniqueIdMap:uniqueidList){
                String uniqueId = uniqueIdMap.get("uniqueid");
                Map<String,String> m = new HashMap<>();
                m.put("uniqueid",uniqueId);
                List<Map<String,String>> journalList = journalMapper.getDistinctJournal(m);
                Map<String,String> journal = journalList.get(0);
                journalist.add(journal);
            }
        }
        PageInfo<Map<String,String>> pageInfo = new PageInfo<Map<String,String>>(journalist);
        return LayUiUtil.pagination(pageInfo);
    }

    public Integer addNewJournal(Map<String,String> params){
        int result = 0;
        for(Map.Entry<String,String> entry:params.entrySet()){
            String key = entry.getKey();
            if(key.equals("receiver")){
                String recIds = entry.getValue();
                String[] recidsArr = recIds.split(",");
                String uuid = UUIDUtil.creatUUID();
                for(String recid:recidsArr){
                    Map<String,String> pmap = new HashMap<>();
                    pmap.putAll(params);
                    pmap.put("receiver",recid);
                    pmap.put("uniqueid",uuid);
                    result += journalMapper.addNewJournal(pmap);
                }
            }
        }
        return result;

    }

    public JSONObject getAllJournalInfo(Integer page, Integer limit, Map<String, String> params) {
        PageHelper.startPage(page, limit);
        List<Map<String,String>> jourlist = journalMapper.getReceiveJour(params);
        PageInfo<Map<String,String>> pageInfo = new PageInfo<Map<String,String>>(jourlist);
        return LayUiUtil.pagination(pageInfo);
//        return null;
    }

    public String getReceivers(String uniqueid){
        String result = "";
        Map<String,String> m = new HashMap<>();
        m.put("uniqueid",uniqueid);
        List<Map<String,String>> receids = journalMapper.getReceivers(m);
        for(Map<String,String> s : receids){
            s.put("u_id",s.get("receiver"));
            String name = journalMapper.getRecname(s);
            result += name+",";
        }
        return result.substring(0,result.length()-1);
    }
}
