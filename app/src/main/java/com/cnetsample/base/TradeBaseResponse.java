package com.cnetsample.base;

import com.cnet.def.http.resp.IRespBase;
import com.google.gson.annotations.SerializedName;

/**
 *
 * @author Cuckoo
 * @date 2017-03-17
 * @description
 *      处理与交易相关的接口，主要是{@link NetCertificate#TRADE}相关的接口
 *
 */

public class TradeBaseResponse<T> implements IRespBase<T> {
    final String SUB_SUCCESS = "00";




    /**  00代表正确，负数均为错误	 */
    @SerializedName("errcode")
    private String subStatus = null;

    /** 请求结果的描述，如果失败，则为失败原因*/
    @SerializedName("errmsg")
    private String msg = null ;

    private transient String json;
    private transient Object tag;
    private transient int resultStatus ;

    @Override
    public boolean isSuccess(){
        return SUB_SUCCESS.equals(subStatus);
    }


    @SerializedName("data")
    private T respObj ;

    @Override
    public T getRespData() {
        return respObj;
    }

    @Override
    public String getJson() {
        return json;
    }

    @Override
    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    public void setJson(String json) {
        this.json = json;
    }

    @Override
    public int getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(int resultStatus) {
        this.resultStatus = resultStatus;
    }

    public void setRespObj(T respObj) {
        this.respObj = respObj;
    }

    @Override
    public String getMessage() {
        return msg;
    }

    @Override
    public void setMessage(String message) {
        this.msg = message;
    }
}
