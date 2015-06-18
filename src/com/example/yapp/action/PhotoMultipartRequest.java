package com.example.yapp.action;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.example.yapp.model.StoreProduct;
 
public class PhotoMultipartRequest extends Request {
 
    private static final String FILE_PART_NAME = "productImage";
    
 
    private MultipartEntityBuilder mBuilder = MultipartEntityBuilder.create().setCharset(Charset.forName("UTF-8"));
    private final Response.Listener<String> mListener;
    private final File mImageFile;
	protected Map<String, String> headers;
	private final StoreProduct sp;
	ProgressDialog dialog;
	
    
    public PhotoMultipartRequest(String url, 
    		ErrorListener errorListener, 
    		Listener<String> listener,  
    		File imageFile,
    		StoreProduct sp,
    		ProgressDialog dialog)
    {
        super(Method.POST, url, errorListener);
 
        mListener = listener;
        mImageFile = imageFile;
        this.sp = sp;
        this.dialog = dialog;
        
        buildMultipartEntity();
        
       /* pDialog = new ProgressDialog(context);
		// Showing progress dialog before making http request
		pDialog.setMessage("Uploading...");
		pDialog.show();*/
    }
 
	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
	    Map<String, String> headers = super.getHeaders();
 
	    if (headers == null
	            || headers.equals(Collections.emptyMap())) {
	        headers = new HashMap<String, String>();
	    }
	    
	    headers.put("Accept", "application/json");
	    
	    return headers;
	}
 
    private void buildMultipartEntity()
    {
    	Log.i("ohodkingKOR",sp.getStoreName());
    	mBuilder.addTextBody("comerSrl","101" );
    	mBuilder.addTextBody("comerName",sp.getStoreName() );
    	mBuilder.addTextBody("comerLoca","37.525758,126.848509" );
    	mBuilder.addTextBody("productCode","2" );
    	mBuilder.addTextBody("productName",sp.getProductName() );
    	mBuilder.addTextBody("productPrice",sp.getOriginPrice() );
    	mBuilder.addTextBody("productSalePrice",sp.getDiscountPrice() );
    	mBuilder.addTextBody("productDisCountPer",sp.getDiscountPercent() );
    	mBuilder.addTextBody("productTimeOutPush",sp.getTime() );
    	mBuilder.addTextBody("productTimeOut",sp.getFinishTime());
    	
//    	InputStreamBody inputStreamBody = new InputStreamBody(new ByteArrayInputStream(data), mImageFile.getName());
//    	mBuilder.addPart(FILE_PART_NAME, new FileBody(mImageFile, "image/jpeg"));
       	mBuilder.addBinaryBody(FILE_PART_NAME, mImageFile, ContentType.create("image/jpeg"), mImageFile.getName());
       	mBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
       	mBuilder.setLaxMode().setBoundary("xx");
    }
 
    @Override
    public String getBodyContentType()
    {
        String contentTypeHeader = mBuilder.build().getContentType().getValue();
        return contentTypeHeader;
    }
 
    @Override
    public byte[] getBody() throws AuthFailureError
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try
        {
        	mBuilder.build().writeTo(bos);
        }
        catch (IOException e)
        {
            VolleyLog.e("IOException writing to ByteArrayOutputStream bos, building the multipart request.");
        }
        
        return bos.toByteArray();
    }
    
    @Override
    protected Response parseNetworkResponse(NetworkResponse response)
    {
//    	String result = null;
    	
     	hidePDialog();
     	
     	
    	 String json = null;
         try {
             json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
             JSONObject output = new JSONObject(json);
				final String value = output.getString("sending");
         } catch (UnsupportedEncodingException e) {
             Log.e("Error", String.format("Encoding problem parsing API response. NetworkResponse:%s", response.toString()), e);
             return Response.error(new ParseError(e));
         } catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
        return Response.success(json, HttpHeaderParser.parseCacheHeaders(response));
    }
    
    @Override
    protected void deliverResponse(Object response)
    {
//    	Log.i("ohdokingResponse",response);
    	hidePDialog();
        mListener.onResponse((String) response);
    }
    
    private void hidePDialog() {
    	if (dialog != null) {
    		dialog.dismiss();
    		dialog = null;
		}
    }
}