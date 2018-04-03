package com.cnetsample.demo.resp;


import com.cnet.util.gson.GsonUtil;
import com.cnetsample.base.CRequest;

/**Request - 收银员登录*/
public class ReqGetCashier extends CRequest {
    public DataBean data;

    public ReqGetCashier() {
        super(null);
        setBaseUrl("http://122.224.218.140:8888");
        setPostFixUrl("/services/wlpos/getcashier.json");
        setShowLoadding(true);
    }

    protected String getDataString() {
        return GsonUtil.toJson(data);
    }

    public static class DataBean {
        /**收银员工号*/
        public String cashier;
        /**用户密码，md5加密*/
        public String password;
        /**收银机号*/
        public String posno;
        /**门店号*/
        public String storeno;
    }

}
