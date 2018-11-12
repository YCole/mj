package cole.net;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.HttpGet;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.params.CoreConnectionPNames;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.HttpStatus;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.client.methods.HttpUriRequest;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.params.CoreProtocolPNames;

public class HttpUtils {
    Context context;

    public static enum Method {
        GET, POST
    }

    private String UTF_8 = "utf-8";
    public HttpClient mClient;

    public HttpEntity getEntity(String url,
                                ArrayList<BasicNameValuePair> params, Method method)
            throws IOException {
        if (url == null || url.equals("")) {
            return null;
        }
        if (!url.startsWith("http")) {
            return null;
        }
        mClient = new DefaultHttpClient();
        HttpUriRequest request = null;
        StringBuilder sb = new StringBuilder(url);
        switch (method) {
            case GET:
                if (null != params && !params.isEmpty()) {
                    sb.append("?");
                    for (BasicNameValuePair param : params) {
                        sb.append(param.getName()).append("=")
                                .append(URLEncoder.encode(param.getValue(), UTF_8))
                                .append("&");

                    }
                    sb.deleteCharAt(sb.length() - 1);
                }
                request = new HttpGet(sb.toString());
                break;

            case POST:
                request = new HttpPost(url);
                if (null != params && !params.isEmpty()) {
                    UrlEncodedFormEntity reqEntity = new UrlEncodedFormEntity(
                            params, UTF_8);
                    ((HttpPost) request).setEntity(reqEntity);
                }
                break;
        }

        mClient.getParams().setParameter(
                CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
        mClient.getParams()
                .setParameter(CoreConnectionPNames.SO_TIMEOUT, 5000);
//        String parameter =
        mClient.getParams()
                .getParameter(CoreProtocolPNames.USER_AGENT).toString();
        mClient.getParams()
                .setParameter(
                        CoreProtocolPNames.USER_AGENT,
                        "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; Trident/4.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729;PROEIM;Media Center PC 6.0; .NET4.0C; .NET4.0E)");

//       mClient.getParams().setParameter(CoreProtocolPNames.USER_AGENT,"xiiiiiiiiiiiiii");
        System.out.println("协议头：" + mClient.getParams()
                .getParameter(CoreProtocolPNames.USER_AGENT));
        HttpResponse response = mClient.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != HttpStatus.SC_OK) {
            return null;
        }
        HttpEntity entity = response.getEntity();
        return entity;
    }

    public InputStream getInputStream(String url,
                                      ArrayList<BasicNameValuePair> params, Method method)
            throws IllegalStateException, IOException {

        return getEntity(url, params, method).getContent();
    }

    public void closeClient() {

        if (mClient != null) {
            mClient.getConnectionManager().shutdown();
        }
    }

    private static AsyncHttpClient client = new AsyncHttpClient();    //实例话对象

    static {
        client.setTimeout(11000);   //设置链接超时，如果不设置，默认为10s
    }

    public static void get(String urlString, AsyncHttpResponseHandler res)    //用一个完整url获取一个string对象
    {
        client.get(urlString, res);
    }

    public static void get(String urlString, RequestParams params, AsyncHttpResponseHandler res)   //url里面带参数
    {
        client.get(urlString, params, res);
    }

    public static void get(String urlString, JsonHttpResponseHandler res)   //不带参数，获取json对象或者数组
    {
        client.get(urlString, res);
    }

    public static void get(String urlString, RequestParams params, JsonHttpResponseHandler res)   //带参数，获取json对象或者数组
    {
        client.get(urlString, params, res);
    }

    public static void get(String uString, BinaryHttpResponseHandler bHandler)   //下载数据使用，会返回byte数据
    {
        client.get(uString, bHandler);
    }

    public static void post(String uString, RequestParams params,AsyncHttpResponseHandler res)   //
    {
        client.post(uString, params, res);
    }
    public static void post(String uString, RequestParams params,JsonHttpResponseHandler res)   //
    {
        client.post(uString, params, res);
    }

    public static AsyncHttpClient getClient() {
        return client;
    }

}
