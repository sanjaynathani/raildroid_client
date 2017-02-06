package com.glassera.railgadi.ando.util;

import java.util.HashMap;
import java.util.Map;

public final class TrainCache {

	private static TrainCache trainCacheObj;
	private Map<String, Object> cacheMap;
	
	private TrainCache(){
		cacheMap = new HashMap<String, Object>();
	}
	
	public static final TrainCache getInstance(){
		if(trainCacheObj == null){
			trainCacheObj = new TrainCache();
		}
		return trainCacheObj;
	}
	
	public void putObject(String key, Object obj){
		if(!cacheMap.containsKey(key)){
			cacheMap.put(key, obj);
		}
	}
	
	public void updateObject(String key, Object obj){
		if(cacheMap.containsKey(key)){
			cacheMap.put(key, obj);
		}
	}
	
	public Object getObject(String key){
		return cacheMap.get(key);
	}
	
	public void clearCache(){
		cacheMap.clear();
	}
}
