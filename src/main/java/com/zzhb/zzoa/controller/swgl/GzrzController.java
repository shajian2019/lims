package com.zzhb.zzoa.controller.swgl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zzhb.zzoa.domain.User;
import com.zzhb.zzoa.service.JournalService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Controller
@RequestMapping("/swgl/gzrz")
public class GzrzController {

    @Autowired
    JournalService journalService;
    /**
    跳转到我的日志页面
     */
    @GetMapping("/wdrz")
    public String myJournalPage(){
        return "swgl/gzrz/wdrz";
    }

    /**
     * 获取我的日志信息
     * @return
     */
    @GetMapping("/getMyJournalInfo")
    @ResponseBody
    public JSONObject getMyJournalInfo(Integer page, Integer limit, @RequestParam Map<String, String> params){
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        JSONObject j  = (JSONObject) JSON.toJSON(session.getAttribute("user"));
        String u_id = String.valueOf(j.getString("u_id"));
        params.put("submitter",u_id);
        return journalService.getMyJournalInfo(page,limit,params);
    }

    /**
     * 进入添加新日志页面
     * @return
     */
    @GetMapping("/addMyJournal")
    public ModelAndView goAddJournalPage(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("swgl/gzrz/addMyJournal");
        return mv;
    }

    /**
     *进入全局日志
     */
    @GetMapping("/qjrz")
    public String allJournalPage(){
        return "swgl/gzrz/qjrz";
    }

    /**
     * 获取所有日志
     */
    @GetMapping("/getAllJournalInfo")
    @ResponseBody
    public JSONObject getAllJournalInfo(Integer page, Integer limit, @RequestParam Map<String, String> params){
        return journalService.getMyJournalInfo(page,limit,params);
    }

    /**
     *进入日志月报页面
     */
    @GetMapping("/rzyb")
    public String journalPage(){
        return "swgl/gzrz/rzyb";
    }

    @GetMapping("/empTree")
    public ModelAndView goPopPage(@RequestParam Map<String, String> params){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("swgl/gzrz/pop");
        mv.addObject("key",params.get("key"));
        mv.addObject("formkey",params.get("formkey"));
        return mv;
    }

}
