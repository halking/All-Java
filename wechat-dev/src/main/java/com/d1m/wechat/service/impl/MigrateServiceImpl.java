package com.d1m.wechat.service.impl;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.locks.ReentrantLock;
import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.d1m.wechat.mapper.AreaInfoMapper;
import com.d1m.wechat.mapper.MenuGroupMapper;
import com.d1m.wechat.migrate.MigrateResult;
import com.d1m.wechat.migrate.MigrateServiceExecutor;
import com.d1m.wechat.migrate.MigrateUserExecutor;
import com.d1m.wechat.model.MenuGroup;
import com.d1m.wechat.model.User;
import com.d1m.wechat.model.enums.MenuGroupStatus;
import com.d1m.wechat.model.enums.MenuType;
import com.d1m.wechat.pamametermodel.MenuGroupModel;
import com.d1m.wechat.pamametermodel.MenuModel;
import com.d1m.wechat.pamametermodel.MenuRuleModel;
import com.d1m.wechat.service.MenuGroupService;
import com.d1m.wechat.service.MigrateService;
import com.d1m.wechat.wxsdk.menu.JwMenuAPI;
import com.d1m.wechat.wxsdk.model.WxMenu;
import com.d1m.wechat.wxsdk.model.WxMenuGroup;
import com.d1m.wechat.wxsdk.model.WxMenuHolder;
import com.d1m.wechat.wxsdk.model.WxMenuMatchrule;
import com.d1m.wechat.wxsdk.user.user.JwUserAPI;
import com.d1m.wechat.wxsdk.wxbase.wxmedia.JwMediaAPI;
import com.d1m.wechat.wxsdk.wxbase.wxmedia.model.WxMaterial;
import com.d1m.wechat.wxsdk.wxbase.wxmedia.model.WxMaterialPage;

/**
 * 微信数据同步服务的实现
 *
 * @author f0rb on 2016-12-01.
 */
@Slf4j
@Service
public class MigrateServiceImpl implements MigrateService {
    private static final ReentrantLock materialLock = new ReentrantLock();
    private static final ReentrantLock menuLock = new ReentrantLock();
    private static final ReentrantLock userLock = new ReentrantLock();
    //测试接口超过次数限制用假数据
    @Value("${wechat.fake:false}")
    private boolean fake;
    @Value("${wechat.fake.news:}")
    private String fakeNews;
    @Value("${wechat.fake.material:}")
    private String fakeMaterial;

    @Value("${wechat.material.pull.count:20}")
    private int LIMIT;//每次去微信拉取的素材数目

    private String[] knownMaterialTypes;//微信素材的类型

    @Resource
    private MigrateServiceExecutor migrateServiceExecutor;
    @Resource
    private MenuGroupService menuGroupService;
    @Resource
    private MenuGroupMapper menuGroupMapper;
    @Resource
    private AreaInfoMapper areaInfoMapper;

    @Value("${wechat.material.types:image,video,voice,news}")
    public void setKnownMaterialTypes(String[] knownMaterialTypes) {
        //排个序算了, 用于二分查找
        Arrays.sort(knownMaterialTypes);
        this.knownMaterialTypes = knownMaterialTypes;
    }

    @Override
    public boolean isKnownMaterialType(String materialType) {
        return Arrays.binarySearch(knownMaterialTypes, materialType) > -1;
    }

    @Override
    public MigrateResult migrateMaterial(String materialType, Boolean canUpdate, Integer userId, Integer wechatId) {
        MigrateResult migrateResult = new MigrateResult();
        List<String> errors = new LinkedList<>();
        if (!materialLock.tryLock()) {//防止素材同步操作被多次执行
            errors.add("任务执行中!");
            migrateResult.setErrors(errors);
            return migrateResult;
        }
        try {
            List<WxMaterial> ignored = new LinkedList<>();
            // 收集异步执行结果
            List<Future<List<WxMaterial>>> futureList = new LinkedList<>();


            //按类型拉取微信素材, 如果为all则拉取全部
            log.info("按类型[{}]拉取微信素材...", materialType);
            String[] materialTypes;
            if (materialType.equals("all")) {
                materialTypes = knownMaterialTypes;
            } else {
                materialTypes = new String[]{materialType};
            }
            for (String type : materialTypes) {
                try {
                    futureList.addAll(doPullMaterial(type, userId, wechatId, canUpdate));
                } catch (RuntimeException e) {
                    log.error("任务分配失败!", e);
                    errors.add(e.getMessage());
                }
            }

            log.info("微信素材拉取完成, 等待后台处理...");
            for (Future<List<WxMaterial>> future : futureList) {
                try {
                    ignored.addAll(future.get());
                } catch (InterruptedException e) {
                    log.error("future被中断", e);
                    errors.add(e.getMessage());
                } catch (ExecutionException e) {
                    log.error("future执行失败", e);
                    errors.add(e.getMessage());
                }
            }
            if (!ignored.isEmpty()) {
                migrateResult.setIgnored(ignored);
            }
            if (!errors.isEmpty()) {
                migrateResult.setErrors(errors);
            }
            log.info("微信素材处理完毕!");
            return migrateResult;
        } finally {
            materialLock.unlock();
        }
    }

    /**
     * 分配素材拉取任务, 收集任务执行结果
     *
     * @param materialType 素材类型
     * @param userId 用户id
     * @param wechatId 微信id
     * @param canUpdate 是否允许更新
     * @return 任务执行的Future
     */
    private List<Future<List<WxMaterial>>> doPullMaterial(String materialType, Integer userId, Integer wechatId, Boolean canUpdate) {
        if (!isKnownMaterialType(materialType)) {
            log.error("Unknown material type: {}", materialType);
            return Collections.emptyList();
        }

        log.info("拉取{}", materialType);

        List<Future<List<WxMaterial>>> futureList = new LinkedList<>();
        Integer total = 1;
        for (int offset = 0; offset < total; offset += LIMIT) {
            //去微信拉取素材列表
            WxMaterialPage wxMaterialPage = queryMaterial(wechatId, materialType, offset, LIMIT);
            if (wxMaterialPage.fail()) {//记录出错信息
                String errorMessage =
                        String.format("拉取%s出错: [%s:%s], 请求参数: wechatId=%s, materialType=%s, offset=%s, LIMIT=%s",
                                      materialType, wxMaterialPage.getErrcode(), wxMaterialPage.getErrmsg(),
                                      wechatId, materialType, offset, LIMIT);
                throw new RuntimeException(errorMessage);
            }
            //异步处理
            Future<List<WxMaterial>> result = migrateServiceExecutor.executeMaterial(wxMaterialPage.getItem(), materialType, userId, wechatId, canUpdate);
            //收集处理结果
            futureList.add(result);
            //更新total
            total = wxMaterialPage.getTotal_count();
        }
        return futureList;
    }

    private WxMaterialPage queryMaterial(Integer wechatId, String materialType, int offset, int count) {
        if (fake) {
            String json;
            if ("news".equals(materialType)) {
                json = fakeNews;
            } else {
                json = fakeMaterial;
            }
            return JSON.parseObject(json, WxMaterialPage.class);
        }

        String accessToken = RefreshAccessTokenJob.getAccessTokenStr(wechatId);
        WxMaterialPage wxMaterialPage = JwMediaAPI.queryMaterial(accessToken, materialType, offset, count);
        if (log.isDebugEnabled()) {
            log.debug("拉取微信[{}]的素材{}\n{}", wechatId, materialType, JSON.toJSONString(wxMaterialPage, true));
        }
        return wxMaterialPage;
    }

    @Override
    public MigrateResult migrateMenu(Boolean canUpdate, User user, Integer wechatId) {
        MigrateResult migrateResult = new MigrateResult();
        if (!menuLock.tryLock()) {
            migrateResult.setMessage("任务执行中!");
            return migrateResult;
        }
        try {
            MenuGroup defaultMenuGroup = menuGroupMapper.getDefaultMenuGroup(wechatId);
            if (defaultMenuGroup != null) {
                if (!canUpdate) {//不允许更新菜单
                    migrateResult.setMessage("微信菜单已存在, 未执行更新操作");
                    return migrateResult;
                } else {//清空系统内菜单
                    MenuGroup query = new MenuGroup();
                    query.setWechatId(wechatId);
                    query.setStatus(MenuGroupStatus.INUSED.getValue());
                    List<MenuGroup> menuGroupList = menuGroupMapper.select(query);
                    //更新status为删除状态
                    for (MenuGroup menuGroup : menuGroupList) {
                        menuGroup.setDeletedAt(new Date());
                        menuGroup.setDeletorId(user.getId());
                        menuGroup.setStatus(MenuGroupStatus.DELETED.getValue());
                        menuGroupMapper.updateByPrimaryKey(menuGroup);
                    }
                }
            }

            String accessToken = RefreshAccessTokenJob.getAccessTokenStr(wechatId);
            WxMenuHolder wxMenuHolder = JwMenuAPI.getWxMenuHolder(accessToken);

            if (wxMenuHolder.fail()) {
                migrateResult.setMessage(wxMenuHolder.getErrmsg());
                return migrateResult;
            }

            WxMenuGroup wxDefaultMenuGroup= wxMenuHolder.getMenu();
            if (log.isInfoEnabled()) {
                log.info("处理默认菜单: \n{}", JSON.toJSONString(wxMenuHolder.getMenu(), true));
            }
            MenuGroupModel menuGroupModel;
            menuGroupModel = new MenuGroupModel();
            menuGroupModel.setMenuGroupName("默认菜单");
            menuGroupModel.setWxMenuId(wxDefaultMenuGroup.getMenuId());
            menuGroupModel.setPush(true);
            List<MenuModel> menuModelList = resolveWxMenuList(null, wxDefaultMenuGroup.getButton());
            menuGroupModel.setMenus(menuModelList);
            menuGroupService.create(user, wechatId, menuGroupModel);

            if (log.isInfoEnabled()) {
                log.info("处理个性化菜单: \n{}", JSON.toJSONString(wxMenuHolder.getConditionalmenu(), true));
            }
            List<WxMenuGroup> conditionalMenu = wxMenuHolder.getConditionalmenu();
            int i = 1;
            for (WxMenuGroup wxMenuGroup : conditionalMenu) {
                menuGroupModel = new MenuGroupModel();
                menuGroupModel.setMenuGroupName("自动同步菜单" + i);
                menuGroupModel.setWxMenuId(wxMenuGroup.getMenuId());
                menuGroupModel.setPush(true);
                List<MenuModel> menuModels = resolveWxMenuList(null, wxMenuGroup.getButton());
                menuGroupModel.setMenus(menuModels);
                MenuRuleModel menuRuleModel = resolveWxMenuMatchrule(wxMenuGroup.getMatchrule());
                menuGroupModel.setRules(menuRuleModel);
                menuGroupService.create(user, wechatId, menuGroupModel);
                i++;
            }
            return migrateResult;
        } finally {
            menuLock.unlock();
        }
    }

    private MenuRuleModel resolveWxMenuMatchrule(WxMenuMatchrule matchrule) {
        MenuRuleModel menuRuleModel = new MenuRuleModel();
        // TODO 处理规则的用户分组
        // menuRuleModel.setGroupId(matchrule.getGroup_id());
        if (StringUtils.isNotBlank(matchrule.getSex())) {
            menuRuleModel.setGender(Byte.valueOf(matchrule.getSex()));
        }
        if (StringUtils.isNotBlank(matchrule.getLanguage())) {
            menuRuleModel.setLanguage(Byte.valueOf(matchrule.getLanguage()));
        }
        menuRuleModel.setClientPlatformType(matchrule.getClient_platform_type());
        if (StringUtils.isNotBlank(matchrule.getCountry())) {
            Integer country = areaInfoMapper.selectIdByName(matchrule.getCountry(), null);
            if (StringUtils.isNotBlank(matchrule.getProvince())) {
                Integer province = areaInfoMapper.selectIdByName(matchrule.getProvince(), country);
                if (StringUtils.isNotBlank(matchrule.getCity())) {
                    Integer city = areaInfoMapper.selectIdByName(matchrule.getCity(), province);
                    menuRuleModel.setCountry(country);
                    menuRuleModel.setProvince(province);
                    menuRuleModel.setCity(city);
                }
            }
        }

        if (log.isInfoEnabled()) {
            log.info("规则转换:\n原始规则: {}\n转换规则{}",
                     JSON.toJSONString(matchrule), JSON.toJSONString(menuRuleModel));
        }
        return menuRuleModel;
    }

    /**
     * 数据结构转换
     * <p>
     * <b>假设素材已经同步完毕</b>
     *
     * @param parent     父菜单
     * @param wxMenuList 微信菜单的列表
     * @return MenuModel列表
     */
    private List<MenuModel> resolveWxMenuList(MenuModel parent, List<WxMenu> wxMenuList) {
        List<MenuModel> menuModelList = new LinkedList<>();
        int seq = 1;
        for (WxMenu wxMenu : wxMenuList) {
            if (log.isDebugEnabled()) {
                log.debug("解析菜单: \n{}", JSON.toJSONString(wxMenu, true));
            }
            MenuModel menuModel = new MenuModel();
            menuModel.setName(wxMenu.getName());
            menuModel.setUrl(wxMenu.getUrl());
            menuModel.setSeq(seq++);
            menuModel.setParent(parent);
            if (wxMenu.getType() == null) {
                menuModel.setType(MenuType.PARENT.getValue());
            } else {
                // TODO 处理news、text、img、photo、video、voice等类型的菜单
                menuModel.setType(MenuType.valueByName(wxMenu.getType()));
            }
            // 递归解析子菜单
            if (wxMenu.getSub_button() != null && !wxMenu.getSub_button().isEmpty()) {
                menuModel.setChildren(resolveWxMenuList(menuModel, wxMenu.getSub_button()));
            }
            menuModelList.add(menuModel);
        }
        return menuModelList;
    }

    @Resource
    private MigrateUserExecutor migrateUserExecutor;

    public MigrateResult migrateUser(Integer wechatId) {
        // 收集异步执行结果
        List<Future<String>> futureList = new LinkedList<>();
        MigrateResult migrateResult = new MigrateResult();

        if (!userLock.tryLock()) {//防止素材同步操作被多次执行
            migrateResult.addError("任务执行中!");
            return migrateResult;
        }
        try {
            String nextOpenId = null;
            while (true) {
                //循环拉取openid, 每次一万
                List<String> openIdList = JwUserAPI.getOpenidList(wechatId, nextOpenId);
                if (openIdList.isEmpty()) {
                    break;
                }
                nextOpenId = openIdList.get(openIdList.size() - 1);

                //每次取100个openid, 提交到线程去拉取详细信息
                int step = 100;
                int fromIndex = 0, toIndex, total = openIdList.size();
                while (fromIndex < total) {
                    toIndex = Math.min(fromIndex + step, openIdList.size());
                    Future<String > future = migrateUserExecutor.execute(openIdList.subList(fromIndex, toIndex), wechatId);
                    futureList.add(future);
                    fromIndex += step;
                }
            }

            log.info("openid拉取完成, 等待后台处理...");
            for (Future<String> future : futureList) {
                try {
                    migrateResult.addMessage(future.get());
                } catch (InterruptedException e) {
                    log.error("future被中断", e);
                    migrateResult.addError(e.getMessage());
                } catch (ExecutionException e) {
                    log.error("future执行失败", e);
                    migrateResult.addError(e.getMessage());
                }
            }
            log.info("openid处理完毕: {}!", JSON.toJSONString(migrateResult));
            return migrateResult;
        } finally {
            userLock.unlock();
        }
    }
}
