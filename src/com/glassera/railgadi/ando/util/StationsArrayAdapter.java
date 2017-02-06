package com.glassera.railgadi.ando.util;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

public class StationsArrayAdapter extends ArrayAdapter<String> implements Filterable{

	private NameFilter filter;
	private List<String> items;
	private List<String> filtered;

	public StationsArrayAdapter(Context context, 
			int textViewResourceId, List<String> objects) {
		super(context, textViewResourceId, objects);
		items = objects;
	}

	public Filter getFilter()
	{
		if(filter == null)
			filter = new NameFilter();
		return filter;
	}
	
	public void add(String object) {
		items.add(object);
	    this.notifyDataSetChanged();
	}
	
	@Override
	public void notifyDataSetChanged() {
	    super.notifyDataSetChanged();
	}
	
	@Override
	public String getItem(int position) {
	    return filtered.get(position);
	}

	private class NameFilter extends Filter
	{
		@Override
	    protected FilterResults performFiltering(CharSequence constraint) {
	        FilterResults results = new FilterResults();

	        if(constraint == null || constraint.length() == 0) {
	            ArrayList<String> list = new ArrayList<String>(items);
	            results.values = list;
	            results.count = list.size();
	        } else {
	            ArrayList<String> newValues = new ArrayList<String>();
	            for(int i = 0; i < items.size(); i++) {
	                String item = items.get(i);
	                if(item != null && item.toLowerCase().contains(constraint.toString().toLowerCase())) {
	                    newValues.add(item);
	                }
	            }
	            results.values = newValues;
	            results.count = newValues.size();
	        }       

	        return results;
	    }

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint, FilterResults results) {
			// NOTE: this function is *always* called from the UI thread.

			filtered = (List<String>)results.values;
			notifyDataSetChanged();
			//clear();
			//for (int i = 0, l = filtered.size(); i < l; i++)
			//	add(filtered.get(i));
			//notifyDataSetInvalidated();

		}

	}

	@Override
	public int getCount() {
		return filtered.size();  
	}



}
