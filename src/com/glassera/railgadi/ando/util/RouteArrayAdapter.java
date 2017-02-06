package com.glassera.railgadi.ando.util;

import android.R.style;
import android.content.Context;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.glassera.railgadi.entity.TrainRoute;

public class RouteArrayAdapter extends ArrayAdapter<TrainRoute>{

	public RouteArrayAdapter(Context context, int resource,
			int textViewResourceId) {
		super(context, resource, textViewResourceId);
	}

	@Override  
	public View getView(int position, View view, ViewGroup viewGroup)
	{
		TextView tv = (TextView) super.getView(position, view, viewGroup);
		
		TrainRoute trainRoute = this.getItem(position);

		int arrLen = 8;
		int dpLen = 12;
		int haltLen = 8;
		int dayLen = 6;

		StringBuilder routeString = new StringBuilder();
		routeString.append(trainRoute.getStationCode()+" - "+trainRoute.getStationName());
		routeString.append("\n");
		routeString.append("                                           ");
		int start = routeString.length()-43;
		int strlen = routeString.length()-43;
		routeString.insert(start, trainRoute.getArrivalTime());
		start = strlen+arrLen;
		routeString.insert(start, trainRoute.getDepartureTime());
		start = strlen+arrLen+dpLen;
		routeString.insert(start, trainRoute.getHaltTime());
		start = strlen+arrLen+dpLen+haltLen;
		routeString.insert(start, trainRoute.getDay());
		start = strlen+arrLen+dpLen+haltLen+dayLen;
		routeString.insert(start, trainRoute.getDistance());
		routeString.trimToSize();


		SpannableString sText = new SpannableString(routeString);
		sText.setSpan(new TextAppearanceSpan(getContext(), style.TextAppearance_Medium),  0, routeString.length(), SpannableString.SPAN_INCLUSIVE_INCLUSIVE);

		tv.setText(sText);
		return tv;
	}
}
