package com.d1m.wechat.spi.action;

import java.util.*;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.d1m.wechat.model.enums.MsgType;
import com.d1m.wechat.service.impl.RefreshAccessTokenJob;
import com.d1m.wechat.spi.core.AbstractNotifyEventAction;
import com.d1m.wechat.spi.core.NotifyObject;
import com.d1m.wechat.spi.core.message.TextMessage;
import com.d1m.wechat.wxsdk.core.req.model.kfaccount.KfcustomSend;
import com.d1m.wechat.wxsdk.core.req.model.kfaccount.MsgArticles;
import com.d1m.wechat.wxsdk.core.req.model.kfaccount.MsgNews;
import com.d1m.wechat.wxsdk.core.sendmsg.JwKfaccountAPI;

/**
 * PushOutletsArticleAction
 * <p>
 * 推送门店信息图文
 *
 * @author f0rb on 2017-07-18.
 */
@Slf4j
@Component
public class PushOutletsArticleAction extends AbstractNotifyEventAction {

    //private String provinces = "辽宁、吉林、黑龙江、河北、山西、陕西、甘肃、青海、山东、安徽、江苏、浙江、河南、湖北、湖南、江西、台湾、福建、云南、海南、四川、贵州、广东、内蒙古、新疆、广西、西藏、宁夏、北京、上海、天津、重庆、香港、澳门";
    private final static Set<String> provinceSet = new HashSet<>();
    private final static Set<String> areaSet = new HashSet<>();
    private final static Map<String, String> cityProvMap = new HashMap<>();

    static {
        String citiesText = "河北：石家庄 唐山 秦皇岛 邯郸 邢台 保定 张家口 承德 沧州 廊坊 衡水\n" +
                "山西：太原 大同 阳泉 长治 晋城 朔州 晋中 运城 忻州 临汾 吕梁\n" +
                "内蒙古：呼和浩特 包头 乌海 赤峰 通辽 鄂尔多斯 呼伦贝尔 巴彦淖尔 乌兰察布 兴安 锡林郭勒 阿拉善\n" +
                "辽宁：沈阳 大连 鞍山 抚顺 本溪 丹东 锦州 营口 阜新 辽阳 盘锦 铁岭 朝阳 葫芦岛\n" +
                "吉林：长春 吉林 四平 辽源 通化 白山 松原 白城 延边\n" +
                "黑龙江：哈尔滨 齐齐哈尔 鸡西 鹤岗 双鸭山 大庆 伊春 佳木斯 七台河 牡丹江 黑河 绥化 大兴安岭\n" +
                "江苏：南京 无锡 徐州 常州 苏州 南通 连云港 淮安 盐城 扬州 镇江 泰州 宿迁\n" +
                "浙江：杭州 宁波 温州 嘉兴 湖州 绍兴 金华 衢州 舟山 台州 丽水\n" +
                "安徽：合肥 芜湖 蚌埠 淮南 马鞍山 淮北 铜陵 安庆 黄山 滁州 阜阳 宿州 巢湖 六安 亳州 池州 宣城\n" +
                "福建：福州 厦门 莆田 三明 泉州 漳州 南平 龙岩 宁德\n" +
                "江西：南昌 景德镇 萍乡 九江 新余 鹰潭 赣州 吉安 宜春 抚州 上饶\n" +
                "山东：济南 青岛 淄博 枣庄 东营 烟台 潍坊 威海 济宁 泰安 日照 莱芜 临沂 德州 聊城 滨州 菏泽\n" +
                "河南：郑州 开封 洛阳 平顶山 焦作 鹤壁 新乡 安阳 濮阳 许昌 漯河 三门峡 南阳 商丘 信阳 周口 驻马店\n" +
                "湖北：武汉 黄石 襄阳 十堰 荆州 宜昌 荆门 鄂州 孝感 黄冈 咸宁 随州 恩施\n" +
                "湖南：长沙 株洲 湘潭 衡阳 邵阳 岳阳 常德 张家界 益阳 郴州 永州 怀化 娄底 湘西\n" +
                "广东：广州 深圳 珠海 汕头 韶关 佛山 江门 湛江 茂名 肇庆 惠州 梅州 汕尾 河源 阳江 清远 东莞 中山 潮州 揭阳 云浮\n" +
                "广西：南宁 柳州 桂林 梧州 北海 防城港 钦州 贵港 玉林 百色 贺州 河池 来宾 崇左\n" +
                "海南：海口 三亚 三沙\n" +
                "四川：成都 自贡 攀枝花 泸州 德阳 绵阳 广元 遂宁 内江 乐山 南充 宜宾 广安 达州 眉山 雅安 巴中 资阳 阿坝 甘孜 凉山\n" +
                "贵州：贵阳 六盘水 遵义 安顺 铜仁 毕节 黔西南 黔东南 黔南\n" +
                "云南：昆明 曲靖 玉溪 保山 昭通 丽江 普洱 临沧 文山 红河 西双版纳 楚雄 大理 德宏 怒江 迪庆\n" +
                "西藏：拉萨 昌都 山南 日喀则 那曲 阿里 林芝\n" +
                "陕西：西安 铜川 宝鸡 咸阳 渭南 延安 汉中 榆林 安康 商洛\n" +
                "甘肃：兰州 嘉峪关 金昌 白银 天水 武威 张掖 平凉 酒泉 庆阳 定西 陇南 临夏 甘南\n" +
                "青海：西宁 海东 海北 黄南 海南 果洛 玉树 海西\n" +
                "宁夏：银川 石嘴山 吴忠 固原 中卫\n" +
                "新疆：乌鲁木齐 克拉玛依 吐鲁番 哈密 和田 阿克苏 喀什 克孜勒苏柯尔克孜 巴音郭楞蒙古 昌吉 博尔塔拉蒙古 伊犁哈萨克 塔城 阿勒泰\n" +
                "台湾：台北 高雄 基隆 台中 台南 新竹 嘉义";
        for (String line : citiesText.split("\n")) {
            String[] arrays = line.split("：");
            areaSet.add(arrays[0]);
            provinceSet.add(arrays[0]);
            for (String city : arrays[1].split(" ")) {
                areaSet.add(city);
                cityProvMap.put(city, arrays[0]);
            }
        }
        Collections.addAll(areaSet, "北京、上海、天津、重庆、香港、澳门".split("、"));
        Collections.addAll(provinceSet, "北京、上海、天津、重庆、香港、澳门".split("、"));
        areaSet.remove("");
    }

    @Override
    public void execute(Integer wechatId, NotifyObject notifyObject, Map<String, String> configParamMap) {
        if (notifyObject instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) notifyObject;
            doExecute(wechatId, textMessage.getFromUserName(), textMessage.getContent(), configParamMap);
        }
    }

    /**
     * 关键词匹配成功后推送相关门店图文
     *
     * @param fromUserName   微信会员的openid
     * @param keyword        1.[extraKeywords], 2.省份/城市名, 3.省份/城市名+[tailKeywords]
     * @param configParamMap 例: {"title":"搜索您附近的杰尼亚精品店","description":"","url":"http://zegna.wechat.d.d1m.cn/site/#!/outlets","picurl":"http://zegna.wechat.d1mgroup.com/backend/webapp/attached/image/material/201702/17190905_651deee1579d1729_295713f57560acdefdcb54e2c597bc69.jpeg","extraKeywords":"附近,店铺,门店,精品店,附近店铺,附近门店,附近精品店","tailKeywords":"店铺,门店,精品店"}
     */
    public void doExecute(Integer wechatId, String fromUserName, String keyword, Map<String, String> configParamMap) {
        log.info("门店推送: wechatId={}, keyword={}", wechatId, keyword);
        try {
            String extraKeywords = configParamMap.get("extraKeywords");
            String tailKeywords = configParamMap.get("tailKeywords");
            // 预处理keyword
            boolean match = false;
            if (StringUtils.contains(extraKeywords, keyword)) {
                match = true;
                log.info("extra keyword matched: {}", keyword);
            } else if (areaSet.contains(keyword)) {
                match = true;
                log.info("area keyword matched: {}", keyword);
            } else if (StringUtils.isNotEmpty(tailKeywords)) {
                for (String tail : tailKeywords.split(",")) {
                    if (keyword.endsWith(tail)) {
                        String temp = keyword.substring(0, keyword.length() - tail.length());
                        if (areaSet.contains(temp)) {
                            log.info("tail keyword matched: {}", keyword);
                            keyword = temp;
                            match = true;
                            break;
                        }
                    }
                }
            }
            if (match) {
                // 匹配省市关键字或者上下文配置的关键字时触发
                String type = "other";
                if (provinceSet.contains(keyword)) {
                    type = "province";
                } else if (areaSet.contains(keyword)) {
                    type = "city";
                }
                MsgArticles msgArticle = new MsgArticles();
                String url = configParamMap.get("url");
                if (url.contains("#/")) {
                    String hash = url.substring(url.lastIndexOf("#"));
                    url += (hash.contains("?") ? "&" : "?");
                } else {
                    url += (url.contains("?") ? "&" : "?");
                }
                url += "keyword=" + keyword + "&type=" + type + "&openId=" + fromUserName;
                if (type.equals("city")) {
                    url += "&province=" + cityProvMap.get(keyword);
                }

                msgArticle.setUrl(url);
                String title = configParamMap.get("title");
                String substitute = configParamMap.get("substitute");
                if (StringUtils.isNotEmpty(substitute) && !type.equals("other")) {
                    title = title.replace(substitute, keyword);
                }
                msgArticle.setTitle(title);
                msgArticle.setDescription(configParamMap.get("description"));
                msgArticle.setPicurl(configParamMap.get("picurl"));

                List<MsgArticles> msgArticlesList = new ArrayList<>();
                msgArticlesList.add(msgArticle);

                String accessToken = RefreshAccessTokenJob.getAccessTokenStr(wechatId);
                KfcustomSend kfcustomSend = new KfcustomSend();
                kfcustomSend.setAccess_token(accessToken);
                kfcustomSend.setTouser(fromUserName);

                MsgNews msgNews = new MsgNews();
                msgNews.setArticles(msgArticlesList);
                kfcustomSend.setNews(msgNews);
                kfcustomSend.setMsgtype(MsgType.NEWS.name().toLowerCase());
                JwKfaccountAPI.sendKfMessage(kfcustomSend);
            }
        } catch (Exception e) {
            log.error(getActionName() + " failed!", e);
        }
    }
}
