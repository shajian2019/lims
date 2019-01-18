package com.zzhb.zzoa.domain;

public class Car {

    private Integer c_id;
    private String car_model;
    private String car_card;
    private String car_type;
    private Integer car_limit;
    private String buy_time;
    private String kilometres;
    private String next_inspection_time;
    private Integer car_status;

    public Integer getC_id() {
        return c_id;
    }

    public void setC_id(Integer c_id) {
        this.c_id = c_id;
    }

    public String getCar_model() {
        return car_model;
    }

    public void setCar_model(String car_model) {
        this.car_model = car_model;
    }

    public String getCar_card() {
        return car_card;
    }

    public void setCar_card(String car_card) {
        this.car_card = car_card;
    }

    public String getCar_type() {
        return car_type;
    }

    public void setCar_type(String car_type) {
        this.car_type = car_type;
    }

    public Integer getCar_limit() {
        return car_limit;
    }

    public void setCar_limit(Integer car_limit) {
        this.car_limit = car_limit;
    }

    public String getBuy_time() {
        return buy_time;
    }

    public void setBuy_time(String buy_time) {
        this.buy_time = buy_time;
    }

    public String getKilometres() {
        return kilometres;
    }

    public void setKilometres(String kilometres) {
        this.kilometres = kilometres;
    }

    public String getNext_inspection_time() {
        return next_inspection_time;
    }

    public void setNext_inspection_time(String next_inspection_time) {
        this.next_inspection_time = next_inspection_time;
    }

    public Integer getCar_status() {
        return car_status;
    }

    public void setCar_status(Integer car_status) {
        this.car_status = car_status;
    }
}
