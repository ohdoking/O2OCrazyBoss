package com.example.yapp;

import java.io.*;
import java.util.*;

import org.apache.http.*;
import org.apache.http.client.*;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.*;
import org.json.*;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.widget.*;

import com.android.volley.*;
import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.*;
import com.skp.Tmap.*;

public class MapActivity extends Activity {

	private Context context = this;
	MyDataShared mds;

	private RelativeLayout mapLayout;
	TMapView tmapview = null;
	private EditText addressEdit;
	private Button gobtn, finalBtn;
	private geoPointTask geoPointTask;
	private ArrayList<storeInfo> storeInfoList;
	private ArrayList<String> userSaveStore;
	int mMarkerID = 0;
	int status;
	int checkLogin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

		Intent intent = getIntent();
		status = intent.getIntExtra("status", 0); // comer, user status
		checkLogin = intent.getIntExtra("map", 0);
		
		
		
		
		mapData();
		mds = new MyDataShared(context);

		mapLayout = (RelativeLayout) findViewById(R.id.mapLayout);
		tmapview = new TMapView(this);
		storeInfoList = new ArrayList<storeInfo>();
		userSaveStore = new ArrayList<String>();
		mapLayout.addView(tmapview);

		// tmapview.setTrackingMode(true);
		listener();
		tmapview.setSKPMapApiKey("deff15b5-8483-340b-aedc-e9c30f94e954");
		tmapview.setLanguage(TMapView.LANGUAGE_KOREAN);

		finalBtn = (Button) findViewById(R.id.mapnextBtn);
		addressEdit = (EditText) findViewById(R.id.addressEdit);
		gobtn = (Button) findViewById(R.id.searchBtn);
		

		if(checkLogin == 2){
			gobtn.setVisibility(View.GONE);
			addressEdit.setVisibility(View.GONE);
			finalBtn.setVisibility(View.GONE);
			
			String[] tempLoc = mds.getValue("userLoca", null).split(",");
			
			
			
			Double lat = Double.parseDouble(tempLoc[0]);
			Double lon = Double.parseDouble(tempLoc[1]);
			showMarkerPoint();
			setMarker(lat,lon);
		}
		gobtn.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				String findAddress = addressEdit.getText().toString();
				if (status == staticConst.USERS_STATUS) {
					mds.put("userHome", findAddress);
				} else if (status == staticConst.ADMIN_STATUS) {
					mds.put("comerHome", findAddress);
				}

				geoPointTask = (com.example.yapp.MapActivity.geoPointTask) new geoPointTask()
						.execute(findAddress);
				// 네트워크 작업이기 때문에 어싱크태스크로 돌려줍니다

			}
		});
		// showMarkerPoint();
	}

	private void mapData() {
		String url = staticConst.URL_address + "users/mapData";
		RequestQueue mQueue = Volley.newRequestQueue(context);

		Listener<String> listener = new Listener<String>() {

			@Override
			public void onResponse(String result) {
				try {
					JSONObject output = new JSONObject(result);
					final String value = output.getString("sending");

					JSONArray data = output.getJSONArray("data");
					for (int i = 0; i < data.length(); i++) {
						storeInfo storeinfo = new storeInfo();
						JSONObject json = data.getJSONObject(i);
						storeinfo.comerSrl = json.getString("comerSrl");
						storeinfo.comerLoca = json.getString("comerLoca");
						storeinfo.comerName = json.getString("comerName");
						storeinfo.comerDescrib = json.getString("comerDescrib");
						storeinfo.comerCategory = json
								.getString("comerCategory");

						storeInfoList.add(storeinfo);

					}
					if (value.equals("success")) {
						// setStoreMarker();
						showMarkerPoint();
						
					} else {
						Log.d("sending", "fail");
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

		};// listener

		com.android.volley.Response.ErrorListener errorListener = new com.android.volley.Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				Toast.makeText(context, "network error: " + arg0,
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

				return params;
			}

		};// StringRequest

		mQueue.add(request);
	}

	private void listener() {

		// 상세보기
		// 등록할수있게하기
		tmapview.setOnCalloutRightButtonClickListener(new TMapView.OnCalloutRightButtonClickCallback() {

			@Override
			public void onCalloutRightButton(TMapMarkerItem markerItem) {
				String storename = markerItem.getCalloutTitle();
				String storeInfo = markerItem.getCalloutSubTitle();
				showAlert(storename, storeInfo, markerItem.getID());
			}

		});
	}

	// 가게상세정보와 등록창 띄우기
	private void showAlert(String storename, String storeInfo,
			final String markerId) {
		new AlertDialog.Builder(context)
				.setTitle(storename)
				.setMessage(storeInfo)
				.setNegativeButton("취소", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Toast.makeText(context, "췰소", Toast.LENGTH_SHORT)
								.show();
						dialog.cancel();
					}
				})
				.setPositiveButton("등록", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 상점 아이디 저장
						System.out.println("눌린 상점 srl = " + markerId);
						
						if(userSaveStore.size()>=3){
							Toast.makeText(context, "3개입력끝",
									Toast.LENGTH_SHORT).show();
							System.out.println("사용자가 등록한 가게 수 = " + userSaveStore.size());
						}else{
							
							userSaveStore.add(markerId);
							
								if(userSaveStore.size()>=3){
									Toast.makeText(context, "3개입력끝",
											Toast.LENGTH_SHORT).show();
								}
							// nextBtn.setVisibility(Button.VISIBLE);
							finalBtn.setOnClickListener(new Button.OnClickListener() {

								@Override
								public void onClick(View v) {
									// 사용자 등록정보 보내기
								
									// 저장된것 확인하기
									String comerIdStr = "";
									for (int i = 0; i < userSaveStore
											.size(); i++) {
										// System.out.println(userFavarite.get(i));
										if (i < userSaveStore.size() - 1) {
											comerIdStr += userSaveStore
													.get(i);
											comerIdStr += ",";
										} else
											comerIdStr += userSaveStore
													.get(i);

									}
									System.out.println("comerIdStr = "
											+ comerIdStr);

									if (status == staticConst.USERS_STATUS) {
										mds.put("userComerSrl", comerIdStr);
										
										UserInfo userInfo;
										userInfo = new UserInfo();
										userInfo.userId = mds.getValue(
												"userId", null);
										userInfo.userPw = mds.getValue(
												"userPw", null);
										userInfo.userHome = mds.getValue(
												"userHome", null); // 주소 풀네임
										userInfo.userLoca = mds.getValue(
												"userLoca", null); // 위도 경도
										userInfo.userFavorite = mds
												.getValue("userFavorite",
														null);
										userInfo.userComerSrl = mds
												.getValue("userComerSrl",
														null);

										enroll(userInfo);

									} else if (status == staticConst.ADMIN_STATUS) {
										mds.put("comercomerSrl", comerIdStr);
										
										ComerInfo comerInfo;
										comerInfo = new ComerInfo();
										comerInfo.comerId = mds.getValue(
												"comerId", null);
										comerInfo.comerPw = mds.getValue(
												"comerPw", null);
										comerInfo.comerHome = mds.getValue(
												"comerHome", null);
										comerInfo.comerLoca = mds.getValue(
												"comerLoca", null);
										comerInfo.comerName = mds.getValue(
												"comerName", null);
										comerInfo.comerDescrib = mds
												.getValue("comerDescrib",
														null);
										comerInfo.comerCategory = mds
												.getValue("comerCategory",
														null);
										comerInfo.comercomerSrl = mds
												.getValue("comercomerSrl",
														null);

										enrollComer(comerInfo);
									}

								}
							});

						}
//						
//						if (userSaveStore.size() < 4) {
//							if (userSaveStore.size() == 3) {
//							
//							} else if (userSaveStore.size() < 3) {
//								userSaveStore.add(markerId);
//							}
//
//						} else
//							Toast.makeText(context, "3개입력끝Rmx", Toast.LENGTH_SHORT)
//									.show();
					}
				}).show();
	}

	private class geoPointTask extends AsyncTask<String, Void, Void> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(String... params) {
			getGeoPoint(getLocationInfo(params[0].replace("\n", " ").replace(
					" ", "%20"))); // 주소를 넘겨준다(공백이나 엔터는 제거합니다)

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

		}
	}

	public JSONObject getLocationInfo(String address) {

		HttpGet httpGet = new HttpGet(
				"http://maps.google.com/maps/api/geocode/json?address="
						+ address + "&ka&sensor=false");
		// 해당 url을 인터넷창에 쳐보면 다양한 위도 경도 정보를 얻을수있다(크롬 으로실행하세요)
		HttpClient client = new DefaultHttpClient();
		HttpResponse response;
		StringBuilder stringBuilder = new StringBuilder();

		try {
			response = client.execute(httpGet);
			HttpEntity entity = response.getEntity();
			InputStream stream = entity.getContent();
			int b;
			while ((b = stream.read()) != -1) {
				stringBuilder.append((char) b);
			}
		} catch (ClientProtocolException e) {
		} catch (IOException e) {
		}

		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject = new JSONObject(stringBuilder.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return jsonObject;
	}

	public void getGeoPoint(JSONObject jsonObject) {

		Double lon = new Double(0);
		Double lat = new Double(0);

		try {
			lon = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
					.getJSONObject("geometry").getJSONObject("location")
					.getDouble("lng");

			lat = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
					.getJSONObject("geometry").getJSONObject("location")
					.getDouble("lat");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Log.d("myLog", "경도:" + lon); // 위도/경도 결과 출력
		Log.d("myLog", "위도:" + lat);

		// 경도/위도 마커 표시
		setMarker(lon, lat);
	}

	// 경도/위도 마커 표시
	// 주소 입력한 곳 표시
	public void setMarker(Double lon, Double lat) {
		// 위도 경도로 저장
		String loca = String.valueOf(lat).concat(",")
				.concat(String.valueOf(lon));
		System.out.println("입력한 주소의 위도/경도 = " + loca);
		if (status == staticConst.USERS_STATUS) {
			mds.put("userLoca", loca);
		} else if (status == staticConst.ADMIN_STATUS) {
			mds.put("comerLoca", loca);
		}

		TMapPoint tpoint = new TMapPoint(lat, lon);
		TMapMarkerItem tItem = new TMapMarkerItem();

		tItem.setTMapPoint(tpoint);
		tItem.setVisible(TMapMarkerItem.VISIBLE);
		tItem.setCanShowCallout(true); // 풍선뷰 사용 여부
		tItem.setCalloutTitle("입력한 주소");
		if (status == staticConst.USERS_STATUS) {
			tItem.setCalloutSubTitle(mds.getValue("userHome", ""));
		} else if (status == staticConst.ADMIN_STATUS) {
			tItem.setCalloutSubTitle(mds.getValue("comerHome", ""));
		}

		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.ic_launcher);

		// tItem.setCalloutRightButtonImage(bitmap);
		tItem.setIcon(bitmap);

		tmapview.addMarkerItem("marker", tItem);
		tmapview.setCenterPoint(lon, lat);
		tmapview.refreshMap();
	}


	// 서버에서 받아온
	// 상점 마커 표시
	public void showMarkerPoint() {
		for (int i = 0; i < storeInfoList.size(); i++) {
			String str = storeInfoList.get(i).comerLoca;
			String[] result = str.split(","); // 위도, 경도 순으로 저장
			double lat = Double.parseDouble(result[1]);// 위도
			double lon = Double.parseDouble(result[0]); // 경도
			System.out.println("lat = " + lat + ", lon = " + lon);

			Bitmap bitmap = null;

			TMapPoint point = new TMapPoint(lon, lat);// 경도 , 위도

			TMapMarkerItem item1 = new TMapMarkerItem();

			bitmap = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.map_pin);

			item1.setTMapPoint(point);
			item1.setName(storeInfoList.get(i).comerName);
			item1.setVisible(item1.VISIBLE);

			item1.setIcon(bitmap);

			item1.setCalloutTitle(storeInfoList.get(i).comerName);
			item1.setCalloutSubTitle(storeInfoList.get(i).comerDescrib);
			item1.setCanShowCallout(true);
			item1.setAutoCalloutVisible(true);

			Bitmap bitmap_i = BitmapFactory.decodeResource(
					context.getResources(), R.drawable.ic_launcher);

			item1.setCalloutRightButtonImage(bitmap_i);

			String strID = String.format("%d",
					Integer.valueOf(storeInfoList.get(i).comerSrl));

			tmapview.addMarkerItem(strID, item1);
		}

	}

	// 사용자 가입
	private void enroll(final UserInfo userInfo) {
		String url = staticConst.URL_address + "users/enroll";
		RequestQueue mQueue = Volley.newRequestQueue(context);

		Listener<String> listener = new Listener<String>() {

			@Override
			public void onResponse(String result) {
				try {
					JSONObject output = new JSONObject(result);
					final String value = output.getString("sending");

					if (value.equals("success")) {
						// 가입완료후 작동하는 인텐트 추가
						Intent intent = new Intent(context, Login.class);
						// 스택에 쌓인 액티비티 종료하고 로그인 액티로 이동
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
						Toast.makeText(context, "가입성공", Toast.LENGTH_LONG)
								.show();
						System.out.println("사용자 가입성공");
					} else {
						Log.d("sending", "fail");
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

		};// listener

		com.android.volley.Response.ErrorListener errorListener = new com.android.volley.Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				Toast.makeText(context, "network error: " + arg0,
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
				params.put("userId", userInfo.userId);
				params.put("userPw", userInfo.userPw);
				params.put("userFavorite", userInfo.userFavorite);
				params.put("userHome", userInfo.userHome);
				params.put("userLoca", userInfo.userLoca);
				params.put("userComerSrl", userInfo.userComerSrl);
				params.put("GCMregitId", "0");

				System.out
						.println(userInfo.userId + "/" + userInfo.userPw + "/"
								+ userInfo.userFavorite + "/"
								+ userInfo.userHome + "/" + userInfo.userLoca
								+ "/" + userInfo.userComerSrl);
				return params;
			}

		};// StringRequest

		mQueue.add(request);
	}

	// 상인 가입
	private void enrollComer(final ComerInfo comerInfo) {
		String url = staticConst.URL_address + "users/enrollComer";
		RequestQueue mQueue = Volley.newRequestQueue(context);

		Listener<String> listener = new Listener<String>() {

			@Override
			public void onResponse(String result) {
				try {
					JSONObject output = new JSONObject(result);
					final String value = output.getString("sending");

					if (value.equals("success")) {
						// 가입완료후 작동하는 인텐트 추가
						Intent intent = new Intent(context, Login.class);
						// 스택에 쌓인 액티비티 종료하고 로그인 액티로 이동
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
						Toast.makeText(context, "가입성공", Toast.LENGTH_LONG)
								.show();
						System.out.println("상인 가입성공");
					} else {
						Log.d("sending", "fail");
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

		};// listener

		com.android.volley.Response.ErrorListener errorListener = new com.android.volley.Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				Toast.makeText(context, "network error: " + arg0,
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
				params.put("comerId", comerInfo.comerId);
				params.put("comerPw", comerInfo.comerPw);
				params.put("comerHome", comerInfo.comerHome);
				params.put("comerName", comerInfo.comerName);
				params.put("comerLoca", comerInfo.comerLoca);
				params.put("comerDescrib", comerInfo.comerDescrib);
				params.put("comerCategory", comerInfo.comerCategory);
				params.put("comercomerSrl", comerInfo.comercomerSrl);
				params.put("GCMregitId", "0");

				System.out.println(comerInfo.comerId + "/" + comerInfo.comerPw
						+ "/" + comerInfo.comerName + "/" + comerInfo.comerHome
						+ "/" + comerInfo.comerLoca + "/"
						+ comerInfo.comercomerSrl + "/" + comerInfo.comerDescrib
						+ "/" + comerInfo.comerCategory);
				return params;
			}

		};// StringRequest

		mQueue.add(request);
	}
}
