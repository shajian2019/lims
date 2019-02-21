package com.zzhb.zzoa.mapper;

import java.util.List;
import java.util.Map;

public interface GzglMapper {

    public List<Map<String,String>> getStamperList(Map<String, String> params);

    public List<Map<String,String>> getStamperTypeList(Map<String, String> params);

}
