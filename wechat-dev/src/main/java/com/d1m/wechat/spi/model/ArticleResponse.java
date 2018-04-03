package com.d1m.wechat.spi.model;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import com.d1m.wechat.spi.core.SpiModel;
import com.d1m.wechat.wxsdk.core.req.model.kfaccount.MsgArticles;

/**
 * ArticleRequest
 *
 * @author f0rb on 2017-07-07.
 */
@Getter
@Setter
@Service
public class ArticleResponse extends SpiModel {

    private List<MsgArticles> articles;
    private Date createTime;

}
