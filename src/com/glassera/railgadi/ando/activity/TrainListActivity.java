package com.glassera.railgadi.ando.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.glassera.railgadi.ando.R;
import com.glassera.railgadi.ando.util.TrainListArrayAdapter;
import com.glassera.railgadi.ando.util.TrainCache;
import com.glassera.railgadi.entity.TrainInfo;
import com.glassera.railgadi.service.RailgadiRegistry;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
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

@SuppressLint("HandlerLeak")
public class TrainListActivity extends ListActivity{
	
	private ArrayAdapter<TrainInfo> lAdapter;
	private List<TrainInfo> trains;
	private String fromStation;
	private String toStation;
	private Map<String, TrainInfo> map;
	private ProgressDialog dialog;
	private TextView listHeader;
	private StringBuilder headerInfo;
	private TrainInfo selectedTrain;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.trainlist);
		fromStation = getIntent().getExtras().getString("fromStation");
		toStation = getIntent().getExtras().getString("toStation");
		listHeader = (TextView)findViewById(R.id.listHeader);
		
		dialog = ProgressDialog.show(this, "Please Wait", "Searching Trains", false);
		
		map = new HashMap<String, TrainInfo>();
		new EventThread().start();
	}
	
	final Handler trainListHandler = new Handler() {

		@Override

		public void handleMessage(Message msg) {

			super.handleMessage(msg);
			
			if(trains != null && !trains.isEmpty()){
				lAdapter = new TrainListArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, android.R.id.text1);
				for(TrainInfo train : trains){
					lAdapter.add(train);
					map.put(train.getTrainId()+"", train);
				}
				setListAdapter(lAdapter);
				ListView lv = getListView();
				lv.setTextFilterEnabled(true);
				/*lv.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// When clicked, show a toast with the TextView text
						
					}

				});*/
				
				lv.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent,
							View view, int position, long id) {
						selectedTrain = (TrainInfo)parent.getItemAtPosition(position);
						createServiceDialog(selectedTrain);
					}
				});
				
			}
			SpannableString sText = new SpannableString(headerInfo);
			listHeader.setBackgroundColor(Color.GRAY);
			sText.setSpan(new ForegroundColorSpan(Color.WHITE) , 0, headerInfo.length(), SpannableString.SPAN_INCLUSIVE_INCLUSIVE);
			listHeader.setText(sText);
			
			dialog.dismiss();
		}

	};
	
	private class EventThread extends Thread{
		public void run() {
			try {
				trains = RailgadiRegistry.getTrainService().searchTrains(fromStation, toStation);
				createHeader(trains.size());
				trainListHandler.sendEmptyMessage(0);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				dialog.dismiss();
				e.printStackTrace();
			}
		}
	}
	
	private void createServiceDialog(TrainInfo train){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final String[] items = getResources().getStringArray(R.array.serviceMenuItems);
		builder.setTitle(train.getTrainNumber() + " " + train.getTrainName());
		builder.setItems(items, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int index) {
				//String item = items[index].toString();
				if(index == 0){
					startRouteDetailActivity();
				}else if(index == 1){
					startBerthDetailActivity();
				}
			}
		});
		
		builder.show();
	}
	
	private void createHeader(int records){
		headerInfo = new StringBuilder();
		headerInfo.append("  "+records +" train(s) from "+fromStation + " to "+toStation);
		headerInfo.append("\n  Arr.        Dep.        Travel            Availbility");
	}
	
	private void startRouteDetailActivity(){
		Intent routeDetailIntent = new Intent(this, RouteDetailActivity.class);
		routeDetailIntent.putExtra("selectedTrainKey", selectedTrain.getTrainId());
		TrainCache.getInstance().putObject(selectedTrain.getTrainId(), selectedTrain);
		startActivity(routeDetailIntent);
	}
	
	private void startBerthDetailActivity(){
		Intent berthDetailIntent = new Intent(this, BerthDetailActivity.class);
		berthDetailIntent.putExtra("selectedTrainKey", selectedTrain.getTrainId());
		TrainCache.getInstance().putObject(selectedTrain.getTrainId(), selectedTrain);
		startActivity(berthDetailIntent);
	}
	 
}
