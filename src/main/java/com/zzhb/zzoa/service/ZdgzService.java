package com.zzhb.zzoa.service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zzhb.zzoa.mapper.JournalMapper;
import com.zzhb.zzoa.mapper.ZdgzMapper;
import com.zzhb.zzoa.utils.LayUiUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ZdgzService {

    @Autowired
    ZdgzMapper zdgzMapper;
    @Autowired
    JournalMapper journalMapper;

    public JSONObject getAllZdgz(Integer page, Integer limit, Map<String, String> params) {
        PageHelper.startPage(page, limit);
        List<Map<String, String>> zdgzList = zdgzMapper.getAllZdgz(params);
        List<Map<String, String>> zdgzlist = new ArrayList<>();
        //对重点工作中的是否审批和是否分发进行判断，插入信息
        for(Map<String,String> m:zdgzList){
            if(Integer.parseInt(String.valueOf(m.get("audit_state"))) == 0){
                m.put("audit_state_info","未审核");
            }else if(m.get("audit_state").equals("1")){
                m.put("audit_state_info","审核同意");
            }else{
                m.put("audit_state_info","审核不同意");
            }

            if(Integer.parseInt(String.valueOf(m.get("distribution_state"))) == 0){
                m.put("distribution_state_info","未指派");
            }else{
                m.put("distribution_state_info","已指派");
            }

            zdgzlist.add(m);
        }
        PageInfo<Map<String, String>> pageInfo = new PageInfo<Map<String, String>>(zdgzlist);
        return LayUiUtil.pagination(pageInfo);
    }

    public Integer addZdgz(Map<String, String> params){
        return zdgzMapper.addZdgz(params);
    }

    public JSONObject getAttachments(Integer page, Integer limit, Map<String, String> params){
        PageHelper.startPage(page, limit);
        List<Map<String,String>> attList = new ArrayList<>();
        if(params.containsKey("attach_id") && params.get("attach_id") != null && !"".equals(params.get("attach_id"))){//附件id中间带逗号
            String attids = params.get("attach_id");
            String[] attidsArr = attids.split(",");
            for(String s:attidsArr){
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
        return null;
    }
}
