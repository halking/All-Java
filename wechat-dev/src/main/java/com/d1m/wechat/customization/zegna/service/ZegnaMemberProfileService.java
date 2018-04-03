package com.d1m.wechat.customization.zegna.service;

import com.d1m.wechat.customization.zegna.model.ZegnaMemberProfileModel;
import com.d1m.wechat.customization.zegna.model.ZegnaMemberProfile;
import com.d1m.wechat.service.IService;

/**
 * ZegnaMemberProfileService
 *
 * @author f0rb on 2017-02-10.
 */
public interface ZegnaMemberProfileService extends IService<ZegnaMemberProfile> {
    void bind(ZegnaMemberProfileModel model);
}
