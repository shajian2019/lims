package com.zzhb.mapper;

import java.util.List;
import java.util.Map;

import com.zzhb.domain.Car;
import com.zzhb.domain.CarStatusInfo;
import com.zzhb.domain.CarTypeInfo;

public interface CarMapper {

    public List<Car> getAllCars(Map<String, String> params);

    public Integer delCarById(Map<String, String> params);

    public List<CarTypeInfo> getCarType(Map<String, String> params);
    public List<CarStatusInfo> getCarStatus(Map<String, String> params);

    public Integer addCar(Map<String, String> params);

    public Integer editCar(Map<String, String> params);

    public List<Map<String, String>> getCarUsageInfo(Map<String, String> params);
}
