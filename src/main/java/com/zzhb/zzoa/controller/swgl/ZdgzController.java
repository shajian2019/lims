package com.zzhb.zzoa.controller.swgl;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/swgl/zdgz")
public class ZdgzController {


    @GetMapping("/ndzdgzdj")
    public String goRegisterPage(){
        return "swgl/zdgz/zdgzdj";
    }

}
