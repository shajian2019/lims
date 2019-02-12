package com.zzhb.zzoa.controller.swgl;

import com.alibaba.fastjson.JSONObject;
import com.zzhb.zzoa.service.ZdgzService;
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

}
