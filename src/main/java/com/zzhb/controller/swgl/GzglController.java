package com.zzhb.controller.swgl;

import com.alibaba.fastjson.JSON;
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

    @PostMapping("/addNewStamperType")
    @ResponseBody
    public Integer addNewStamperType(@RequestParam Map<String, String> params){
        return gzglService.addNewStamperType(params);
//        return 1;
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

    @GetMapping("/addNewStamper")
    public ModelAndView addNewStamperPage(@RequestParam Map<String, String> params){
        ModelAndView mv = new ModelAndView();
        mv.addObject("params",params);
        mv.setViewName("swgl/yzgl/addNewStamper");
        return mv;
    }

    @GetMapping("/stampertypeselect")
    @ResponseBody
    public JSONObject stamperTypeSelect(@RequestParam Map<String, String> params){
        return gzglService.getStamperType(1,Integer.MAX_VALUE,params);
    }


    @PostMapping("/addNewStamperInfo")
    @ResponseBody
    public Integer addNewStamperInfo(@RequestParam Map<String, String> params){
        return gzglService.addNewStamperInfo(params);
    }

    @GetMapping("/yzsytj")
    public String goTjPage(){
        return "swgl/yzgl/yzshtj";
    }

    @GetMapping("/stamperUsageCount")
    @ResponseBody
    public JSONObject stamperUsageCount(Integer page, Integer limit, @RequestParam Map<String, String> params){
        return gzglService.stamperUsageCount(page,limit,params);
    }

    @PostMapping("/deleteStamper")
    @ResponseBody
    public Integer deleteStamper(@RequestParam Map<String, String> params){
        return gzglService.deleteStamper(params);
    }

    @PostMapping("/editStamperInfo")
    @ResponseBody
    public Integer editStamperInfo(@RequestParam Map<String, String> params){
        return gzglService.editStamperInfo(params);
    }

}
