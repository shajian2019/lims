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
    public String goRegisterPage() {
        return "swgl/zdgz/zdgzdj";
    }

    @GetMapping("/zdgzgl")
    @ResponseBody
    public JSONObject getWork(Integer page, Integer limit, @RequestParam Map<String, String> params) {
        System.out.println(zdgzService.getAllZdgz(page, limit, params).toString());
        return zdgzService.getAllZdgz(page, limit, params);
    }

    @GetMapping("/addZdgz")
    public ModelAndView goAddPage() {
        ModelAndView mv = new ModelAndView();
        mv.addObject("key", "zdgzsp");
        mv.addObject("formkey", "zdgzsp");
        mv.setViewName("swgl/zdgz/add");
        return mv;
    }

    @PostMapping("/addNewZdgz")
    @ResponseBody
    public Integer addNewZdgz(@RequestParam Map<String, String> params) {
        return zdgzService.addZdgz(params);
    }

    @GetMapping("/zdgzdetail")
    public ModelAndView detailPage(@RequestParam Map<String, String> params) {
        ModelAndView mv = new ModelAndView();
        String nameResult = zdgzService.getAuditterNames(params);
        params.put("auditterNames", nameResult);
        mv.addObject("params", params);
        mv.setViewName("swgl/zdgz/detail");
        return mv;
    }

    @PostMapping("/getAttachments")
    @ResponseBody
    public JSONObject getAttach(@RequestParam Map<String, String> params) {
        return zdgzService.getAttachments(1, Integer.MAX_VALUE, params);
    }

    @GetMapping("/ndzdgzsh")
    public String zdgzsh() {
        return "swgl/zdgz/approval";
    }

    @GetMapping("/zdgzsh")
    @ResponseBody
    public JSONObject getZdgzsh(Integer page, Integer limit, @RequestParam Map<String, String> params) {
        return zdgzService.getZdgzsh(page, limit, params);
    }

    @PostMapping("/shresult")
    @ResponseBody
    public Integer getShResult(@RequestParam Map<String, String> params) {
        return zdgzService.getShResult(params);
    }

    @GetMapping("/zdgzshpage")
    public ModelAndView goShPage(@RequestParam Map<String, String> params) {
        ModelAndView mv = new ModelAndView();
        String nameResult = zdgzService.getAuditterNames(params);
        params.put("auditterNames", nameResult);
        mv.addObject("params", params);
        mv.setViewName("swgl/zdgz/approvalPage");
        return mv;
    }

    @GetMapping("/ndzdgzjztx")
    public String goJztxPage(){
        return "swgl/zdgz/ndzdgzjztx";
    }

    @GetMapping("/zdgzglysh")
    @ResponseBody
    public JSONObject getzdgzysh(Integer page, Integer limit, @RequestParam Map<String, String> params){
        return zdgzService.getzdgzysh(page,limit,params);
    }

    @PostMapping("/addprincipal")
    @ResponseBody
    public Integer addprincipal(@RequestParam Map<String, String> params) {
        return zdgzService.addPrincipal(params);
    }

    @GetMapping("/zdgzjz")
    public ModelAndView goJzPage(@RequestParam Map<String, String> params){
        ModelAndView mv = new ModelAndView();
        mv.addObject("params",params);
        mv.setViewName("swgl/zdgz/jzlist");//进展列表
        return mv;
    }

    @GetMapping("/zdgzjzlist")
    @ResponseBody
    public JSONObject zdgzjzlist(Integer page, Integer limit,@RequestParam Map<String, String> params){
        return zdgzService.getZdgzjzList(page,limit,params);
    }

    @GetMapping("/addZdgzjz")
    public ModelAndView addZdgzjz(@RequestParam Map<String, String> params){
        ModelAndView mv = new ModelAndView();
        mv.addObject("params",params);
        mv.setViewName("swgl/zdgz/jzpage");
        return mv;
    }

    @PostMapping("/addzdgzjz")
    @ResponseBody
    public Integer addZdgzjz1(@RequestParam Map<String, String> params){
        return zdgzService.addZdgzjz(params);
    }

}
