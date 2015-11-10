package com.mtm.ksu.mobile.activitytask;

import java.text.NumberFormat;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mtm.ksu.mobile.activity.R;
import com.mtm.ksu.mobile.model.Rekening;

// here's our beautiful adapter
public class ArrayAdapterAngsuranItem extends ArrayAdapter<Rekening> {

    Context mContext;
    int layoutResourceId;
    private final List<Rekening> itemsArrayList;
    Rekening data[] = null;

    public ArrayAdapterAngsuranItem(Context mContext, int layoutResourceId, List<Rekening> itemsArrayList) {

        super(mContext, layoutResourceId, itemsArrayList);
        
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.itemsArrayList = itemsArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

    	/*
		 * The convertView argument is essentially a "ScrapView" as described is Lucas post 
		 * http://lucasr.org/2012/04/05/performance-tips-for-androids-listview/
		 * It will have a non-null value when ListView is asking you recycle the row layout. 
		 * So, when convertView is not null, you should simply update its contents instead of inflating a new row layout.
    	 */
    	/*
        if(convertView==null){
	        // inflate the layout
	        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
	        convertView = inflater.inflate(layoutResourceId, parent, false);
        }
        
        // object item based on the position
        Rekening objectItem = data[position];
        
        // get the TextView and then set the text (item name) and tag (item ID) values
        TextView textViewItem = (TextView) convertView.findViewById(R.id.textViewItem);
        textViewItem.setText(objectItem.getNoRekening() + " - " +objectItem.getNamaNasabah());
        textViewItem.setTag(objectItem.getNoRekening());

        return convertView;
        */
    	
    	// 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) mContext
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        View rowView = inflater.inflate(layoutResourceId, parent, false);

        // 3. Get the two text view from the rowView
        TextView rekeningView = (TextView) rowView.findViewById(R.id.textViewItemRekening);
        TextView nasabahView = (TextView) rowView.findViewById(R.id.textViewItemNasabah);
       
        // 4. Set the text for textView
        rekeningView.setText(itemsArrayList.get(position).getNoRekening());
        nasabahView.setText("  -  " + itemsArrayList.get(position).getNamaNasabah());
        NumberFormat nf = NumberFormat.getInstance();
		
		
        // 5. retrn rowView
        return rowView;
        
    }
	
}
