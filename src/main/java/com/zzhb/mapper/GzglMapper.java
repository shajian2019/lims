package com.zzhb.mapper;

import java.util.List;
import java.util.Map;

public interface GzglMapper {

    public List<Map<String,String>> getStamperList(Map<String, String> params);

    public List<Map<String,String>> getStamperTypeList(Map<String, String> params);

    public Integer addNewStamperType(Map<String, String> params);

    public Integer deleteStamperType(Map<String, String> params);

    public Integer editNewStamperTypeInfo(Map<String, String> params);

    public Integer addNewStamperInfo(Map<String, String> params);

    public List<Map<String,String>> stamperUsageCount(Map<String, String> params);

    public Integer deleteStamper(Map<String, String> params);
    public Integer editStamperInfo(Map<String, String> params);

}
