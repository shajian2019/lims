package com.zzhb.controller.swgl;

import com.alibaba.fastjson.JSONObject;
import com.zzhb.domain.Car;
import com.zzhb.mapper.CarMapper;
import com.zzhb.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Controller
@RequestMapping("/swgl/clgl")
public class ClglController {

    @Autowired
    CarService carService;

    @Autowired
    CarMapper carMapper;

    @RequestMapping("/clgl")
    public String goClglPage(){
        return "swgl/clgl/clgl";
    }

    @GetMapping("/getAllCars")
    @ResponseBody
    public JSONObject getCarInfo(Integer page, Integer limit, @RequestParam Map<String, String> params){
        return carService.getAllCars(page,limit,params);
    }

    @PostMapping("/delCar")
    @ResponseBody
    public Integer delCarById(@RequestParam Map<String, String> params){
        return carService.delCarById(params);
    }

    @GetMapping("/editCarPage")
    @ResponseBody
    public ModelAndView editCar(@RequestParam Map<String, String> params){
        ModelAndView mv = new ModelAndView();
        if(params.get("flag").equals("edit")){//进入编辑页面
            mv.addObject("map",params);
            String c_id = params.get("c_id");
            Car c = carMapper.getAllCars(params).get(0);
            mv.addObject("car",c);
            mv.setViewName("swgl/clgl/edit");
        }else{//进入新增页面
            mv.addObject("map",params);
            mv.setViewName("swgl/clgl/edit");
        }
        return mv;
    }

    @GetMapping("/listcartype")
    @ResponseBody
    public JSONObject listCarType(@RequestParam Map<String, String> map){
        return carService.listCarType(1, Integer.MAX_VALUE, map);
    }

    @GetMapping("/getcarstatus")
    @ResponseBody
    public JSONObject listCarStatus(@RequestParam Map<String, String> map){
        return carService.listCarStatus(1, Integer.MAX_VALUE, map);
    }


    @PostMapping("/editCar")
    @ResponseBody
    public Integer editOrAddCar(@RequestParam Map<String, String> map){
        Integer result = 0;
        if(map.get("flag").equals("add")){
            result = carService.addCar(map);
        }else{
            result = carService.editCar(map);
        }
        return result;
    }

    @GetMapping("/clsytj")
    @ResponseBody
    public ModelAndView getCarUsage(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("swgl/clgl/clsytj/clsytj");
        return mv;
    }

    @GetMapping("/carUsageInfo")
    @ResponseBody
    public JSONObject getCarUsageInfo(Integer page, Integer limit,@RequestParam Map<String, String> map){
        return carService.getCarUsageInfo(page,limit,map);
    }

}
