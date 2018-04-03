package com.d1m.wechat.dto;

import java.util.List;

/**
 * Created by D1M on 2017/5/3.
 */
public class PlaceBusinesssDto {
    private String name;
    private List<PlaceBusinesssDto> cityList;
    private List<PlaceBusinesssDto> businessList;
    private Integer storeId;
    private String storeName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PlaceBusinesssDto> getCityList() {
        return cityList;
    }

    public void setCityList(List<PlaceBusinesssDto> cityList) {
        this.cityList = cityList;
    }

    public List<PlaceBusinesssDto> getBusinessList() {
        return businessList;
    }

    public void setBusinessList(List<PlaceBusinesssDto> businessList) {
        this.businessList = businessList;
    }

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
}
