package com.glassera.railgadi.ando.activity;

import java.util.HashMap;
import java.util.Map;

import com.glassera.railgadi.ando.R;
import com.glassera.railgadi.ando.util.RouteArrayAdapter;
import com.glassera.railgadi.ando.util.TrainCache;
import com.glassera.railgadi.entity.TrainInfo;
import com.glassera.railgadi.entity.TrainRoute;
import com.glassera.railgadi.service.RailgadiRegistry;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class RouteDetailActivity extends ListActivity{

	private TrainInfo selectedTrain;
	private ArrayAdapter<TrainRoute> lAdapter;
	private Map<String, TrainRoute> map;
	private ProgressDialog dialog;
	private StringBuilder headerInfo;
	private TextView routeHeader;
	private String trainKey;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.routedetail);
		trainKey = getIntent().getExtras().getString("selectedTrainKey");
		selectedTrain = (TrainInfo)TrainCache.getInstance().getObject(trainKey);
		routeHeader = (TextView)findViewById(R.id.routeDetailHeader);
		
		dialog = ProgressDialog.show(this, "Please Wait", "Getting Route", false);
		
		map = new HashMap<String, TrainRoute>();
		new RouteEventThread().start();
	}
	
	final Handler routeDetailHandler = new Handler() {

		@Override

		public void handleMessage(Message msg) {

			super.handleMessage(msg);
			
			if(selectedTrain != null && selectedTrain.getTrainRoute() != null){
				lAdapter = new RouteArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, android.R.id.text1);
				for(TrainRoute trainRoute : selectedTrain.getTrainRoute()){
					lAdapter.add(trainRoute);
					map.put(trainRoute.getIndex()+"", trainRoute);
				}
				setListAdapter(lAdapter);
				ListView lv = getListView();
				lv.setTextFilterEnabled(true);
				lv.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// When clicked, show a toast with the TextView text
						
					}

				});
			}
			SpannableString sText = new SpannableString(headerInfo);
			routeHeader.setBackgroundColor(Color.GRAY);
			sText.setSpan(new ForegroundColorSpan(Color.WHITE) , 0, headerInfo.length(), SpannableString.SPAN_INCLUSIVE_INCLUSIVE);
			routeHeader.setText(sText);
			
			dialog.dismiss();
		}

	};
	
	private class RouteEventThread extends Thread{
		public void run() {
			try {
				if(selectedTrain.getTrainRoute() == null || selectedTrain.getTrainRoute().isEmpty()){
					selectedTrain = RailgadiRegistry.getTrainService().getRoute(selectedTrain);
					TrainCache.getInstance().updateObject(trainKey, selectedTrain);
				}
				createHeader(selectedTrain);
				routeDetailHandler.sendEmptyMessage(0);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				dialog.dismiss();
				e.printStackTrace();
			}
		}
	}
	
	private void createHeader(TrainInfo train){
		headerInfo = new StringBuilder();
		headerInfo.append(" "+train.getTrainNumber()+" "+train.getTrainName());
		headerInfo.append("\n  Arrival     Departure    Halt     Day    Distance");
	}
}
