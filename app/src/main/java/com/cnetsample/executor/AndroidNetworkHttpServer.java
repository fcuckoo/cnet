package com.cnetsample.executor;

import android.content.Context;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.Method;
import com.androidnetworking.common.RequestBuilder;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.cnet.def.caller.IDownloadCallback;
import com.cnet.def.http.CHttpMethod;
import com.cnet.def.http.IHttpServer;
import com.cnet.def.http.IHttpServerCallback;
import com.cnet.def.http.exception.HttpException;
import com.cnet.def.http.request.CAbstractRequst;
import com.cnet.def.http.resp.IErrResp;
import com.cnet.def.http.resp.IHttpResp;
import com.cnet.def.http.resp.IRespBase;
import com.cnet.impl.volley.resp.DefBaseResponse;
import com.cnet.util.gson.GsonUtil;
import com.cutil.CListUtil;
import com.cutil.ST;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * @author Cuckoo
 * @date 2016-09-03
 * @description Use Android Network to get data.
 */
public class AndroidNetworkHttpServer implements IHttpServer {
    private static HashMap<CHttpMethod, Integer> supportMethodMap = null;
    //Just use to post string.

    static {
        supportMethodMap = new HashMap<>();
        supportMethodMap.put(CHttpMethod.GET, Method.GET);
        supportMethodMap.put(CHttpMethod.POST, Method.POST);
        supportMethodMap.put(CHttpMethod.PUT, Method.PUT);
        supportMethodMap.put(CHttpMethod.DELETE, Method.DELETE);
        supportMethodMap.put(CHttpMethod.HEAD, Method.HEAD);
//        supportMethodMap.put(CHttpMethod.OPTIONS, Method.OPTIONS);  //不支持这两种方式
//        supportMethodMap.put(CHttpMethod.TRACE, Method.TRACE);
        supportMethodMap.put(CHttpMethod.PATCH, Method.PATCH);
    }

    @Override
    public <T, E extends IRespBase> E getDataBySync(Context context, CHttpMethod method, String url,
                                                    HashMap<String, Object> postParams,
                                                    String postParamsByJson,
                                                    HashMap<String, String> headerMap,
                                                    Class<T> respObjClass,
                                                    Class<T> baseRespClass, boolean isReturnJson,
                                                    CAbstractRequst request) {
        return null ;
    }

    @Override
    public <T> boolean getData(Context context, CHttpMethod method, String url,
                               HashMap<String, Object> postParams,
                               final String postParamsByJson,
                               HashMap<String, String> headerMap,
                               final Class<T> respObjClass,
                               final Class<T> baseRespClass,
                               final boolean isReturnJson, final IHttpServerCallback callback,
                               final CAbstractRequst request) {
        ANRequest req = parseRequest(method,url,postParams,postParamsByJson,headerMap);
        if( req != null ){
            //发送请求数据
            req.getAsString(new StringRequestListener() {
                @Override
                public void onResponse(String response) {
                    Log.e("cuckoo", ST.f(postParamsByJson));
                    Log.e("cuckoo", ST.f(response));
                    if (callback != null) {
                        IRespBase<T> resp = parse2Result(response, respObjClass, baseRespClass,null, isReturnJson);
                        callback.onResponse(resp, request);
                    }
                }

                @Override
                public void onError(ANError anError) {
                    Log.e("cuckoo",postParamsByJson);
                    Log.e("cuckoo",anError.getLocalizedMessage());
                    if (callback != null) {
                        HttpException exception = new HttpException(IErrResp.STATUS_RESP_NON200,
                                anError.getMessage());
                        exception.setTag(anError);
                        callback.onErrResponse(exception, request);
                    }
                }
            });
        }else {
            if (callback != null) {
                HttpException exception = new HttpException(IErrResp.STATUS_REQUEST_UNSUPPORT,
                        "UnSupport the request:" + method.toString());
                exception.setTag(exception);
                callback.onErrResponse(exception, request);
            }
        }
        //当req正确时返回成功
        return req != null;
    }

    /**
     * 下载文件
     * @param context
     * @param url
     *      Url地址
     * @param destinationPath
     *      下载文件存放路径
     * @param  isAllow3G
     *      是否允许3G下下载
     * @param  downloadCallback
     *      下载回调
     * @return
     */
    @Override
    public boolean downloadFile(Context context, String url, String destinationPath,
                                boolean isAllow3G, IDownloadCallback downloadCallback) {
        return false;
    }

    /**
     * Pase params to Android Network request.
     *
     * @param method
     * @param url
     * @param postParams
     * @param headerMap
     * @return
     */
    private ANRequest parseRequest(CHttpMethod method, String url,
                                   HashMap<String, Object> postParams,
                                   String postParamsByJson,
                                   HashMap<String, String> headerMap) {
        RequestBuilder builder = null ;
        if( method == CHttpMethod.GET){
            //get请求
            builder = AndroidNetworking.get(url);
        }else if(method == CHttpMethod.POST){
            //POST请求
            builder = AndroidNetworking.post(url);
        }
        if( builder != null ){
            builder.setTag(url);
            appendRequestHeader(headerMap,builder);
            appendRequestParams(postParams,postParamsByJson,builder);
        }
        if( builder instanceof ANRequest.GetRequestBuilder){
            return ((ANRequest.GetRequestBuilder)builder).build();
        }else if(builder instanceof ANRequest.PostRequestBuilder){
            return ((ANRequest.PostRequestBuilder)builder).build();
        }
        return null ;
    }

    /**
     * 拼接header部分
     * @param headerMap
     * @param builder
     * @return
     */
    private RequestBuilder appendRequestHeader(HashMap<String, String> headerMap, RequestBuilder builder){
        if (builder != null) {
            if (headerMap != null) {
                Iterator<Map.Entry<String, String>> it = headerMap.entrySet().iterator();
                Map.Entry<String, String> entry = null;
                while (it.hasNext()) {
                    entry = it.next();
                    builder.addHeaders(entry.getKey(), ST.f(entry.getValue()));
                }
            }
            builder.addHeaders("Accept", "application/json");
            builder.addHeaders("Content-Type", "application/json; charset=utf8");
        }
        return builder;
    }

    /**
     * 拼接需要上传的参数
     * @param postParams
     * @param postParamsByJson
     * @param builder
     * @return
     */
    private RequestBuilder appendRequestParams(HashMap<String, Object> postParams,
                                               String postParamsByJson, RequestBuilder builder){
        if (postParams != null) {
            Iterator<Map.Entry<String,Object>> it = postParams.entrySet().iterator();
            Map.Entry<String,Object> entry = null ;
            while (it.hasNext()){
                entry = it.next();
                if(entry.getValue() instanceof String){
                    //目前暂时只处理值为字符串的情况
                    builder.addQueryParameter(entry.getKey(), (String)entry.getValue());
                }
            }
        }else if(!ST.isEmpty(postParamsByJson)&&!"{}".equals(postParamsByJson)){
//            builder.addQueryParameter(postParamsByJson);
            if (builder instanceof ANRequest.PostRequestBuilder) {
                ((ANRequest.PostRequestBuilder) builder).setContentType("application/json; charset=utf8");
                ((ANRequest.PostRequestBuilder) builder).addByteBody(postParamsByJson.getBytes());
            }
        }
        return builder;
    }

    /**
     * Parse result to {@link IRespBase}
     * @param json
     * @param respObj
     *      json中返回data节点所对应的类
     * @param baseRespClass
     *      json中的首节点，主要用于表示当前请求成功失败与否以及失败原因等。 需要实现{@link IHttpResp}
     * @param errResp
     * @param isReturnJson
     * @param <T>
     * @return
     */
    private <T> IRespBase<T> parse2Result(String json, Class<T> respObj,
                                          Class<T> baseRespClass,
                                          IErrResp errResp, boolean isReturnJson) {
        IRespBase<T> response = null;
        int errCode = IErrResp.STATUS_RESP_NON200;
        String message = null;
        boolean isError = false;
        if (errResp != null) {
            //Request error.
            errCode = errResp.getStatus();
            message = errResp.getErrMsg();
            isError = true;
        } else {
            if (ST.isEmpty(json)) {
                errCode = IErrResp.STATUS_RESPNULL;
                isError = true;
            }
        }

        if (!isError) {
            //Parse json
            try {
                if (respObj == null){
                    response = (IRespBase<T>) GsonUtil.parseJson(json,baseRespClass);
                }else {
                    String data = null ;
                    JSONObject jsonObject = new JSONObject(json);
                    try {
                        //当没有data节点时， 会报异常
                        data = jsonObject.getString("data");
                    }catch (Exception e){

                    }
                    if( ST.isEmpty(data)){
                        //data字段为空,将其改为空对象
                        jsonObject.put("data",null);
                        String newJson = jsonObject.toString();
                        response = (IRespBase<T>) GsonUtil.parseJson(newJson,baseRespClass);
                    }else {
                        response = (IRespBase<T>) GsonUtil.parseJsonByArgument(baseRespClass, json, respObj);
                    }
                }
                if (response != null && isReturnJson) {
                    response.setJson(json);
                }
            } catch (Exception e) {
                //parse json error.
                errCode = IErrResp.STATUS_PARSE_JSON_ERROR;
                message = getExceptionMsg(e);
            }
        }
        if (response == null) {
            try {
                response = (IRespBase<T>)baseRespClass.newInstance();
            } catch (Exception e) {
                message = getExceptionMsg(e);
                errCode = IErrResp.STATUS_BASERESPONSE_ERROR;
            }
            if( response == null ){
                response = new DefBaseResponse();
            }
            response.setResultStatus(errCode);
            response.setMessage(message);
        }
        return response;
    }

    /**
     * Get http method
     *
     * @param httpMethod
     * @return
     */
    private int getMethod(CHttpMethod httpMethod) {
        if (httpMethod != null &&
                supportMethodMap.containsKey(httpMethod)) {
            return supportMethodMap.get(httpMethod);
        }
        throw new HttpException("Can not support http method:" + httpMethod);
    }

    /**
     * Check is neet upload files.
     * The file type likes: File, Bitmap
     *
     * @param postParams
     * @return
     */
    private boolean isNeedPostFiles(HashMap<String, Object> postParams) {
        if (!CListUtil.isEmpty(postParams)) {
            Iterator<Map.Entry<String, Object>> it = postParams.entrySet().iterator();
            Object value = null;
            while (it.hasNext()) {
                value = it.next().getValue();
                if (!(value instanceof String)) {
                    return true;
                }
            }
        }
        return false;
    }

    private String getExceptionMsg(Exception e){
        if( e != null ){
            return e.getMessage();
        }
        return null ;
    }
}
