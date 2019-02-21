package com.zzhb.controller.swgl;

import com.alibaba.fastjson.JSONObject;
import com.zzhb.service.GzglService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
    public String addStamperType(){
        return "swgl/yzgl/addStamperType";
    }

    @PostMapping("/addNewStamperType")
    @ResponseBody
    public Integer addNewStamperType(@RequestParam Map<String, String> params){
//        return gzglService.addNewStamperType(params);
        return null;

    }

}
