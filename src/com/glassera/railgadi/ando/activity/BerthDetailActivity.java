package com.glassera.railgadi.ando.activity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.glassera.railgadi.ando.R;
import com.glassera.railgadi.ando.util.TrainCache;
import com.glassera.railgadi.entity.TrainInfo;
import com.glassera.railgadi.service.RailgadiRegistry;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class BerthDetailActivity extends Activity{

	private EditText journeyDate;
	private Spinner quotaSpinner;
	private Button availBtn;

	private ProgressDialog dialog;

	private int year;
	private int month;
	private int day;

	private TrainInfo selectedTrain;
	private String trainKey;

	static final int DATE_DIALOG_ID = 999;

	private TextView date1;
	private TextView date2;
	private TextView date3;
	private TextView date4;
	private TextView date5;
	private TextView date6;
	//private TextView date7;
	private TextView trainInfo;
	private TextView quotaInfo;
	private TextView helpInfo;

	OnClickListener availBtnClickListener;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.berthavail);

		trainKey = getIntent().getExtras().getString("selectedTrainKey");
		selectedTrain = (TrainInfo)TrainCache.getInstance().getObject(trainKey);

		journeyDate = (EditText)findViewById(R.id.journeyDate);
		availBtn = (Button)findViewById(R.id.availBtn);

		journeyDate.setInputType(InputType.TYPE_NULL);

		quotaSpinner = (Spinner)findViewById(R.id.quota);

		date1 = (TextView)findViewById(R.id.date1);
		date2 = (TextView)findViewById(R.id.date2);
		date3 = (TextView)findViewById(R.id.date3);
		date4 = (TextView)findViewById(R.id.date4);
		date5 = (TextView)findViewById(R.id.date5);
		date6 = (TextView)findViewById(R.id.date6);
		//date7 = (TextView)findViewById(R.id.date7);
		
		trainInfo = (TextView)findViewById(R.id.trainInfo);
		quotaInfo = (TextView)findViewById(R.id.quotaInfo);
		helpInfo = (TextView)findViewById(R.id.helpInfo);
		
		trainInfo.setText(selectedTrain.getTrainNumber()+" "+selectedTrain.getTrainName());

		final Calendar c = Calendar.getInstance();

		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);


		setJourneyDate();
		setAvailListner();
		if(selectedTrain != null && selectedTrain.getAvailibility() != null && !selectedTrain.getAvailibility().isEmpty()){
			availHandler.sendEmptyMessage(0);
		}
	}

	final Handler availHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			populateData();

			createDateClickListner(date1,0);
			createDateClickListner(date2,1);
			createDateClickListner(date3,2);
			createDateClickListner(date4,3);
			createDateClickListner(date5,4);
			createDateClickListner(date6,5);
			//createDateClickListner(date7,6);
		}
	};

	private void setJourneyDate(){
		journeyDate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				showDialog(DATE_DIALOG_ID);
			}
		});
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			// set date picker as current date
			return new DatePickerDialog(this, datePickerListener, 
					year, month,day);
		}
		return null;
	}

	private DatePickerDialog.OnDateSetListener datePickerListener 
	= new DatePickerDialog.OnDateSetListener() {

		// when dialog box is closed, below method will be called.
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {
			year = selectedYear;
			month = selectedMonth;
			day = selectedDay;

			// set selected date into textview
			Calendar journeyDateCal = Calendar.getInstance();
			journeyDateCal.set(year, month, day);
			DateFormat df = new SimpleDateFormat("dd MMM yyyy, EEEE");

			journeyDate.setText(df.format(journeyDateCal.getTime()));

		}
	};

	private void setAvailListner(){
		availBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(journeyDate.getText() != null && journeyDate.getText().length() != 0){
					showProgress("Please Wait", "Getting availibility detail");
					new BerthAvailableCall().start();
				}else{
					CharSequence[] messages = {"Please select travel date"};
					alertMessage("Alert", messages);
				}
				
			}
		});
	}

	private void showProgress(String title, String message){
		dialog = ProgressDialog.show(this, title, message, false);
	}

	private class BerthAvailableCall extends Thread{
		public void run() {
			try {
				System.out.println("Berth Avail Check Thread Started");
				selectedTrain.setJourneyDay(day+"");
				selectedTrain.setJourneyMonth(month+1+"");
				selectedTrain.setQuota(getQuotaCode(quotaSpinner.getSelectedItem().toString()));
				selectedTrain = RailgadiRegistry.getTrainService().getBerthAvailibility(selectedTrain);
				TrainCache.getInstance().updateObject(trainKey, selectedTrain);
				availHandler.sendEmptyMessage(0);
				dialog.dismiss();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				dialog.dismiss();
				e.printStackTrace();
			}
		}
	}

	private String getQuotaCode(String quotaName){
		String quotaCode = "GN";
		if(quotaName.equals("General")){
			quotaCode = "GN";
		}else if(quotaName.equals("Tatkal")){
			quotaCode = "CK";
		}else if(quotaName.equals("Ladies")){
			quotaCode = "LD";
		}
//		else if(quotaName.equals("Foreign")){
//			quotaCode = "FT";
//		}else if(quotaName.equals("Defence")){
//			quotaCode = "DF";
//		}else if(quotaName.equals("Lower Berth")){
//			quotaCode = "SS";
//		}else if(quotaName.equals("Yuva")){
//			quotaCode = "YU";
//		}else if(quotaName.equals("Handicaped")){
//			quotaCode = "HP";
//		}else if(quotaName.equals("Head quarters")){
//			quotaCode = "HO";
//		}else if(quotaName.equals("Duty Pass")){
//			quotaCode = "DP";
//		}else if(quotaName.equals("Parliament")){
//			quotaCode = "PH";
//		}
		return quotaCode;
	}

	private void populateData(){
		quotaInfo.setText("Availibility for "+getQuotaString(selectedTrain.getQuota())+" quota");
		helpInfo.setText("Tap on date to see availibility");
		date1.setText(selectedTrain.getAvailibility().get(0).getDate());
		date2.setText(selectedTrain.getAvailibility().get(1).getDate());
		date3.setText(selectedTrain.getAvailibility().get(2).getDate());
		date4.setText(selectedTrain.getAvailibility().get(3).getDate());
		date5.setText(selectedTrain.getAvailibility().get(4).getDate());
		date6.setText(selectedTrain.getAvailibility().get(5).getDate());
		//date7.setText("Availibility on : "+selectedTrain.getAvailibility().get(6).getDate());
	}
	
	private static String getQuotaString(String quotaCode){
		String value = "";
		if(quotaCode.equals("GN")){
			value = "General";
		}else if(quotaCode.equals("CK")){
			value = "Tatkal";
		}else if(quotaCode.equals("LD")){
			value = "Ladies";
		}
		return value;
	}

	private void createDateClickListner(TextView dateText, final int index){
		dateText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				createAvailibilityDialog(selectedTrain, index);
			}
		});
	}

	private void createAvailibilityDialog(TrainInfo train, int index){
		AlertDialog.Builder builder = new AlertDialog.Builder(BerthDetailActivity.this);
		final CharSequence[] items = new CharSequence[7];
		items[0] = "SL  : "+(train.getAvailibility().get(index).getSleeper() != null ? train.getAvailibility().get(index).getSleeper() : "N/A");
		items[1] = "3AC : "+(train.getAvailibility().get(index).getThirdAC() != null ? train.getAvailibility().get(index).getThirdAC() : "N/A");
		items[2] = "2AC : "+(train.getAvailibility().get(index).getSecondAC() != null ? train.getAvailibility().get(index).getSecondAC() : "N/A");
		items[3] = "1AC : "+(train.getAvailibility().get(index).getFirstAC() != null ? train.getAvailibility().get(index).getFirstAC() : "N/A");
		items[4] = "CC  : "+(train.getAvailibility().get(index).getChariCarAC() != null ? train.getAvailibility().get(index).getChariCarAC() : "N/A");
		items[5] = "EAC : "+(train.getAvailibility().get(index).getThirdACEconomy() != null ? train.getAvailibility().get(index).getThirdACEconomy() : "N/A");
		items[6] = "SS : "+(train.getAvailibility().get(index).getSecondSeat() != null ? train.getAvailibility().get(index).getSecondSeat() : "N/A");


		builder.setTitle(train.getTrainNumber() + " " + train.getTrainName());
		builder.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
			}
		});
		builder.setCancelable(true);
		builder.show();
	}
	
	private void alertMessage(String title, CharSequence[] messages){
		AlertDialog.Builder messageDialog = new AlertDialog.Builder(BerthDetailActivity.this);
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

}
