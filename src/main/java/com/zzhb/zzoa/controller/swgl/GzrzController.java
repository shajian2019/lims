package com.zzhb.zzoa.controller.swgl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zzhb.zzoa.domain.User;
import com.zzhb.zzoa.mapper.JournalMapper;
import com.zzhb.zzoa.service.JournalService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/swgl/gzrz")
public class GzrzController {

    @Autowired
    JournalService journalService;

    @Autowired
    JournalMapper journalMapper;
    /**
    跳转到我的日志页面
     */
    @GetMapping("/wdrz")
    public ModelAndView myJournalPage(){
        ModelAndView mv = new ModelAndView();
        mv.addObject("flag","wdrz");
        mv.setViewName("swgl/gzrz/wdrz");
        return mv;
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
    public ModelAndView allJournalPage(){
        ModelAndView mv = new ModelAndView();
        mv.addObject("flag","qjrz");
        mv.setViewName("swgl/gzrz/qjrz");
        return mv;
    }

    /**
     * 获取所有提交给当前登录人员的日志
     */
    @GetMapping("/getAllJournalInfo")
    @ResponseBody
    public JSONObject getAllJournalInfo(Integer page, Integer limit, @RequestParam Map<String, String> params){
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        JSONObject j  = (JSONObject) JSON.toJSON(session.getAttribute("user"));
        String u_id = String.valueOf(j.getString("u_id"));
        params.put("receiver",u_id);
        return journalService.getAllJournalInfo(page,limit,params);
    }

    /**
     *进入日志月报页面
     */
    @GetMapping("/rzyb")
    public String journalPage(){
        return "swgl/gzrz/rzyb";
    }

    /**
     * 插入日志
     */
    @PostMapping("/addJournal")
    @ResponseBody
    public Integer addNewJournal(@RequestParam Map<String, String> params){
        return journalService.addNewJournal(params);
    }

    /**
     * 日志详情页面
     */
    @GetMapping("/journalDetail")
    public ModelAndView journalDetail(@RequestParam Map<String, String> params){
        System.out.println(JSON.toJSONString(params));
        ModelAndView mv = new ModelAndView();
        String flag = params.get("flag");
        if(flag.equals("wdrz")){
            //我的日志,处理好接收人,根据uniqueid查到接收人
            String receivers = journalService.getReceivers(params.get("uniqueid"));
            params.put("receiver",receivers);
        }else{
            //全局日志,处理好提交人，根据uniqueid查到提交人
            Map<String,String> map = new HashMap<>();
            map.put("u_id",params.get("uniqueid"));
            String submit = journalService.getSubmitter(params.get("uniqueid"));
            params.put("submitter",submit);
        }
        mv.addObject("params",params);
        mv.setViewName("swgl/gzrz/detail");
        return mv;
    }
}
