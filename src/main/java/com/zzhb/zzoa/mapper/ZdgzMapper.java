package com.zzhb.zzoa.mapper;

import java.util.List;
import java.util.Map;

public interface ZdgzMapper {

    public List<Map<String,String>> getAllZdgz(Map<String, String> params);

    public Integer addZdgz(Map<String, String> params);

    public List<Map<String,String>> getzdgzsh(Map<String, String> params);
}
