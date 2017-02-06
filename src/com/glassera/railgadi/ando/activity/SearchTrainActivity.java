package com.glassera.railgadi.ando.activity;


import com.glassera.railgadi.ando.R;
import com.glassera.railgadi.ando.util.StationsArrayAdapter;
import com.glassera.railgadi.ando.util.StationsData;
import com.glassera.railgadi.ando.util.TrainCache;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

public class SearchTrainActivity extends Activity{
	/** Called when the activity is first created. */
	
	private Button searchButton;
	private Button swapButoon;
	private Button resetButton;
	private AutoCompleteTextView fromText;
	private AutoCompleteTextView toText;
	private String fromStation;
	private String toStation;
	private StationsArrayAdapter stationArrayAdapter;
	private ConnectivityManager conMgr;

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.searchtrain);
		conMgr =  (ConnectivityManager)this.getSystemService(CONNECTIVITY_SERVICE);
		searchButton = (Button)findViewById(R.id.searchTrainBtn);
		swapButoon = (Button)findViewById(R.id.swapBtn);
		resetButton = (Button)findViewById(R.id.resetBtn);
		fromText = (AutoCompleteTextView)findViewById(R.id.fromAutoComplete);
		toText = (AutoCompleteTextView)findViewById(R.id.toAutoComplete);
		
		setAutoComplete();
		searchButtonEvent();
		swapEvent();
		resetEvent();
		
	}
	
	private void setAutoComplete(){
		try{
			stationArrayAdapter = new StationsArrayAdapter(this,
					android.R.layout.simple_dropdown_item_1line, StationsData.getStationList(false, getAssets()));
			
			AutoCompleteTextView fromView = (AutoCompleteTextView)
					findViewById(R.id.fromAutoComplete);
			
			fromView.setAdapter(stationArrayAdapter);
			
			AutoCompleteTextView toView = (AutoCompleteTextView)
					findViewById(R.id.toAutoComplete);
			toView.setAdapter(stationArrayAdapter);
			
			fromView.addTextChangedListener(new TextWatcher() {
				
				@Override
				public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
						int arg3) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void afterTextChanged(Editable arg0) {
					try{
						if((fromText.getText() != null && !fromText.getText().toString().equals(" "))){
							fromStation = fromText.getText().toString().split(" ")[0];
						}
					}catch(Exception e){
						
					}
					
				}
			});
			
			toView.addTextChangedListener(new TextWatcher() {
				
				@Override
				public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
						int arg3) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void afterTextChanged(Editable arg0) {
					try{
						if((toText.getText() != null && !toText.getText().toString().equals(" "))){
							toStation = toText.getText().toString().split(" ")[0];
						}
					}catch(Exception e){
						
					}
					
				}
			});
		}catch(Exception e){
			e.printStackTrace();
		}
	}


	private void searchButtonEvent(){
		searchButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if((fromText.getText() != null && fromText.getText().length() != 0)
						&& (toText.getText() != null && toText.getText().length() != 0)){
					if(checkConnection()){
						TrainCache.getInstance().clearCache();
						startListTrainActivity();
					}
				}else{
					CharSequence[] messages = {"Please provide valid inputs"};
					alertMessage("Alert", messages);
				}
			}
		});
	}
	
	private void startListTrainActivity(){
		Intent listTrainIntent = new Intent(this, TrainListActivity.class);
		listTrainIntent.putExtra("fromStation", fromStation);
		listTrainIntent.putExtra("toStation", toStation);
		startActivity(listTrainIntent);
	}
	
	private void swapEvent(){
		swapButoon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String temp = fromText.getText().toString(); 
				fromText.setText(toText.getText().toString());
				toText.setText(temp);
				temp = null;
			}
		});
	}
	
	private void resetEvent(){
		resetButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				fromText.setText("");
				toText.setText("");
			}
		});
	}
	
	private void alertMessage(String title, CharSequence[] messages){
		AlertDialog.Builder messageDialog = new AlertDialog.Builder(SearchTrainActivity.this);
		messageDialog.setTitle(title);
		messageDialog.setItems(messages, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}
		});
		messageDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				arg0.dismiss();
			}
		});
		messageDialog.show();
	}
	
	private boolean checkConnection(){
		boolean connected = true;
		if(conMgr.getActiveNetworkInfo() == null || !conMgr.getActiveNetworkInfo().isConnected() || !conMgr.getActiveNetworkInfo().isAvailable()){
			CharSequence[] messages = {"No internet connection"};
			alertMessage("Alert", messages);
			connected = false;
		}
		return connected;
	}

}