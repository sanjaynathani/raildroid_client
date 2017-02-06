package com.glassera.railgadi.ando.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import android.content.res.AssetManager;

public class StationsData {

	private static List<String> stationsList;

	private StationsData(){
		super();
	}

	public static final List<String> getStationList(boolean refresh, AssetManager assetManager) throws IOException{
		if(refresh || (stationsList == null || stationsList.size() == 0)){
			InputStreamReader inputStreamReader = null;
			BufferedReader bufferedReader = null;
			try{
				assetManager.open("stations.txt");
				
				inputStreamReader = new InputStreamReader(assetManager.open("stations.txt"));
				bufferedReader = new BufferedReader(inputStreamReader);

				String data = bufferedReader.readLine();
				bufferedReader.close();
				inputStreamReader.close();

				stationsList = Arrays.asList(data.split(":"));
			}finally{
				if(bufferedReader != null)bufferedReader.close();
				if(inputStreamReader != null)inputStreamReader.close();

			}
		}
		return stationsList;
	}
}
