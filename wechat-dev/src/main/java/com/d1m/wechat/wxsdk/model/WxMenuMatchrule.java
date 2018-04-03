package com.d1m.wechat.wxsdk.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 类描述
 *
 * @author f0rb on 2016-12-06.
 */
@Getter
@Setter
public class WxMenuMatchrule {
    //用户分组id，可通过用户分组管理接口获取
    private String group_id;
    //性别：男（1）女（2）
    private String sex;

    private String country;

    private String province;

    private String city;
    //客户端版本，当前只具体到系统型号：IOS(1), Android(2), Others(3)，不填则不做匹配
    private Byte client_platform_type;
    /**
     * 语言信息，是用户在微信中设置的语言，具体请参考语言表：
     * <ul>
     * <li>1、简体中文 "zh_CN"</li>
     * <li>2、繁体中文TW "zh_TW"</li>
     * <li>3、繁体中文HK "zh_HK"</li>
     * <li>4、英文 "en"</li>
     * <li>5、印尼 "id"</li>
     * <li>6、马来 "ms"</li>
     * <li>7、西班牙 "es"</li>
     * <li>8、韩国 "ko"</li>
     * <li>9、意大利 "it"</li>
     * <li>10、日本 "ja"</li>
     * <li>11、波兰 "pl"</li>
     * <li>12、葡萄牙 "pt"</li>
     * <li>13、俄国 "ru"</li>
     * <li>14、泰文 "th"</li>
     * <li>15、越南 "vi"</li>
     * <li>16、阿拉伯语 "ar"</li>
     * <li>17、北印度 "hi"</li>
     * <li>18、希伯来 "he"</li>
     * <li>19、土耳其 "tr"</li>
     * <li>20、德语 "de"</li>
     * <li>21、法语 "fr"</li>
     * </ul>
     */
    private String language;

}
