package com.cutil.log;

import com.cutil.CUtil;
import com.cutil.OUtil;
import com.cutil.ST;

/**
 * @author Cuckoo
 * @date 2017-05-11
 * @descitpion
 *      日志工具类, 统一输出日志
 */

public class CLog{
    private static ILog log = null ;

    /**
     * 设置log实现类
     * @param logImpl
     */
    public static void setLogImpl(ILog logImpl){
        log = logImpl;
    }

    public static void e(String msg) {
        log(LogType.E,null,msg,getMethodInfo());
    }

    
    public static void e(String tag, String msg) {
        log(LogType.E,tag,msg,getMethodInfo());
    }

    public static  void e(String tag, String msg, Throwable e) {
        if(isOn()){
            if(e != null && !ST.isEmpty(e.getMessage())){
                msg = ST.f(msg) + e.getMessage();
            }
            log(LogType.E,tag,msg,getMethodInfo());
        }
    }

    public static  void w(String msg) {
        log(LogType.W,null,msg,getMethodInfo());
    }

    public static  void w(String tag, String msg) {
        log(LogType.W,tag,msg,getMethodInfo());
    }

    
    public static  void i(String msg) {
        log(LogType.I,null,msg,getMethodInfo());
    }

    
    public static  void i(String tag, String msg) {
        log(LogType.I,tag,msg,getMethodInfo());
    }

    public static  void d(String msg) {
        log(LogType.D,null,msg,getMethodInfo());
    }

    public static  void d(String tag, String msg) {
        log(LogType.D,tag,msg,getMethodInfo());
    }

    /**
     * 打印日志
     * @param logType
     *  日志类型
     * @param tag
     *  日志tag
     * @param msg
     * @param methodInfo
     *  方法信息， 如果有值就打印
     */
    private static  void log(LogType logType, String tag, String msg, String methodInfo) {
        if( isOn() ){
            if(!ST.isEmpty(methodInfo)){
                msg = methodInfo+ msg;
            }
            if(ST.isEmpty(tag)){
                tag = CUtil.LOG_TAG;
            }
            if(LogType.D == logType){
                log.d(tag,msg);
            }else if(LogType.I == logType){
                log.i(tag,msg);
            }else if(LogType.W == logType){
                log.w(tag,msg);
            }else if(LogType.E == logType){
                log.e(tag,msg);
            }
        }
    }

    /**
     * 获取方法信息,当允许打日志并且允许显示调用者信息时 返回值
     * @return
     */
    private static String getMethodInfo(){
        if(isOn() &&
                isShowMethod()){
            StackTraceElement element = OUtil.getPreviousCallerInfo(2);
            if( element != null ){
                return element.toString()+"\n";
            }
        }
        return null ;
    }

    /**
     * 是否开启日志
     * @return
     */
    private static boolean isOn(){
        if( log != null ){
            return log.isOn();
        }
        return false;
    }

    /**
     * 是否显示调用日志的类以及方法
     * @return
     */
    private static boolean isShowMethod(){
        if( log != null ){
            return log.isShowMethod();
        }
        return false;
    }

    /**
     * 日志类型
     */
    enum LogType{
        D,I,W,E;
    }
}
