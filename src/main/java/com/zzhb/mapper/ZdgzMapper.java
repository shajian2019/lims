package com.zzhb.mapper;

import java.util.List;
import java.util.Map;

public interface ZdgzMapper {

    public List<Map<String,String>> getAllZdgz(Map<String, String> params);

    public Integer addZdgz(Map<String, String> params);

    public List<Map<String,String>> getzdgzsh(Map<String, String> params);

    public Integer getShResult(Map<String, String> params);

    public List<Map<String,String>> getzdgzysh(Map<String, String> params);

    public List<Map<String,String>> getzdgzyjz(Map<String, String> params);

    public Integer addZdgzjz(Map<String, String> params);

}