package com.d1m.wechat.anno;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.d1m.wechat.model.enums.Effect;

@Retention(RetentionPolicy.RUNTIME)
public @interface EffectCode {

	public Effect value();

}
