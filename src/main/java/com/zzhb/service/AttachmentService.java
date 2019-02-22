package com.zzhb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zzhb.mapper.AttachmentMapper;

import java.util.*;

@Service
public class AttachmentService {
    @Autowired
    AttachmentMapper attachmentMapper;


    public Integer insertAttaInfo(Map<String,String> params){

        return attachmentMapper.insertAttaInfo(params);
    }

}
