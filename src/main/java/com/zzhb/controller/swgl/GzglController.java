package com.zzhb.controller.swgl;

import com.alibaba.fastjson.JSONObject;
import com.zzhb.service.GzglService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Controller
@RequestMapping("/swgl/yzgl")
public class GzglController {

    @Autowired
    GzglService gzglService;


    @GetMapping("/gzgl")
    public String goGzglPage(){
        return "swgl/yzgl/gzgl";
    }



    @GetMapping("/stamperTypeMng")
    public String stamperTypeMng(){
        return "swgl/yzgl/stamperTypeMng";
    }

    @GetMapping("/gzlist")
    @ResponseBody
    public JSONObject getStamperList(Integer page, Integer limit, @RequestParam Map<String, String> params){
        return gzglService.getStamperList(page,limit,params);
    }

    @GetMapping("/getStamperType")
    @ResponseBody
    public JSONObject getStamperType(Integer page, Integer limit, @RequestParam Map<String, String> params){
        return gzglService.getStamperType(page,limit,params);
    }

    @GetMapping("/addStamperType")
    public ModelAndView addStamperType(@RequestParam Map<String, String> params){
        ModelAndView mv = new ModelAndView();
        mv.addObject("params",params);
        mv.setViewName("swgl/yzgl/addStamperType");
        return mv;
    }

    @PostMapping("/addNewStamperTypeInfo")
    @ResponseBody
    public Integer addNewStamperType(@RequestParam Map<String, String> params){
        return gzglService.addNewStamperType(params);
    }

    @PostMapping("/deleteStamperType")
    @ResponseBody
    public Integer deleteStamperType(@RequestParam Map<String, String> params){
        return gzglService.deleteStamperType(params);
    }

    @PostMapping("/editNewStamperTypeInfo")
    @ResponseBody
    public Integer editNewStamperTypeInfo(@RequestParam Map<String, String> params){
        return gzglService.editNewStamperTypeInfo(params);
    }
}
