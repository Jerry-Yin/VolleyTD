package com.example.jerryyin.volleytd.toolbox;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;

/**
 * Created by JerryYin on 10/8/15.
 * 仿照StringRequest编写的返回xml数据的工具类
 */
public class XmlRequest extends Request<XmlPullParser> {

    private final Response.Listener<XmlPullParser> mListener;

    public XmlRequest(int method, String url, Response.Listener<XmlPullParser> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.mListener = listener;
    }

    public XmlRequest(String url, Response.Listener<XmlPullParser> listener, Response.ErrorListener errorListener) {
        this(0, url, listener, errorListener);
    }


    @Override
    protected void deliverResponse(XmlPullParser response) {
        //将XmlPullParser对象进行回调
        this.mListener.onResponse(response);
    }

    @Override
    protected Response parseNetworkResponse(NetworkResponse response) {
        String xmlString;
        try {
            //将服务器响应的数据解析成一个字符串
            xmlString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            //然后设置到XmlPullParser对象中
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(xmlString));
            return Response.success(xmlString, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (XmlPullParserException e) {
            return Response.error(new ParseError(e));
        }
    }
}
