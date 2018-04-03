package com.d1m.wechat.service.impl;

import static com.d1m.wechat.util.IllegalArgumentUtil.notBlank;

import java.util.*;

import com.alibaba.fastjson.JSONObject;
import com.d1m.wechat.controller.menugroup.MenuGroupController;
import com.d1m.wechat.dto.BusinessAreaListDto;
import com.d1m.wechat.dto.PlaceBusinesssDto;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tk.mybatis.mapper.common.Mapper;

import com.d1m.wechat.dto.BusinessDto;
import com.d1m.wechat.exception.WechatException;
import com.d1m.wechat.mapper.AreaInfoMapper;
import com.d1m.wechat.mapper.BusinessMapper;
import com.d1m.wechat.mapper.BusinessPhotoMapper;
import com.d1m.wechat.mapper.BusinessResultMapper;
import com.d1m.wechat.model.Business;
import com.d1m.wechat.model.BusinessPhoto;
import com.d1m.wechat.model.BusinessResult;
import com.d1m.wechat.model.User;
import com.d1m.wechat.model.enums.BusinessStatus;
import com.d1m.wechat.pamametermodel.BusinessModel;
import com.d1m.wechat.service.AreaInfoService;
import com.d1m.wechat.service.BusinessService;
import com.d1m.wechat.util.BaiduLocationUtil;
import com.d1m.wechat.util.Message;
import com.d1m.wechat.wxsdk.wxshop.JwShopAPI;
import com.d1m.wechat.wxsdk.wxshop.model.BaseInfo;
import com.d1m.wechat.wxsdk.wxshop.model.Photo;
import com.d1m.wechat.wxsdk.wxshop.model.PoiId;
import com.d1m.wechat.wxsdk.wxshop.model.ShopRtnInfo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

@Service
public class BusinessServiceImpl extends BaseService<Business> implements
        BusinessService {

    @Autowired
    private BusinessMapper businessMapper;

    @Autowired
    private BusinessPhotoMapper businessPhotoMapper;

    @Autowired
    private AreaInfoService areaInfoService;

    @Autowired
    private BusinessResultMapper businessResultMapper;

    @Autowired
    private AreaInfoMapper areaInfoMapper;

    @Autowired
    private BaiduLocationUtil baiduLocationUtil;

    public void setBusinessMapper(BusinessMapper businessMapper) {
        this.businessMapper = businessMapper;
    }

    private Logger log = LoggerFactory.getLogger(BusinessServiceImpl.class);

    @Override
    public Mapper<Business> getGenericMapper() {
        return businessMapper;
    }

    @Override
    public Business create(Integer wechatId, User user, BusinessModel model)
            throws WechatException {
        notBlank(model.getBusinessName(), Message.BUSINESS_NAME_NOT_BLANK);
        notBlank(model.getAddress(), Message.BUSINESS_ADDRESS_NOT_BLANK);
        notBlank(model.getTelephone(), Message.BUSINESS_TELEPHONE_NOT_BLANK);

        if (StringUtils.isNotBlank(model.getBranchName())
                && StringUtils.equals(model.getBusinessName(),
                model.getBranchName())) {
            throw new WechatException(
                    Message.BUSINESS_NAME_NOT_EQUALS_BRANCH_NAME);
        }
        Business exist = getByBusinessName(wechatId, model.getBusinessName());
        if (exist != null) {
            throw new WechatException(Message.BUSINESS_NAME_EXIST);
        }

        Business business = new Business();
        business.setWechatId(wechatId);
        business.setBusinessName(model.getBusinessName());
        business.setBranchName(model.getBranchName());
        business.setProvince(model.getProvince());
        business.setCity(model.getCity());
        business.setDistrict(model.getDistrict());
        business.setAddress(model.getAddress());
        business.setTelephone(model.getTelephone());
        business.setLongitude(model.getLongitude());
        business.setLatitude(model.getLatitude());
        business.setRecommend(model.getRecommend());
        business.setSpecial(model.getSpecial());
        business.setIntroduction(model.getIntroduction());
        business.setOpenTime(model.getOpenStartTime() + "-"
                + model.getOpenEndTime());
        business.setAvgPrice(model.getAvgPrice());
        business.setCreatedAt(new Date());
        business.setCreatorId(user.getId());
        business.setStatus(BusinessStatus.INUSED.getValue());

        checkBusinessCodeRepeat(model.getBusinessCode());
        business.setBusinessCode(model.getBusinessCode());
        business.setBus(model.getBus());
        business.setIsPush(BusinessStatus.NOTPUSHED.getValue());

        businessMapper.insertSelective(business);

        createBusinessPhoto(wechatId, model, business);

        return business;
    }

    private void checkBusinessCodeRepeat(String businessCode) {
        Business record = new Business();
        record.setBusinessCode(businessCode);
        record.setStatus((byte) 1);
        record = businessMapper.selectOne(record);
        if (record != null) {
            throw new WechatException(Message.BUSINESS_CODE_EXIST);
        }
    }

    private void checkBusinessCodeRepeat(BusinessModel model) {
        Business record = new Business();
        record.setBusinessCode(model.getBusinessCode());
        record.setStatus((byte) 1);
        record = businessMapper.selectOne(record);
        if (record != null) {
            if (!record.getId().equals(model.getId())) {
                throw new WechatException(Message.BUSINESS_CODE_EXIST);
            }
        }
    }

    private Business getByBusinessName(Integer wechatId, String businessName) {
        Business record = new Business();
        record.setBusinessName(businessName);
        record.setWechatId(wechatId);
        record.setStatus((byte) 1);
        return businessMapper.selectOne(record);
    }

    @Override
    public Page<BusinessDto> search(Integer wechatId,
                                    BusinessModel businessModel, boolean queryCount)
            throws WechatException {
        if (businessModel.pagable()) {
            PageHelper.startPage(businessModel.getPageNum(),
                    businessModel.getPageSize(), queryCount);
        }
//        if (businessModel.getProvince() != null) {
//            List<String> places = Arrays.asList("北京", "天津", "上海", "重庆");
//            String place = areaInfoMapper.selectNameById(businessModel.getProvince(), "CN");
//            log.info(JSONObject.toJSONString(businessModel));
//            log.info(place);
//            if (places.contains(place)) {
//                log.info("~~~~~~~~~~~~~~~~~~~~");
//                return businessMapper.searchDirect(wechatId, businessModel.getProvince(),
//                        businessModel.getCity(), businessModel.getSortName(), businessModel.getSortDir());
//            }
//        }
        return businessMapper.search(wechatId,
                BusinessStatus.INUSED.getValue(), businessModel.getProvince(),
                businessModel.getCity(), businessModel.getLng(),
                businessModel.getLat(), businessModel.getQuery(),
                businessModel.getSortName(), businessModel.getSortDir());
    }

    @Override
    public BusinessDto get(Integer wechatId, Integer id) throws WechatException {
        notBlank(id, Message.BUSINESS_ID_NOT_BLANK);
        return businessMapper.get(wechatId, id);
    }

    @Override
    public void delete(Integer wechatId, BusinessModel model) throws WechatException {
        notBlank(model.getId(), Message.BUSINESS_ID_NOT_BLANK);
        Business record = getBusiness(wechatId, model.getId());
        record.setStatus(BusinessStatus.DELETED.getValue());
        businessMapper.updateByPrimaryKeySelective(record);
        if (record.getIsPush() == 1) {
            String poiId = businessMapper.searchByBusinessId(model.getId());
            String accesstoken = RefreshAccessTokenJob.getAccessTokenStr(wechatId);
            PoiId poi = new PoiId();
            poi.setPoi_id(poiId);
            ShopRtnInfo result = JwShopAPI.delShop(accesstoken, poi);

            if (!result.getErrmsg().equals("ok")) {
                throw new WechatException(Message.BUSINESS_WEXIN_DELETE_FAIL);
            }
        }
    }

    private Business getBusiness(Integer wechatId, Integer id) {
        Business record = new Business();
        record.setId(id);
        record.setWechatId(wechatId);
        record.setStatus(BusinessStatus.INUSED.getValue());
        record = businessMapper.selectOne(record);
        if (record == null) {
            throw new WechatException(Message.BUSINESS_NOT_EXIST);
        }
        return record;
    }

    @Override
    public Business update(Integer wechatId, BusinessModel model)
            throws WechatException {
        notBlank(model.getId(), Message.BUSINESS_ID_NOT_BLANK);
        notBlank(model.getBusinessName(), Message.BUSINESS_NAME_NOT_BLANK);
        notBlank(model.getAddress(), Message.BUSINESS_ADDRESS_NOT_BLANK);
        notBlank(model.getTelephone(), Message.BUSINESS_TELEPHONE_NOT_BLANK);

        if (StringUtils.isNotBlank(model.getBranchName())
                && StringUtils.equals(model.getBusinessName(),
                model.getBranchName())) {
            throw new WechatException(
                    Message.BUSINESS_NAME_NOT_EQUALS_BRANCH_NAME);
        }

        Business business = getBusiness(wechatId, model.getId());
        if (business == null) {
            throw new WechatException(Message.BUSINESS_NOT_EXIST);
        }
        Business exist = getByBusinessName(wechatId, model.getBusinessName());
        if (exist != null && !exist.getId().equals(model.getId())) {
            throw new WechatException(Message.BUSINESS_NAME_EXIST);
        }

        business.setBusinessName(model.getBusinessName());
        business.setBranchName(model.getBranchName());
        business.setProvince(model.getProvince());
        business.setCity(model.getCity());
        business.setDistrict(model.getDistrict());
        business.setAddress(model.getAddress());
        business.setTelephone(model.getTelephone());
        business.setLongitude(model.getLongitude());
        business.setLatitude(model.getLatitude());
        business.setRecommend(model.getRecommend());
        business.setSpecial(model.getSpecial());
        business.setIntroduction(model.getIntroduction());
        business.setOpenTime(model.getOpenStartTime() + "-"
                + model.getOpenEndTime());
        business.setAvgPrice(model.getAvgPrice());

        checkBusinessCodeRepeat(model);
        business.setBusinessCode(model.getBusinessCode());
        business.setBus(model.getBus());

        businessMapper.updateByPrimaryKeySelective(business);

        BusinessPhoto record = new BusinessPhoto();
        record.setBusinessId(business.getId());
        record.setWechatId(wechatId);
        businessPhotoMapper.delete(record);
        createBusinessPhoto(wechatId, model, business);

        if (model.getIsPush() == 1) {
            if (model.getPush() != null) {
                updateBusinessToWx(wechatId, business, model);
            }
        } else {
            if (model.getPush() != null) {
                pushBusinessToWx(wechatId, business, model);
            }
        }

        return business;
    }

    private void createBusinessPhoto(Integer wechatId, BusinessModel model,
                                     Business business) {
        List<String> photoList = model.getPhotoList();
        if (photoList == null || photoList.isEmpty()) {
            return;
        }
        BusinessPhoto businessPhoto = null;
        List<BusinessPhoto> businessPhotos = new ArrayList<BusinessPhoto>();
        for (String photpUrl : photoList) {
            businessPhoto = new BusinessPhoto();
            businessPhoto.setBusinessId(business.getId());
            businessPhoto.setPhotoUrl(photpUrl);
            businessPhoto.setWechatId(wechatId);
            businessPhotos.add(businessPhoto);
        }
        businessPhotoMapper.insertList(businessPhotos);
    }

    @Override
    public Business getBusinessByCode(Integer wechatId, String code) {
        Business record = new Business();
        record.setWechatId(wechatId);
        record.setBusinessCode(code);
        return businessMapper.selectOne(record);
    }

    @Override
    public List<BusinessDto> searchByLngLat(Integer wechatId, Double lng,
                                            Double lat, Integer size) throws WechatException {
        return businessMapper.searchByLngLat(wechatId, lng, lat, size);
    }

    @Override
    public void pushBusinessToWx(Integer wechatId, Business business, BusinessModel model) {
        String accesstoken = RefreshAccessTokenJob.getAccessTokenStr(wechatId);
        com.d1m.wechat.wxsdk.wxshop.model.Business record = new com.d1m.wechat.wxsdk.wxshop.model.Business();
        BaseInfo baseInfo = new BaseInfo();
        List<Photo> photo_list = new ArrayList<Photo>();
        List<String> absolutePhotoList = model.getAbsolutePhotoList();

        if (absolutePhotoList != null && !absolutePhotoList.isEmpty()) {
            for (String absolutePhoto : absolutePhotoList) {
                String uploadUrl = JwShopAPI.uploadImg(accesstoken, absolutePhoto);
                if (uploadUrl == null) {
                    throw new WechatException(Message.BUSINESS_WEIXIN_PHOTO_UPLOAD_FAIL);
                }
                Photo photo = new Photo();
                photo.setPhoto_url(uploadUrl);
                photo_list.add(photo);
                String[] strs = absolutePhoto.split("/");
                String str = strs[strs.length - 1];
                BusinessPhoto businessPhoto = businessPhotoMapper.searchLike(str);
                businessPhoto.setWxUrl(uploadUrl);
                businessPhotoMapper.updateByPrimaryKeySelective(businessPhoto);
            }
        }

        if (business.getBusinessCode() != null) {
            baseInfo.setSid(business.getBusinessCode());
        }
        baseInfo.setBusiness_name(business.getBusinessName());
        baseInfo.setBranch_name(business.getBranchName());
        String province = areaInfoService.selectNameById(business.getProvince(), null);
        String city = areaInfoService.selectNameById(business.getCity(), null);
        baseInfo.setProvince(province);
        baseInfo.setCity(city);
        baseInfo.setDistrict(business.getDistrict());
        baseInfo.setAddress(business.getAddress());
        baseInfo.setTelephone(business.getTelephone());
        baseInfo.setOffset_type(String.valueOf(1));
        baseInfo.setLongitude(business.getLongitude().toString());
        baseInfo.setLatitude(business.getLatitude().toString());

        if (!photo_list.isEmpty()) {
            baseInfo.setPhoto_list(photo_list);
        }
        if (business.getRecommend() != null) {
            baseInfo.setRecommend(business.getRecommend());
        }
        if (business.getSpecial() != null) {
            baseInfo.setSpecial(business.getSpecial());
        }
        if (business.getIntroduction() != null) {
            baseInfo.setIntroduction(business.getIntroduction());
        }
        if (business.getOpenTime() != null) {
            baseInfo.setOpen_time(business.getOpenTime());
        }
        if (business.getAvgPrice() != null) {
            baseInfo.setAvg_price(business.getAvgPrice().toString());
        }

        List<String> categories = model.getCategories();
        baseInfo.setCategories(categories.toString());

        record.setBase_info(baseInfo);
        ShopRtnInfo result = JwShopAPI.doAddshop(accesstoken, record);

        if (!result.getErrmsg().equals("ok")) {
            throw new WechatException(Message.BUSINESS_WEIXIN_PUBLISE_FAIL);
        }

    }

    private void updateBusinessToWx(Integer wechatId, Business business,
                                    BusinessModel model) {
        String accesstoken = RefreshAccessTokenJob.getAccessTokenStr(wechatId);
        com.d1m.wechat.wxsdk.wxshop.model.Business record = new com.d1m.wechat.wxsdk.wxshop.model.Business();
        BaseInfo baseInfo = new BaseInfo();
        List<Photo> photo_list = new ArrayList<Photo>();
        List<String> absolutePhotoList = model.getAbsolutePhotoList();

        if (absolutePhotoList != null && !absolutePhotoList.isEmpty()) {
            for (String absolutePhoto : absolutePhotoList) {
                String uploadUrl = null;
                String[] strs = absolutePhoto.split("/");
                String str = strs[strs.length - 1];
                BusinessPhoto businessPhoto = businessPhotoMapper.searchLike(str);
                if (businessPhoto.getWxUrl() == null) {
                    uploadUrl = JwShopAPI.uploadImg(accesstoken, absolutePhoto);
                    if (uploadUrl == null) {
                        throw new WechatException(Message.BUSINESS_WEIXIN_PHOTO_UPLOAD_FAIL);
                    }
                    businessPhoto.setWxUrl(uploadUrl);
                    businessPhotoMapper.updateByPrimaryKeySelective(businessPhoto);
                } else {
                    uploadUrl = businessPhoto.getWxUrl();
                }

                Photo photo = new Photo();
                photo.setPhoto_url(uploadUrl);
                photo_list.add(photo);
            }
        }

        BusinessResult businessResult = businessResultMapper.searchByUniqId(business.getBusinessCode());
        baseInfo.setPoi_id(businessResult.getPoiId());
        baseInfo.setTelephone(business.getTelephone());
        if (!photo_list.isEmpty()) {
            baseInfo.setPhoto_list(photo_list);
        }
        if (business.getRecommend() != null) {
            baseInfo.setRecommend(business.getRecommend());
        }
        if (business.getSpecial() != null) {
            baseInfo.setSpecial(business.getSpecial());
        }
        if (business.getIntroduction() != null) {
            baseInfo.setIntroduction(business.getIntroduction());
        }
        if (business.getOpenTime() != null) {
            baseInfo.setOpen_time(business.getOpenTime());
        }
        if (business.getAvgPrice() != null) {
            baseInfo.setAvg_price(business.getAvgPrice().toString());
        }

        record.setBase_info(baseInfo);
        ShopRtnInfo result = JwShopAPI.updateShop(accesstoken, record);

        if (!result.getErrmsg().equals("ok")) {
            throw new WechatException(Message.BUSINESS_WEXIN_UPDATE_FAIL);
        }

    }

    @Override
    public synchronized void initBusinessLatAndLng(Integer wechatId, User user) {
        List<Business> list = businessMapper.getAll();
        String[] directCity = {"北京市", "上海市", "重庆市", "天津市"};
        List<String> directCityList = Arrays.asList(directCity);
        Boolean flagBaidu;
        Boolean flagQQ;
        for (Business business : list) {
            if (business.getAddress() != null) {
                flagBaidu = false;
                flagQQ = false;
                if (business.getLatitude() != null && business.getLongitude() != null) {
                    flagBaidu = true;
                }
                if (business.getWxlat() != null && business.getWxlng() != null) {
                    flagQQ = true;
                }
                if (flagBaidu && flagQQ) {
                    continue;
                }
                if (!flagBaidu) {
                    Map<String, Double> map = baiduLocationUtil.getLatAndLngByAddress(wechatId,
                            business.getAddress());
                    if (map != null) {
                        log.debug(JSONObject.toJSONString(map));
                        Map<String, String> mapAddress = baiduLocationUtil.getAddressByLatAndLng(wechatId,
                                map.get("lat").toString(), map.get("lng").toString());
                        log.debug(JSONObject.toJSONString(mapAddress));
                        if (mapAddress != null) {
                            if (map.get("lat") != null && !"".equals(map.get("lat")))
                                business.setLatitude(map.get("lat"));
                            if (map.get("lng") != null && !"".equals(map.get("lng")))
                                business.setLongitude(map.get("lng"));
                        } else {
                            throw new WechatException(Message.BUSINESS_FETCH_BAIDU_ADDRAPI_FAIL);
                        }
                        String country = mapAddress.get("country");
                        String province = mapAddress.get("province");
                        String city = mapAddress.get("city");
                        String district = mapAddress.get("district");
                        Integer countrycode = areaInfoMapper.selectIdByName(country, null);
                        Integer provincecode = areaInfoMapper.selectIdByName(province, countrycode);
                        business.setProvince(provincecode);
                        business.setDistrict(district);
                        if (directCityList.contains(mapAddress.get("province"))) {
                            business.setCity(areaInfoMapper.selectIdByName(district, provincecode));
                        } else {
                            business.setCity(areaInfoMapper.selectIdByName(city, provincecode));
                        }
                    } else {
                        throw new WechatException(Message.BUSINESS_FETCH_BAIDU_GEOAPI_FAIL);
                    }
                }
                if (!flagQQ) {
                    Double lat = business.getLatitude();
                    Double lng = business.getLongitude();
                    JSONObject object = baiduLocationUtil.transforLatAndLng(wechatId, lat + "," + lng);
                    log.debug(JSONObject.toJSONString(object));
                    if (object != null) {
                        lat = object.getJSONArray("locations").getJSONObject(0).getDouble("lat");
                        lng = object.getJSONArray("locations").getJSONObject(0).getDouble("lng");
                    } else {
                        throw new WechatException(Message.BUSINESS_TRANSFER_QQ_GEOAPI_FAIL);
                    }
                    business.setWxlat(lat);
                    business.setWxlng(lng);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                log.info(JSONObject.toJSONString(business));
                businessMapper.updateByPrimaryKeySelective(business);

            }
        }
    }


    @Override
    public List<BusinessAreaListDto> getProvinceList(Integer wechatId) {
        return businessMapper.getProvinceList(wechatId);
    }

    @Override
    public List<BusinessDto> getShanghaiBusinessList() {
        return businessMapper.getShanghaiBusinessList();
    }

    @Override
    public List<BusinessAreaListDto> getCityList(Integer wechatId) {
        return businessMapper.getCityList(wechatId);
    }

    @Override
    public List<PlaceBusinesssDto> getAllGroupedBusiness() {
        return businessMapper.getAllGroupedBusiness();
    }
}
