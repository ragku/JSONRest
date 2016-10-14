package com.ragku.demo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSONObject;
import com.ragku.rest.annotation.RestDelete;
import com.ragku.rest.annotation.RestGet;
import com.ragku.rest.annotation.RestPatch;
import com.ragku.rest.annotation.RestPost;
import com.ragku.rest.annotation.RestPut;

public class HelloApi {
	
	private static final Log log = LogFactory.getLog(HelloApi.class);
	
	@RestGet("/hello/bike")
	public BikeEntity getBikeEntity(Long id) {
		log.info(id);
		BikeEntity be = new BikeEntity();
		be.setBikeNo("aaaaaaaa");
		be.setColor("black");
		be.setId(100L);
		return be;
	}
	
	@RestPost("/hello/bike")
	public void addBike(BikeEntity bike) {
		log.info(JSONObject.toJSONString(bike));
	}
	
	@RestPut("/hello/bike")
	public void updateBike(BikeEntity bike) {
		log.info(JSONObject.toJSONString(bike));
	}
	
	@RestPatch("/hello/bike")
	public void modifyBike(BikeEntity bike) {
		log.info(JSONObject.toJSONString(bike));
	}
	
	@RestDelete("/hello/bike")
	public void deleteBike(Long id) {
		log.info(id);
	}
}
