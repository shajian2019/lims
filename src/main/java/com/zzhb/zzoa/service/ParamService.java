package com.zzhb.zzoa.service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zzhb.zzoa.domain.common.Param;
import com.zzhb.zzoa.mapper.ParamMapper;
import com.zzhb.zzoa.utils.LayUiUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Service
public class ParamService {

    @Autowired
    ParamMapper paramMapper;

    public JSONObject getAllParams(Integer page, Integer limit, @RequestParam Map<String, String> params){
        PageHelper.startPage(page, limit);
        List<Param> paramList = paramMapper.getAllParams(params);
        PageInfo<Param> pageInfo = new PageInfo<Param>(paramList);
        return LayUiUtil.pagination(pageInfo);
    }

    public Integer saveParam(Param param,String flag){
        Integer result = 0;
        Integer checkKey = paramMapper.checkKey(param);
        if(checkKey == 0){
            if(flag.equals("edit")){//修改
                result = paramMapper.updateParam(param);

            }else{//新增
                result = paramMapper.addNewParam(param);
            }
        }else {
            result = -1;
        }

        return result;
    }

}
