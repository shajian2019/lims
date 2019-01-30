package com.zzhb.zzoa.mapper;

import java.util.List;
import java.util.Map;

public interface JournalMapper {


    public List<Map<String,String>> getMyJournalInfo(Map<String,String> map);

    public Integer addNewJournal(Map<String,String> map);

    public List<Map<String,String>> getDistinctId();

    public List<Map<String,String>> getDistinctJournal(Map<String,String> map);

    public List<Map<String,String>> getReceivers(Map<String,String> map);

    public List<Map<String,String>> getReceiveJour(Map<String,String> params);

    public String getRecname(Map<String,String> map);

    public List<String> getSubmit(Map<String,String> map);

    public List<String> getAttIds(Map<String,String> map);

    public List<Map<String,String>> getAtt(Map<String,String> map);
}
