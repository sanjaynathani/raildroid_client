package com.glassera.railgadi.ando.util;


import com.glassera.railgadi.entity.TrainInfo;

import android.R.style;
import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TrainListArrayAdapter extends ArrayAdapter<TrainInfo>{

	private final String[] days = {"M","T","W","T","F","S","S"};
	public TrainListArrayAdapter(Context context, int resource,
			int textViewResourceId) {
		super(context, resource, textViewResourceId);
	}
	
	 @Override  
	 public View getView(int position, View view, ViewGroup viewGroup)
	 {
	     TextView tv = (TextView) super.getView(position, view, viewGroup);
	   
	     TrainInfo trainInfo = this.getItem(position);
	     
	     StringBuilder headerString = new StringBuilder();
	     headerString.append(trainInfo.getTrainNumber());
	     headerString.append(" ");
	     headerString.append(trainInfo.getTrainName());
	     headerString.append(" - ");
	     headerString.append(trainInfo.getOrigin() + " To " + trainInfo.getDestination());
	     headerString.append("\n" + trainInfo.getArrivalTime());
	     headerString.append("  ");
	     headerString.append(trainInfo.getDepartureTime());
	     headerString.append("  ");
	     headerString.append(trainInfo.getTravelHour());
	     headerString.append("      ");
	     
	     StringBuilder availableDayString = new StringBuilder();
	     
	     char[] strArray = trainInfo.getAvailableDays().toString().toCharArray(); 
	     availableDayString.append(trainInfo.getAvailableDays());
	     
	     int startlen = 0;
	     
	     for (int i=0; i<strArray.length; i++){
	    	 if(i==0){
	    		 startlen = headerString.length();
	    	 }
	    	 headerString.append(days[i]+" ");
	     }
	     headerString.toString().trim();
	     
	     int finalLen = headerString.length() - startlen;
	     
	     SpannableString sText = new SpannableString(headerString);
	     sText.setSpan(new TextAppearanceSpan(getContext(), style.TextAppearance_Medium),  0, headerString.length(), SpannableString.SPAN_INCLUSIVE_INCLUSIVE);
	     
	     for (int i=0; i<strArray.length; i++){
	    	 if(strArray[i] == 'Y'){
	    		 sText.setSpan(new ForegroundColorSpan(Color.GREEN), (headerString.length() - (finalLen - i)), (headerString.length() - (finalLen - i) + 1), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
	    	 }else{
	    		 sText.setSpan(new ForegroundColorSpan(Color.RED), (headerString.length() - (finalLen - i)), (headerString.length() - (finalLen - i) + 1), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
	    	 }
	    	 finalLen--;
	     }
	     tv.setText(sText);
	     return tv;
	 }

}
