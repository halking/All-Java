package com.d1m.wechat.spi.model;

import lombok.Getter;
import lombok.Setter;

import com.d1m.wechat.spi.core.SpiModel;

/**
 * ArticleRequest
 *
 * @author f0rb on 2017-07-07.
 */
@Getter
@Setter
public class ArticleRequest extends SpiModel {
    private String sceneId;
}
