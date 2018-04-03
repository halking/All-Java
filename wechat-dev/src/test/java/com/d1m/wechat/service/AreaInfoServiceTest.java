package com.d1m.wechat.service;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import com.d1m.wechat.mapper.AreaInfoMapper;
import com.d1m.wechat.model.AreaInfo;
import com.d1m.wechat.service.impl.AreaInfoServiceImpl;

public class AreaInfoServiceTest {

	AreaInfoServiceImpl areaInfoService;

	AreaInfoMapper areaInfoMapper;

	Mockery context = new JUnit4Mockery();

	@Before
	public void setUp() {
		areaInfoMapper = context.mock(AreaInfoMapper.class);
		areaInfoService = new AreaInfoServiceImpl();
		areaInfoService.setAreaInfoMapper(areaInfoMapper);
	}

//	@Test
	public void selectAll() {
		context.checking(new Expectations() {
			{
				oneOf(areaInfoMapper).selectAll();
				List<AreaInfo> aiList = new ArrayList<AreaInfo>();
				AreaInfo ai = new AreaInfo();
				aiList.add(ai);
				will(returnValue(aiList));
			}
		});

		List<AreaInfo> aiList = areaInfoService.selectAll();
		assertTrue(!aiList.isEmpty());
	}

	@Test
	public void transfor(){
//		String url = "http://apis.map.qq.com/ws/coord/v1/translate?" +
//				"locations=39.12,116.83;30.21,115.43&" +
//				"type=3&" +
//				"key=YLFBZ-WHAWI-ZXUGH-53Q65-TOJ7E-ADBNQ";
		String strUrl = String
				.format("http://apis.map.qq.com/ws/coord/v1/translate?locations=%s&type=3&key=%s",
						"39.12,116.83;30.21,115.43", "YLFBZ-WHAWI-ZXUGH-53Q65-TOJ7E-ADBNQ");
		URLConnection conn = null;
		try {
			URL url = new URL(strUrl);
			conn = url.openConnection();
			StringBuilder result = new StringBuilder();
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			rd.close();
			JSONObject json = JSONObject.parseObject( result.toString());
			System.out.println(url);
			System.out.println(json);
			Double lat = json.getJSONArray("locations").getJSONObject(0).getDouble("lat");
			Double lng = json.getJSONArray("locations").getJSONObject(0).getDouble("lng");
			System.out.println(lat + "," + lng);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
