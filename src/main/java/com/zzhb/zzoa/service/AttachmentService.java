package com.zzhb.zzoa.service;

import com.zzhb.zzoa.mapper.AttachmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class AttachmentService {
    @Autowired
    AttachmentMapper attachmentMapper;


    public Integer insertAttaInfo(Map<String,String> params){

        return attachmentMapper.insertAttaInfo(params);
    }

}
