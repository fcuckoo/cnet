package com.cnet.def.http;

import android.content.Context;

import com.cnet.def.caller.IDownloadCallback;
import com.cnet.def.caller.IReqCallback;
import com.cnet.def.caller.IReqRefactor;
import com.cnet.def.http.def.DefReqErrReceiver;
import com.cnet.def.http.dispatcher.ReqDispatcher;
import com.cnet.def.http.request.CAbstractRequst;
import com.cnet.def.http.request.IBaseRequest;
import com.cnet.def.http.resp.IRespBase;
import com.cutil.log.CLog;
import com.cutil.log.ILog;

import java.util.HashMap;

/**
 *
 * @author Cuckoo
 * @date 2016-09-03
 * @description
 *      The implements class of {@link HttpServer}, to manange http request.
 */
public class ImplHttpServer {
    private static Context context = null ;
    //Is need return json to caller. default is not.
    private static boolean isReturnJson = false ;
    private static IHttpServer httpServerImpl = null ;
    //接口返回json第一级的节点信息， 所有接口通用
    private static Class baseRespClass = null ;
    protected static void init(Context ctx, Class implClass, Class respClass, boolean isNeedReturnJson,
                               ILog logImpl){
        setLogImpl(logImpl);
        if( ctx == null || implClass == null  || respClass == null ){
            new RuntimeException("HttpServer init failure, context, baseRespClass or implClass is null");
        }
        IHttpServer httpServer = null ;
        try {
            Object obj = implClass.newInstance();
            if( obj instanceof IHttpServer){
                httpServer = (IHttpServer)obj;
            }
        }catch (Exception e){

        }
        if( httpServer == null ){
            new RuntimeException("HttpServer impl class is incorrect." + implClass.toString());
        }
 		try {
            checkBaseRespClass(respClass);
        }catch (Exception e){
            throw e;
        }
        context = ctx.getApplicationContext();
        httpServerImpl = httpServer;
        isReturnJson = isNeedReturnJson ;
        baseRespClass = respClass;
    }

    /**
     * 检查接口返回的顶级节点是否实现{@link IRespBase}接口
     * @param respClass
     * @return
     */
    protected static boolean checkBaseRespClass(Class respClass){
        IRespBase respBase = null ;
        if( respClass != null){
            try {
                Object obj = respClass.newInstance();
                if( obj instanceof IRespBase){
                    respBase = (IRespBase)obj;
                }
            }catch (Exception e){

            }
        }
        if( respBase == null ){
            new RuntimeException("Base response class is incorrect." + respClass.toString());
            return false ;
        }
        return true ;
    }


    /**
     *
     * @param logImpl
     */
    protected static void setLogImpl(ILog logImpl){
        CLog.setLogImpl(logImpl);
    }

    /**
     * Check this sdk is initted.
     * @return
     */
    protected static boolean isInit(){
        if( context == null || httpServerImpl == null  ){
            throw new RuntimeException("HttpServer init failure, context is null");
        }
        return true ;
    }

    /**
     * 下载文件
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
    protected static boolean downloadFile(String url, String destinationPath,
                                          boolean isAllow3G, IDownloadCallback downloadCallback){
        if( !isInit() ){
            return false ;
        }
        return httpServerImpl.downloadFile(context,url,destinationPath,isAllow3G,downloadCallback) ;
    }

    /**
     * Get default http executor
     * @return
     */
    protected static IHttpServer getDefHttpExecutor(){
        return httpServerImpl;
    }

    /**
     * Get data by sync. Just support one request.
     * @param request
     * @param <E>
     * @return
     */
    protected static  <T,E extends IRespBase> E getDataBySync(CAbstractRequst<T> request){
        if( !isInit() ){
            return null ;
        }
        if( request == null ){
            return null ;
        }

        HashMap<String,Object> postParamMap = null ;
        String postParamByJson = null ;
        if( request.isPostJson()  ){
            postParamByJson = request.getRequestParamsByJson();
        }else {
            postParamMap = request.getRequestParams();
        }
        //获取顶级解析类
        Class rootResp = HttpServerHelper.parseRootRespClass(baseRespClass,
                request);
        IHttpServer httpExecutor = HttpServerHelper.parseHttpExecutor(httpServerImpl,request);
        return (E)httpExecutor.getDataBySync(context,request.getReqMethod(),
                request.getFullUrl(),
                postParamMap,
                postParamByJson,
                request.getRequestHeader(context),
                request.getRespObjClass(),
                rootResp,isReturnJson,request);
    }

    /******************************************************/
    /******************Async request************************/
    /******************************************************/
    /**
     * Get data by async request list.
     * @param requestCallback
     * @param requestRefactor 重置请求参数回调
     * @param baseRespCls 返回json的第一级节点信息， 需要实现{@link IRespBase}接口<br>
     *      当值为null时采用默认基类节点{@link #baseRespClass}
     * @param httpServer
     *  自己指定网络请求框架
     * @param requestList
     * @return
     */
    protected static boolean getData(IReqCallback requestCallback,
                                     IReqRefactor requestRefactor,
                                     Class baseRespCls,
                                     IHttpServer httpServer,
                                     IBaseRequest... requestList) {
        if( !isInit() ){
            return false ;
        }
        if( baseRespCls == null ){
            baseRespCls = baseRespClass;
        }else {
            if (!checkBaseRespClass(baseRespCls)) {
                return false;
            }
        }

        if( httpServer == null ){
            httpServer = httpServerImpl ;
        }

        //执行请求
        ReqDispatcher dispatcher = new ReqDispatcher(context,
                requestList,requestCallback,requestRefactor,
                baseRespCls,isReturnJson,httpServer);
        DefReqErrReceiver reqErrReceiver = new DefReqErrReceiver(context);
        dispatcher.setReqErrorReceiver(reqErrReceiver);
        return dispatcher.onDispatch();
    }
}
