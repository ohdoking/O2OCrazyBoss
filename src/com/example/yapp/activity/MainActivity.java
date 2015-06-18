package com.example.yapp.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.yapp.MapActivity;
import com.example.yapp.MyDataShared;
import com.example.yapp.PushSetting;
import com.example.yapp.R;
import com.example.yapp.action.CustomListAdapter;
import com.example.yapp.model.Movie;
import com.example.yapp.model.StoreProduct;
import com.example.yapp.value.Yapp;
import com.melnykov.fab.FloatingActionButton;

public class MainActivity extends Activity implements OnItemClickListener {
    // Log tag
    private static final String TAG = MainActivity.class.getSimpleName();
 
    // Movies json url
    private ProgressDialog pDialog;
    private List<StoreProduct> StoreList = new ArrayList<StoreProduct>();
    private ListView listView;
    private CustomListAdapter adapter;
 
    private FloatingActionButton floatingActionButton;
    MyDataShared mds;
    

	private String storeId;// = {"101","102","103"};
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
 
        mds = new MyDataShared(getApplicationContext());
        listView = (ListView) findViewById(R.id.list);
        adapter = new CustomListAdapter(this, StoreList);
        listView.setAdapter(adapter);
 
        storeId = mds.getValue("userComerSrl", null);
        Log.i("ohdokingStoreId",storeId);
        listView.setOnItemClickListener(this);
        
      
 
        // changing action bar color
        getActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.parseColor("#1b1b1b")));
        
        floatingActionButton = (FloatingActionButton) findViewById(R.id.addButton);
        final Context scope = this;
        floatingActionButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(scope, "FloatingActionButton clicked", Toast.LENGTH_SHORT).show();
                
            	Intent i = new Intent(getApplicationContext(),MapActivity.class);
            	i.putExtra("map", 2);
            	startActivity(i);
            }
        });
 
        // Creating volley request obj
       /* JsonArrayRequest movieReq = new JsonArrayRequest(url+"/home/getHome",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        hidePDialog();
 
                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {
 
                                JSONObject obj = response.getJSONObject(i);
                                Movie movie = new Movie();
                                movie.setTitle(obj.getString("title"));
                                movie.setThumbnailUrl(obj.getString("image"));
                                movie.setRating(((Number) obj.get("rating"))
                                        .doubleValue());
                                movie.setYear(obj.getInt("releaseYear"));
 
                                // Genre is json array
                                JSONArray genreArry = obj.getJSONArray("genre");
                                ArrayList<String> genre = new ArrayList<String>();
                                for (int j = 0; j < genreArry.length(); j++) {
                                    genre.add((String) genreArry.get(j));
                                }
                                movie.setGenre(genre);
 
                                // adding movie to movies array
                                movieList.add(movie);
 
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
 
                        }
 
                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                        hidePDialog();
 
                    }
                });
 
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);*/
        getProductList(storeId);
        
        
      
        
       
        
    }
    
    private void getProductList(final String storeId) {
    	
    	  pDialog = new ProgressDialog(this);
          // Showing progress dialog before making http request
          pDialog.setMessage("Loading...");
          pDialog.show();
          
          
          
		String url = Yapp.HOME_URL + "home/getHome ";
		RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());

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
				            
				            StoreList.add(store);
				            
				            
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
				Toast.makeText(getApplicationContext(), "network error: " + arg0,
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

			/*	for (String string : storeId) {
					tempStoreId.append(string);
					
					tempStoreId.append(",");
				}*/
				
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
 
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
//		getMenuInflater().inflate(R.menu.main, menu);
//		return true;
//		
		super.onCreateOptionsMenu(menu);
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main, menu);

	    

	    return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_map) {
			Intent i = new Intent(MainActivity.this,MapActivity.class);
			startActivity(i);
			return true;
		}
		/*else if (id == R.id.action_push) {
			Intent i = new Intent(MainActivity.this,PushActivity.class);
			startActivity(i);
			return true;
		}*/
		else if (id == R.id.action_admin) {
			Intent i = new Intent(MainActivity.this,TabActivity.class);
			startActivity(i);
			return true;
		}
		
		return super.onOptionsItemSelected(item);
		
		
	}
	

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// arg2 = the id of the item in our view (List/Grid) that we clicked
		// arg3 = the id of the item that we have clicked
		// if we didn't assign any id for the Object (Book) the arg3 value is 0
		// That means if we comment, aBookDetail.setBookIsbn(i); arg3 value become 0
		Toast.makeText(getApplicationContext(), "You clicked on position : " + arg2 + " and id : " + arg3, Toast.LENGTH_LONG).show();
		
	}
	
}