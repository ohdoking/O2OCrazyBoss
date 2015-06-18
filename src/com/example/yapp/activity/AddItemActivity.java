package com.example.yapp.activity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.yapp.R;
import com.example.yapp.action.PhotoMultipartRequest;
import com.example.yapp.model.StoreProduct;
import com.example.yapp.value.Yapp;

public class AddItemActivity extends Activity {

	// LogCat tag
	private static final String TAG = MainActivity.class.getSimpleName();

	// Camera activity request codes
	private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;

	public static final int MEDIA_TYPE_IMAGE = 1;

	private Uri fileUri; // file url to store image/video
	private ProgressDialog pDialog;

	private ImageView btnCapturePicture;
	private TextView productName;
	private TextView originPrice;
	private TextView discountPrice;
	private TextView discountPercent;
	
	private Spinner remainderTime;
	private Spinner finishTime;

	private Button uploadBtn;
	private Button cancelBtn;

	
	private File imgFile;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_item);

		// Changing action bar background color
		// These two lines are not needed
		getActionBar().setBackgroundDrawable(
				new ColorDrawable(Color.parseColor(getResources().getString(
						R.color.accent))));

		btnCapturePicture = (ImageView) findViewById(R.id.uploadImgPreview);
		productName = (TextView) findViewById(R.id.uploadProductName);
		originPrice = (TextView) findViewById(R.id.uploadOriginPrice);
		discountPrice = (TextView) findViewById(R.id.uploadDiscountPrice);
		discountPercent = (TextView) findViewById(R.id.uploadDiscountPercent);

		
		uploadBtn = (Button) findViewById(R.id.uploadBtn);
		cancelBtn = (Button) findViewById(R.id.uploadCancleBtn);

		remainderTime = (Spinner)findViewById(R.id.uploadTime);
		finishTime = (Spinner)findViewById(R.id.uploadFinishTime);
		
		String[] items = new String[]{"1 시간 전", "2 시간 전", "3 시간 전", "4 시간 전", "5 시간 전", "6 시간 전" , "12 시간 전", "18 시간 전" , "24 시간 전"};
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item , items);
		remainderTime.setAdapter(adapter);
		
		String[] items2 = new String[]{"1 시간 전", "2 시간 전", "3 시간 전", "4 시간 전", "5 시간 전", "6 시간 전","7 시간 전", "8시간 전" , "9시간 전", "10 시간 전", "11시간 전", "12 시간 전", "18 시간 전" , "24 시간 전"};
		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item , items2);
		finishTime.setAdapter(adapter2);
		
		uploadBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				uploadProduct();
			}
		});

		cancelBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		/**
		 * Capture image button click event
		 */
		btnCapturePicture.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// capture picture
				captureImage();
			}
		});


		// Checking camera availability
		if (!isDeviceSupportCamera()) {
			Toast.makeText(getApplicationContext(),
					"Sorry! Your device doesn't support camera",
					Toast.LENGTH_LONG).show();
			// will close the app if the device does't have camera
			finish();
		}
		
		discountPrice.addTextChangedListener(new TextWatcher() {

	        @Override
	        public void onTextChanged(CharSequence s, int start, int before, int count) {


	        }

	        @Override
	        public void beforeTextChanged(CharSequence s, int start, int count,
	                int after) {
	            // TODO Auto-generated method stub

	        }

	        @Override
	        public void afterTextChanged(Editable s) {
	            // TODO Auto-generated method stub
	        	if (discountPrice.getText().toString().length() != 0) {
	        		
	        		Float tempDiscount = Float.parseFloat(discountPrice.getText().toString()) / Float.parseFloat(originPrice.getText().toString());
            		discountPercent.setText(String.valueOf(Math.round((1-tempDiscount) * 100)) + " % ");
            		
            	}
	        }
	    });
		
		discountPercent.addTextChangedListener(new TextWatcher() {

	        @Override
	        public void onTextChanged(CharSequence s, int start, int before, int count) {

	                          if(s.toString().trim().length()==0){
	                        	  uploadBtn.setEnabled(false);
	                           } else {
	                        	   uploadBtn.setEnabled(true);
	                            }


	        }

	        @Override
	        public void beforeTextChanged(CharSequence s, int start, int count,
	                int after) {
	            // TODO Auto-generated method stub

	        }

	        @Override
	        public void afterTextChanged(Editable s) {
	            // TODO Auto-generated method stub

	        }
	    });
		
		
		
	}

	/**
	 * Checking device has camera hardware or not
	 * */
	private boolean isDeviceSupportCamera() {
		if (getApplicationContext().getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_CAMERA)) {
			// this device has a camera
			return true;
		} else {
			// no camera on this device
			return false;
		}
	}

	/**
	 * Launching camera app to capture image
	 */
	private void captureImage() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

		intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

		// start the image capture Intent
		startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
	}

	/**
	 * Launching camera app to record video
	 */

	/**
	 * Here we store the file url as it will be null after returning from camera
	 * app
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		// save file url in bundle as it will be null on screen orientation
		// changes
		outState.putParcelable("file_uri", fileUri);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

		// get the file url
		fileUri = savedInstanceState.getParcelable("file_uri");
	}

	/**
	 * Receiving activity result method will be called after closing the camera
	 * */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// if the result is capturing Image
		if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {

				// successfully captured the image
				// launching upload activity
				launchUploadActivity(true);

			} else if (resultCode == RESULT_CANCELED) {

				// user cancelled Image capture
				Toast.makeText(getApplicationContext(),
						"User cancelled image capture", Toast.LENGTH_SHORT)
						.show();

			} else {
				// failed to capture image
				Toast.makeText(getApplicationContext(),
						"Sorry! Failed to capture image", Toast.LENGTH_SHORT)
						.show();
			}

		}
	}

	private void launchUploadActivity(boolean isImage) {
		/*
		 * Intent i = new Intent(AddItemActivity.this, UploadActivity.class);
		 * i.putExtra("filePath", fileUri.getPath()); 
		 * i.putExtra("isImage",
		 * isImage); 
		 * startActivity(i);
		 */

		imgFile = new File(fileUri.getPath());

		if (imgFile.exists()) {

			Bitmap myBitmap = BitmapFactory.decodeFile(imgFile
					.getAbsolutePath());

			btnCapturePicture.setImageBitmap(myBitmap);
			
			

		}

	}

	/**
	 * ------------ Helper Methods ----------------------
	 * */

	/**
	 * Creating file uri to store image/video
	 */
	public Uri getOutputMediaFileUri(int type) {
		return Uri.fromFile(getOutputMediaFile(type));
	}

	/**
	 * returning image / video
	 */
	private static File getOutputMediaFile(int type) {

		// External sdcard location
		File mediaStorageDir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				Yapp.IMAGE_DIRECTORY_NAME);

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d(TAG, "Oops! Failed create " + Yapp.IMAGE_DIRECTORY_NAME
						+ " directory");
				return null;
			}
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
				Locale.getDefault()).format(new Date());
		File mediaFile;
		if (type == MEDIA_TYPE_IMAGE) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ "IMG_" + timeStamp + ".jpg");
		} else {
			return null;
		}

		return mediaFile;
	}

	/*
	 * upload product
	 */
	private void uploadProduct() {

		pDialog = new ProgressDialog(this);
		// Showing progress dialog before making http request
		pDialog.setMessage("Uploading...");
		pDialog.show();

		String url = Yapp.HOME_URL + "home/postHome ";
		RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());

		Listener<String> listener = new Listener<String>() {

			@Override
			public void onResponse(String result) {
				JSONObject output;
				try {
					output = new JSONObject(result);
					final String value = output.getString("sending");
					
					
					Log.i("ohdoking volley error",output.getString("data"));
					hidePDialog();
					if(value.equals("success")){
						
						Toast.makeText(getApplicationContext(),
								"업로드 성공!", Toast.LENGTH_SHORT).show();
						finish();
						
					}
					else{
						Toast.makeText(getApplicationContext(),
								"업로 드를 실패.. 다시 시도해주세요", Toast.LENGTH_LONG).show();
						
					}
					
					
		
				
				
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}

		};// listener

		ErrorListener errorListener = new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				Toast.makeText(getApplicationContext(),
						"network error: " + arg0, Toast.LENGTH_SHORT).show();
				System.out.println("network error: " + arg0);
			}

		};// ErrorListener

		

		StoreProduct sp = new StoreProduct();
		sp.setTime(remainderTime.getSelectedItem().toString());
		sp.setFinishTime(finishTime.getSelectedItem().toString());
		sp.setDiscountPercent(discountPercent.getText().toString());
		sp.setDiscountPrice(discountPrice.getText().toString());
		sp.setOriginPrice(originPrice.getText().toString());
		sp.setProductName(productName.getText().toString());
		sp.setStoreName("도근청과");

		
		PhotoMultipartRequest request = new PhotoMultipartRequest(url,
				errorListener,listener,imgFile,sp,pDialog) ;

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