package com.example.jerryyin.volleytd;

import android.graphics.Bitmap;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.jerryyin.volleytd.toolbox.BitmapCache;
import com.example.jerryyin.volleytd.toolbox.XmlRequest;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;


public class MainActivity extends ActionBarActivity {

    private static String TAG = "MainActivity";

    private ImageView mImageReq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
//        getRQ();
//        getStringReq();
        getJsonReq();
//        getImageReq();
        getImageLoader();
        getNetworkImgView();
        getXmlReq();
    }

    private void initView() {
        mImageReq = (ImageView) findViewById(R.id.img_req);
    }

    /**1.普通网络请*/
    public void getRQ() {
        //请求队列对象,它可以缓存所有的HTTP请求，然后按照一定的算法并发地发出这些请求
        RequestQueue mQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(
                "http://www.baidu.com",
                //服务器响应成功的回调
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                    }
                },
                //服务器响应失败的回调
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.getMessage(), error);
                    }
                })
//        {
//            //如果是采用post方式请求，请求参数在这里设置，是重写父类的方法
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> map = new HashMap<>();
//                map.put("params1", "value1");
//                map.put("params2", "value2");
//                return super.getParams();
//            }
//        }
                ;
        mQueue.add(stringRequest);
    }

    public void getStringReq(){
        VolleySingleTon mVolleySingleTon = VolleySingleTon.getVolleySingleTon(this.getApplicationContext());
        RequestQueue mRequestQueue = mVolleySingleTon.getRequestQueue();
        StringRequest stringRequest = new StringRequest("http://www.baidu.com",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.e(TAG, volleyError.getMessage(), volleyError);
                    }
                });
        mRequestQueue.add(stringRequest);
    }

    /**2.请求json数据*/
    public void getJsonReq(){
//        JSONObject object = new JSONObject();
//        try {
//            object.put("name", "jack");
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        String data = String.valueOf(object);

        VolleySingleTon volleySingleTon = new VolleySingleTon(this.getApplicationContext());
        RequestQueue requestQueue = volleySingleTon.getRequestQueue();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                "http://m.weather.com.cn/atad/101230201.html", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.d(TAG, jsonObject.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG, volleyError.getMessage());
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }

    /**3.ImageRequest请求图片*/
    public void getImageReq(){
        ImageRequest imageRequest = new ImageRequest(
                "http://developer.android.com/images/home/aw_dac.png",
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        mImageReq.setImageBitmap(bitmap);
                    }
                },
                0, 0, Bitmap.Config.RGB_565,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
//                        mImageReq.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher));
                        mImageReq.setImageResource(R.mipmap.ic_launcher);
                    }
                }

        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(imageRequest);
    }

    /**4.ImageLoader加载图片*/
    public void getImageLoader(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        /**1.空的imageCache*/
//        ImageLoader.ImageCache imageCache = new ImageLoader.ImageCache() {
//            @Override
//            public Bitmap getBitmap(String s) {
//                return null;
//            }
//
//            @Override
//            public void putBitmap(String s, Bitmap bitmap) {
//
//            }
//        };

        /**2.借助LruCache实现的imageCache*/
        BitmapCache imageCache = new BitmapCache();

        ImageLoader imageLoader = new ImageLoader(requestQueue, imageCache);
        ImageLoader.ImageListener imageListener = ImageLoader.getImageListener(mImageReq, R.drawable.img_dafault, R.drawable.ic_launcher);
        imageLoader.get("http://img.my.csdn.net/uploads/201404/13/1397393290_5765.jpeg", imageListener);
    }

    /**5.NetworkImageView(自定义控件)加载网络图片的用法*/
    public void getNetworkImgView(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        ImageLoader imageLoader = new ImageLoader(requestQueue, new BitmapCache());
        NetworkImageView networkImageView = (NetworkImageView) findViewById(R.id.network_image_view);
        networkImageView.setDefaultImageResId(R.drawable.img_dafault);
        networkImageView.setErrorImageResId(R.drawable.ic_launcher);
        networkImageView.setImageUrl("http://img.my.csdn.net/uploads/201404/13/1397393290_5765.jpeg", imageLoader);
    }

    /**6.自定义请求xml数据*/
    public void getXmlReq(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        XmlRequest xmlRequest = new XmlRequest(
                "http://flash.weather.com.cn/wmaps/xml/china.xml",
                new Response.Listener<XmlPullParser>() {
                    @Override
                    public void onResponse(XmlPullParser response) {
                        try {
                            int eventType = response.getEventType();
                            while (eventType != XmlPullParser.END_DOCUMENT){
                                switch (eventType){
                                    case XmlPullParser.START_TAG:
                                        String nodeName = response.getName();
                                        if ("city".equals(nodeName)){
                                            String cityName = response.getAttributeName(0);
                                            String weather = response.getAttributeName(5);
                                            Log.d(TAG, cityName +" "+ weather+"\n");
                                        }
                                        break;
                                }
                                eventType = response.next();
                            }

                        } catch (XmlPullParserException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.e(TAG, volleyError.getMessage(), volleyError);
                    }
                });
        requestQueue.add(xmlRequest);
    }


}
