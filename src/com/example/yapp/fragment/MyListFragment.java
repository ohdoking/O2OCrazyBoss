package com.example.yapp.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.yapp.R;
import com.example.yapp.action.CustomFragmentAdapter;
import com.example.yapp.activity.AddItemActivity;
import com.example.yapp.model.Movie;
import com.example.yapp.model.StoreProduct;
import com.example.yapp.value.Yapp;
import com.melnykov.fab.FloatingActionButton;

public class MyListFragment extends ListFragment {

	String[] menutitles;
	TypedArray menuIcons;

	CustomFragmentAdapter adapter;
	private List<StoreProduct> rowItems;
	private List<StoreProduct> storeList = new ArrayList<StoreProduct>();
	ListView lv;
	
	 private FloatingActionButton floatingActionButton;
	 private ProgressDialog pDialog;
	 
	 private String storeId = "101";
	 
	 
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View layout = super.onCreateView(inflater, container,
				savedInstanceState);
/*		lv = (ListView) layout.findViewById(android.R.id.list);
		ViewGroup parent = (ViewGroup) lv.getParent();

		// Remove ListView and add CustomView in its place
		int lvIndex = parent.indexOfChild(lv);
		parent.removeViewAt(lvIndex);
		FrameLayout mLinearLayout = (FrameLayout) inflater.inflate(
				R.layout.admin_list_fragment, container, false);
		parent.addView(mLinearLayout, lvIndex, lv.getLayoutParams());*/

		
	        
		return layout;

		// return inflater.inflate(R.layout.admin_list_fragment, null, false);
	}

	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setHasOptionsMenu(true);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	    // Do something that differs the Activity's menu here
	    super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case R.id.add_item:
	    	
	    	Intent i = new Intent(getActivity().getApplicationContext(),AddItemActivity.class);
	    	
	    	startActivity(i);
	    	
	        return false;
	    default:
	        break;
	    }

	    return false;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);
	
	/*	ArrayList al = new ArrayList<Movie>();

		for (int i = 0; i < 10; i++) {
			Movie movie = new Movie();
			movie.setTitle("title" + i);
			// movie.setThumbnailUrl(R.drawable.ic_launcher);
			movie.setRating(((Number) i).doubleValue());
			movie.setYear(2000 + i);

			// Genre is json array
			ArrayList<String> genre = new ArrayList<String>();

			genre.add("Drama" + i);
			genre.add("Thriller" + i);

			movie.setGenre(genre);

			// adding movie to movies array
			movieList.add(movie);

		}*/

		// notifying list adapter about data changes
		// so that it renders the list view with updated data
	

		/*
		 * 
		 * adapter = new CustomFragmentAdapter(getActivity(), movieList);
		 * setListAdapter(adapter); getListView().setOnItemClickListener(this);
		 */
/*		
		setEmptyText("Your emptyText message");

		setListAdapter(adapter);

		setListShown(false);

		getListView().setOnItemClickListener(this);
		
		adapter.notifyDataSetChanged();*/
		
		
		
		getProductList(storeId);
	        
		    adapter = new CustomFragmentAdapter(getActivity(), storeList);
		    setListAdapter(adapter);
	}

	@Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        super.onListItemClick(l, v, position, id);
        Toast.makeText(getActivity(), storeList.get(position).getStoreName(), Toast.LENGTH_SHORT)
		.show();
    }
	
	private void getProductList(final String storeId) {
    	
  	  pDialog = new ProgressDialog(getActivity());
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();
        
        
        
		String url = Yapp.HOME_URL + "home/getHome ";
		RequestQueue mQueue = Volley.newRequestQueue(getActivity());

		Listener<String> listener = new Listener<String>() {

			@Override
			public void onResponse(String result) {
				try {
					JSONObject output = new JSONObject(result);
					final String value = output.getString("sending");

					StringBuffer sb = new StringBuffer();
					
					JSONArray outputData = new JSONArray(output.getString("data"));
					
					for (int i = 0; i < outputData.length(); i++){
					      JSONObject outputTemp = outputData.getJSONObject(i);
					      
					      StoreProduct store = new StoreProduct();
				            store.setProductImage(Yapp.HOME_URL+"uploads/"+outputTemp.getString("productImage"));
				            store.setDiscountPercent(outputTemp.getString("productDisCountPer"));
				            store.setDiscountPrice(outputTemp.getString("productSalePrice") + " ¿ø");
				            store.setOriginPrice(outputTemp.getString("productPrice") + " ¿ø");
				            store.setProductName(outputTemp.getString("productName"));
				            store.setStoreName(outputTemp.getString("comerName"));
				            
				            long dv = Long.valueOf(outputTemp.getString("productTimeOut"))*1000;// its need to be in milisecond
				            Date df = new java.util.Date(dv);
				            String vv = new SimpleDateFormat("MM dd, yyyy hh:mma").format(df);
				            
				            store.setTime(vv);
				            
				            Log.i("ohdoking","indo");
				            
				            storeList.add(store);
				            
				            
					   }
					
					
					
					Log.i("ohdoking","outdo");

				        // notifying list adapter about data changes
				        // so that it renders the list view with updated data
				        adapter.notifyDataSetChanged();
				        hidePDialog();
				        
				        

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

		};// listener

		ErrorListener errorListener = new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				Toast.makeText(getActivity(), "network error: " + arg0,
						Toast.LENGTH_SHORT).show();
				System.out.println("network error: " + arg0);
			}

		};// ErrorListener

		StringRequest request = new StringRequest(Method.POST, url, listener,
				errorListener) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> params = new HashMap<String, String>();
				params.put("Content-Type", "x-www-form-urlencoded");
				
				StringBuilder tempStoreId = new StringBuilder();
				params.put("comerSrl", storeId);

				return params;
			}

		};// StringRequest

		mQueue.add(request);
	}
  
  

  @Override
  public void onDestroy() {
      super.onDestroy();
      hidePDialog();
  }

  private void hidePDialog() {
      if (pDialog != null) {
          pDialog.dismiss();
          pDialog = null;
      }
  }

}