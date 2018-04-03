package com.cnetsample.demo.resp;


import com.cutil.CListUtil;
import com.cutil.ST;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;

/**Response - 收银员登录*/
public class RespGetCashier {
    //管理员角色
    private final transient String ROLE_ADMIN = "3" ;
    /**工号*/
    @SerializedName("m0501")
    private String jobNo;
    /**密码*/
    private String m0504;
    /**专柜号*/
    @SerializedName("m0506")
    private String shoppeNo;

    /**所属角色*/
    private String m0507;
    /**角色权限 值为3代表管理员*/
    @SerializedName("m0508")
    private String role;
    /**会员注册地址，根据收银员登录门店动态生成链接*/
    private String registerurl;
    /**门店名称*/
    @SerializedName("storename")
    private String storeName;
    /**门店号*/
    @SerializedName("storeno")
    private String storeNo;
    /**服务器端配置信息*/
    @SerializedName("configure")
    private ArrayList<Configure> configList = null ;

    /**服务器端返回的接口配置信息，并整理 key:配置名称，配置内容*/
    private transient HashMap<String, Configure> configMap = null ;

    public String getJobNo() {
        return jobNo;
    }

    public void setJobNo(String jobNo) {
        this.jobNo = jobNo;
    }

    public String getM0504() {
        return m0504;
    }

    public void setM0504(String m0504) {
        this.m0504 = m0504;
    }

    public String getShoppeNo() {
        return shoppeNo;
    }

    public void setShoppeNo(String shoppeNo) {
        this.shoppeNo = shoppeNo;
    }

    public String getM0507() {
        return m0507;
    }

    public void setM0507(String m0507) {
        this.m0507 = m0507;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRegisterurl() {
        return registerurl;
    }

    public void setRegisterurl(String registerurl) {
        this.registerurl = registerurl;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreNo() {
        return storeNo;
    }

    public void setStoreNo(String storeNo) {
        this.storeNo = storeNo;
    }

    public boolean isAdmin(){
        return ROLE_ADMIN.equalsIgnoreCase(getRole());
    }

    private HashMap<String,Configure> getConfigMap(){
        if( configMap == null && !CListUtil.isEmpty(configList)){
            configMap = new HashMap<>();
            for(Configure config: configList){
                configMap.put(config.getName(),config);
            }
        }
        return configMap;
    }

    /**
     * 获取预付费卡相关的域名以及端口信息
     * @return
     */
    public String getPrepaidCardBaseUrl() {
        String hostKey = "Mobilehost";
        String portKey = "Mobileport";
        return getBaseUrl(hostKey,portKey);
    }

    /**
     * 获取券相关的域名以及端口
     * @return
     */
    public String getCouponBaseUrl(){
        String hostKey = "Host3";
        String portKey = "Port3";
        return getBaseUrl(hostKey,portKey);
    }

    /**
     * 获取会员相关的域名以及端口
     * @return
     */
    public String getMemberBaseUrl(){
        String hostKey = "Host4";
        String portKey = "Port4";
        return getBaseUrl(hostKey,portKey);
    }

    /**
     * 获取相关的域名以及端口信息,并拼接为url
     * @return
     */
    private String getBaseUrl(String hostKey, String portKey){
        String host = null;
        String port = null;
        Configure hostConfig = getConfigMap().get(hostKey);
        Configure portConfig = getConfigMap().get(portKey);
        if( hostConfig != null ){
            host = hostConfig.getValue();
        }
        if( portConfig != null ){
            port = portConfig.getValue();
        }
        String url = null;
        if(!ST.isEmpty(host)){
            url = "http://"+host;
            if(!ST.isEmpty(port)){
                url += ":" + port;
            }
        }
        return url ;
    }

    class Configure{
        @SerializedName("title")
        private String title;
        @SerializedName("name")
        private String name ;
        @SerializedName("value")
        private String value ;
        @SerializedName("code")
        private String code ;
        @SerializedName("type")
        private String type ;

        public String getTitle() {
            return title;
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }

        public String getCode() {
            return code;
        }

        public String getType() {
            return type;
        }
    }
}
