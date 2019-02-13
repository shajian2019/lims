package com.zzhb.zzoa.controller.swgl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zzhb.zzoa.service.ZdgzService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Controller
@RequestMapping("/swgl/zdgz")
public class ZdgzController {

    @Autowired
    ZdgzService zdgzService;

    @GetMapping("/ndzdgzdj")
    public String goRegisterPage(){
        return "swgl/zdgz/zdgzdj";
    }

    @GetMapping("/zdgzgl")
    @ResponseBody
    public JSONObject getWork(Integer page, Integer limit, @RequestParam Map<String, String> params){
        return zdgzService.getAllZdgz(page,limit,params);
    }

    @GetMapping("/addZdgz")
    public ModelAndView goAddPage(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("swgl/zdgz/add");
        return mv;
    }
    @PostMapping("/addNewZdgz")
    @ResponseBody
    public Integer addNewZdgz(@RequestParam Map<String, String> params){
        return zdgzService.addZdgz(params);
    }

    @GetMapping("/zdgzdetail")
    public ModelAndView detailPage(@RequestParam Map<String, String> params){
        ModelAndView mv = new ModelAndView();
        mv.addObject("params",params);
        mv.setViewName("swgl/zdgz/detail");
        return mv;
    }

    @PostMapping("/getAttachments")
    @ResponseBody
    public JSONObject getAttach(@RequestParam Map<String, String> params){

        return zdgzService.getAttachments(1,Integer.MAX_VALUE,params);
    }

    @GetMapping("/zdgzsh")
    public void zdgzsh(){

    }

}
