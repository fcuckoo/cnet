package com.cnet.def.http.resp;

/**
 *
 * @author Cuckoo
 * @date 2016-09-05
 * @description
 *      Error info when reqeust failure.
 */
public interface IErrResp extends IHttpResp{
    int STATUS_RESPNULL = 1000;
    int STATUS_REQUESTNULL = 1001;
    int STATUS_BUSINESS_FAILURE = 1002;
    int STATUS_RESP_NON200 = 3000;
    int STATUS_PARSE_JSON_ERROR = 1004;
    int STATUS_BASERESPONSE_ERROR = 1005;
    //不支持当前请求类型
    int STATUS_REQUEST_UNSUPPORT = 1006;
    //未知异常
    int STATUS_UNKNOWN = 1007;
    //无网络
    int STATUS_NONET = 1008 ;
    //自定义超时时间生效，中断请求
    int STATUS_CUSTOM_TIMEOUT_BREAK = 1009 ;
    //请求地址无效
    int STATUS_UNKNOWN_HOST = 1010 ;
    //连接异常
    int STATUS_CONNECT_EXCEPTION = 1011 ;






    /**
     * Get status fo interface.
     * @return
     */
    int getStatus();

    /**
     * Get error message.
     * @return
     */
    String getErrMsg() ;
    /**
     * 请求原始异常码
     * @return
     */
    int getErrorCode();
}
