package com.example.yapp.action;
import java.util.List;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.yapp.R;
import com.example.yapp.model.Movie;
import com.example.yapp.model.StoreProduct;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomFragmentAdapter extends BaseAdapter {

    Context context;
    List<StoreProduct> rowItem;
    
    private LayoutInflater inflater;
    private List<StoreProduct> storeItems;
    
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public CustomFragmentAdapter(Context context, List<StoreProduct> storeItems) {
        this.context = context;
        this.storeItems = storeItems;

    }

    @Override
    public int getCount() {

        return storeItems.size();
    }

    @Override
    public Object getItem(int position) {

        return storeItems.get(position);
    }

    @Override
    public long getItemId(int position) {

        return storeItems.indexOf(getItem(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

    	 if (inflater == null)
             inflater = (LayoutInflater) context
                     .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
         if (convertView == null)
             convertView = inflater.inflate(R.layout.list_row, null);
  
         if (imageLoader == null)
             imageLoader = AppController.getInstance().getImageLoader();
//         NetworkImageView thumbNail = (NetworkImageView) convertView
//                 .findViewById(R.id.thumbnail);
         NetworkImageView productImage = (NetworkImageView) convertView
                 .findViewById(R.id.productImage);
//         ImageView productImage = (ImageView) convertView
//                 .findViewById(R.id.productImage);
         TextView storeName = (TextView) convertView.findViewById(R.id.storeName);
         TextView productName = (TextView) convertView.findViewById(R.id.productName);
         TextView discountPercent = (TextView) convertView.findViewById(R.id.discountPercent);
         TextView originPrice = (TextView) convertView.findViewById(R.id.originPrice);
         TextView discountPrice = (TextView) convertView.findViewById(R.id.discountPrice);
         TextView leaveTime = (TextView) convertView.findViewById(R.id.leaveTime);
  
         // getting movie data for the row
         StoreProduct m = storeItems.get(position);
  
         // thumbnail image
         productImage.setImageUrl(m.getProductImage(), imageLoader);
//         productImage.setImageResource(R.id.icon);
         storeName.setText(m.getStoreName());
         productName.setText(m.getProductName());
         discountPercent.setText(m.getDiscountPercent() + "%");
         originPrice.setText(m.getOriginPrice());
         originPrice.setPaintFlags(originPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
         discountPrice.setText(m.getDiscountPrice());
         leaveTime.setText(String.valueOf(m.getTime()));
  
         return convertView;

    }

}
