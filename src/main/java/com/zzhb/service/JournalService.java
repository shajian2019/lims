package com.zzhb.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zzhb.mapper.JournalMapper;
import com.zzhb.mapper.UserMapper;
import com.zzhb.utils.LayUiUtil;
import com.zzhb.utils.UUIDUtil;

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
    public String getSubmitter(String uniqueid){
        String result = "";
        Map<String,String> umap = new HashMap<>();
        umap.put("uniqueid",uniqueid);
        List<String> m = journalMapper.getSubmit(umap);
        if(m.size() > 0){
            result = m.get(0);

        }
        return result;
    }

    public JSONObject getAttachments(Integer page, Integer limit,Map<String, String> params){
        PageHelper.startPage(page, limit);
        List<String> attIDs = journalMapper.getAttIds(params);
        attIDs.removeAll(Collections.singleton(null));
        List<Map<String,String>> attList = new ArrayList<>();
        if(attIDs.size() > 0 && attIDs != null){
            String attID = attIDs.get(0);
            String[] attIdArr = attID.split(",");
            if(attIdArr.length>0){
                for(String s:attIdArr){
                    if(s != null && !"".equals(s)){
                        Map<String,String> m = new HashMap<>();
                        m.put("attach_id",s);
                        List<Map<String,String>> attachment = journalMapper.getAtt(m);
                        attList.addAll(attachment);
                    }

                }
                PageInfo<Map<String,String>> pageInfo = new PageInfo<Map<String,String>>(attList);
                return LayUiUtil.pagination(pageInfo);
            }
        }else{
            return null;
        }
        return null;
    }

    public JSONObject getRzyb(Integer page, Integer limit, Map<String, String> params) {
        PageHelper.startPage(page, limit);
        String mouth = "";
        if(!params.containsKey("subtime") || params.get("subtime").equals("") || params.get("subtime") == null){
            mouth = getMouth();//获取到2019-01这种类型的数据
            params.put("subtime",mouth);
        }
        List<Map<String,String>> ybList = journalMapper.getRzyb(params);
        PageInfo<Map<String,String>> pageInfo = new PageInfo<Map<String,String>>(ybList);
        return LayUiUtil.pagination(pageInfo);
    }

    public String getMouth() {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(d);
        return date.substring(0,7);
    }
}
