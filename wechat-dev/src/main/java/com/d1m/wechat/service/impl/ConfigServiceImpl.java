package com.d1m.wechat.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.d1m.wechat.dto.ConfigDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tk.mybatis.mapper.common.Mapper;

import com.d1m.wechat.mapper.ConfigMapper;
import com.d1m.wechat.model.Config;
import com.d1m.wechat.service.ConfigService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ConfigServiceImpl extends BaseService<Config> implements
		ConfigService {
	
	@Autowired
	private ConfigMapper configMapper;
	
	@Override
	public Mapper<Config> getGenericMapper() {
		return configMapper;
	}

	@Override
	public Config getConfig(Integer wechatId, String group, String key) {
		return configMapper.getConfig(wechatId, group, key);
	}

	@Override
	public ConfigDto getConfigDto(Integer wechatId, String group, String key) {
		return configMapper.getConfigDto(wechatId, group, key);
	}

	@Override
	public List<ConfigDto> getConfigDtoList(Integer wechatId, String group) {
		return configMapper.getConfigDtoList(wechatId, group);
	}

	@Override
	public String getConfigValue(Integer wechatId, String group, String key) {
		Config cfg = getConfig(wechatId, group, key);
		if(cfg!=null){
			return cfg.getCfgValue();
		}
		return null;
	}

	@Override
	public void update(JSONArray jsonArray) {
		if(jsonArray!=null){
			for (int i=0;i<jsonArray.size();i++){
				JSONObject obj = jsonArray.getJSONObject(i);
				Config config = new Config();
				config.setId(obj.getInteger("id"));
				config.setCfgValue(obj.getString("value"));
				configMapper.updateByPrimaryKeySelective(config);
			}
		}
	}

	@Override
	public Map<String, Object> getConfigMap(Integer wechatId, String group) {
		List<Map<String, Object>> list = configMapper.getConfigMap(wechatId,
				group);
		return getMap(list);
	}

	private Map<String, Object> getMap(List<Map<String, Object>> list) {
		Map<String, Object> m = new HashMap<String, Object>();
		for (Map<String, Object> map : list) {
			m.put((String) map.get("cfg_key"), map.get("cfg_value"));
		}
		return m;
	}
}
