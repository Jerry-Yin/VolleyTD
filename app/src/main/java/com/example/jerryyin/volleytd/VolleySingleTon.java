package com.example.jerryyin.volleytd;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import java.util.Map;

/**
 * Created by JerryYin on 10/6/15.
 */
public class VolleySingleTon {

    private static VolleySingleTon mVolleySingleTon;

    private Context mContext;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    //构造方法
    public VolleySingleTon(Context context) {
        this.mContext = context;
        mRequestQueue = getRequestQueue();
        mImageLoader = new ImageLoader(mRequestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(20);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });
    }

    //供外部调用的方法
    public static synchronized VolleySingleTon getVolleySingleTon(Context context) {
        if (mVolleySingleTon == null) {
            mVolleySingleTon = new VolleySingleTon(context);
        }
        return mVolleySingleTon;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public ImageLoader getmImageLoader() {
        return mImageLoader;
    }
}
