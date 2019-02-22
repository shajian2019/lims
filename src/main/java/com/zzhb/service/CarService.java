package com.zzhb.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zzhb.domain.Car;
import com.zzhb.domain.CarStatusInfo;
import com.zzhb.domain.CarTypeInfo;
import com.zzhb.mapper.CarMapper;
import com.zzhb.utils.LayUiUtil;

@Service
public class CarService {

    @Autowired
    CarMapper carMapper;

    public JSONObject getAllCars(Integer page, Integer limit, Map<String, String> params) {
        PageHelper.startPage(page, limit);
        List<Car> carList = carMapper.getAllCars(params);
        PageInfo<Car> pageInfo = new PageInfo<Car>(carList);
        return LayUiUtil.pagination(pageInfo);
    }

    public Integer delCarById(Map<String, String> params){
        return carMapper.delCarById(params);
    }

    public JSONObject listCarType(Integer page, Integer limit, Map<String, String> params){
        PageHelper.startPage(page, limit);
        List<CarTypeInfo> typeList = carMapper.getCarType(params);
        PageInfo<CarTypeInfo> pageInfo = new PageInfo<CarTypeInfo>(typeList);
        return LayUiUtil.pagination(pageInfo);
    }

    public JSONObject listCarStatus(Integer page, Integer limit, Map<String, String> params){
        PageHelper.startPage(page, limit);
        List<CarStatusInfo> typeList = carMapper.getCarStatus(params);
        PageInfo<CarStatusInfo> pageInfo = new PageInfo<CarStatusInfo>(typeList);
        return LayUiUtil.pagination(pageInfo);
    }

    public Integer addCar(Map<String, String> params){
        return carMapper.addCar(params);
    }

    public Integer editCar(Map<String, String> params){
        return carMapper.editCar(params);
    }

    public JSONObject getCarUsageInfo(Integer page, Integer limit, Map<String, String> params){
        PageHelper.startPage(page, limit);
        List<Map<String,String>> typeList = carMapper.getCarUsageInfo(params);
        PageInfo<Map<String,String>> pageInfo = new PageInfo<Map<String,String>>(typeList);
        return LayUiUtil.pagination(pageInfo);
    }
}
