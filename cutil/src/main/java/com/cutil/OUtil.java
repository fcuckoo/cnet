package com.cutil;

import com.cutil.log.CLog;

/**
 *
 * @author Cuckoo
 * @date 2017-05-11
 * @description
 *      其它工具类
 */

public class OUtil {

    /**
     * 获取调用当前方法前的第二个方法信息
     * @param methodLayer
     *  查看调用当前方法之前几层方法。 如{@link CLog#d(String, String)}调用改方法，需要查看改方法的信息，其值为1
     * @return
     */
    public static StackTraceElement getPreviousCallerInfo(int methodLayer){
        StackTraceElement[] elements  = Thread.currentThread().getStackTrace();
        /**
         * 前三层的方法分别为：
         *  0 = {StackTraceElement@3668} "dalvik.system.VMStack.getThreadStackTrace(Native Method)"
         *  1 = {StackTraceElement@3669} "java.lang.Thread.getStackTrace(Thread.java:580)"
         *  2 = {StackTraceElement@3670} "com.cutil.OUtil.getPreviousCallerInfo(OUtil.java:22)"
         */

        int layer = 3 + methodLayer;
        if(elements!= null && elements.length > layer){
            //根据传入的参数以及方法名 获取对应的方法
            return elements[layer];
        }
        return null ;
    }
}
