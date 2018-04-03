package com.cutil;

import android.widget.EditText;


import com.cutil.log.CLog;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;


/**
 *
 * @author Cuckoo
 * @date 2017-03-22
 * @description
 *      处理数字相关的工具类
 */

public class NumberUtil {
    /**
     * 检查小数点位数
     * @param targetEt
     * @param max
     *  小数点后的最大位数
     * @return
     *      true:小数点位数超过最大位数
     */
    public static boolean checkDecimalDigits(EditText targetEt, int max){
        String value = targetEt.getText().toString().trim();
        int index = value.indexOf(".");
        if(index >= 0 ){
            //包含小数
            int size = value.length()- (index +1) ;
            if(size > max){
                return true ;
            }
        }
        return false ;
    }

    public static double subDecimalPoint(double target, int decimalPointSize){
        double  denominator = 1 ;
        if( decimalPointSize > 0 ){
            for(int i = 0 ;i < decimalPointSize ;i ++){
                denominator *= 10 ;
            }
            return Math.round(Math.round(target * denominator))/denominator;
        }
        return target ;
    }

    /**
     * 将数值转换成指定小数点位数
     * @param target
     * @param decimalPointSize
     * 		需要显示的小数点位数
     * @return
     */
    public static String subDecimalPointByString(double target, int decimalPointSize){
        String format = "#";
        if( decimalPointSize > 0){
            format +=".";
            for(int i = 0 ;i < decimalPointSize ;i ++){
                format +="#";
            }
        }else {
            return parse2Int(target);
        }
        DecimalFormat df= new DecimalFormat(format);
        return df.format(target);
    }

    /**
     * 字符串转成double类型
     * @param et
     * @return
     */
    public static double parse2Double(EditText et){
        try {
            return Double.parseDouble(ST.getString(et));
        }catch (Exception e){
            CLog.e(null,null,e);
        }
        return -1 ;
    }

    /**
     * 字符串转成double类型
     * @param value
     * @return
     */
    public static double parse2Double(String value){
        try {
           return Double.parseDouble(value);
        }catch (Exception e){
            CLog.e(null,null,e);
        }
        return -1 ;
    }

    /**
     * 取正数
     * @param et
     * @return
     */
    public static int parse2Int(EditText et){
        return parse2Int(ST.getString(et));
    }

    /**
     * 取正数
     * @param et
     * @return
     */
    public static float parse2Float(EditText et){
        return parse2Float(ST.getString(et));
    }

    /**
     * 将字符串转成整型，转换失败返回-1
     * @param value
     * @return
     */
    public static int parse2Int(String value){
        try{
            return Integer.parseInt(ST.f(value));
        }catch (Exception e){

        }
        return -1;
    }

    /**
     * 将字符串转成整型，转换失败返回-1
     * @param value
     * @return
     */
    public static float parse2Float(String value){
        try{
            return Float.parseFloat(ST.f(value));
        }catch (Exception e){

        }
        return 0;
    }

    /**
     * 取正数
     * @param target
     * @return
     */
    public static String parse2Int(double target){
        String value = ""+ target;
        int index = value.indexOf(".");
        if( index > 0 ){
            return value.substring(0,index);
        }
        return value ;
    }

    /**
     * 判断target是否在min和max之间
     * @param target
     * @param min
     * @param max
     * @return
     */
    public static boolean isBetween(double target, double min, double max){
        if( target >=min && target <= max){
            return true ;
        }
        return false ;
    }

    /**
     * 判断target是否在min和max之间
     * @param target
     * @param min
     * @param max
     * @return
     */
    public static boolean isBetween(float target, float min, float max){
        if( target >=min && target <= max){
            return true ;
        }
        return false ;
    }

    /**
     * 比较两数的大小
     * @param b1
     * @param b2
     * @return
     *  1: b1大，
     *  =0：一样大
     *  -1:b2大
     */
    public static int compare(BigDecimal b1, BigDecimal b2){
        if(b1 == null ){
            b1 = BigDecimal.ZERO;
        }
        if( b2 == null ){
            b2 = BigDecimal.ZERO;
        }
        return b1.compareTo(b2);
    }

    /**
     * 大于
     * @param b1
     * @param b2
     * @return
     */
    public static boolean over(BigDecimal b1, BigDecimal b2){
        if(b1 == null ){
            b1 = BigDecimal.ZERO;
        }
        if( b2 == null ){
            b2 = BigDecimal.ZERO;
        }
        int res = b1.compareTo(b2);
        return res == 1 ;
    }

    /**
     * 大于等于
     * @param b1
     * @param b2
     * @return
     */
    public static boolean overEq(BigDecimal b1, BigDecimal b2){
        if(b1 == null ){
            b1 = BigDecimal.ZERO;
        }
        if( b2 == null ){
            b2 = BigDecimal.ZERO;
        }
        int res = b1.compareTo(b2);
        if( res == 0 ||
                res == 1){
            return true ;
        }
        return false;
    }

    /**
     * 提供精确加法计算的add方法
     *
     * @return 两个参数的和
     */
    public static BigDecimal add(BigDecimal b1, BigDecimal b2){
        if(b1==null){
            b1= new BigDecimal(0);
        }
        if(b2==null){
           return b1;
        }
        return b1.add(b2);
    }

    public static BigDecimal add(int b1, BigDecimal b2){
        return add(new BigDecimal(b1),b2);
    }

    public static BigDecimal add(long b1, BigDecimal b2){
        return add(new BigDecimal(b1),b2);
    }

    public static BigDecimal add(float b1, BigDecimal b2){
        return add(new BigDecimal(b1),b2);
    }

    /**
     * 提供精确减法运算的sub方法
     * @param b1 被减数
     * @param b2 减数
     * @return 两个参数的差
     */
    public static BigDecimal sub(BigDecimal b1, BigDecimal b2){
        if(b1==null){
            return new BigDecimal(0);
        }
        if(b2==null){
            return b1;
        }
        return b1.subtract(b2);
    }

    /**
     * 提供精确乘法运算的mul方法
     * @return 两个参数的积
     */
    public static BigDecimal mul(BigDecimal b1, BigDecimal b2){
        if(b1==null || b2==null){
            return new BigDecimal(0);
        }
        return b1.multiply(b2);
    }

    public static BigDecimal mul(int b1, BigDecimal b2){
       return  mul(new BigDecimal(Integer.toString(b1)),b2);
    }

    public static BigDecimal mul(long b1, BigDecimal b2){
        return  mul(new BigDecimal(Long.toString(b1)),b2);
    }

    public static BigDecimal mul(float b1, BigDecimal b2){
        return  mul(new BigDecimal(Float.toString(b1)),b2);
    }

    /**
     * 提供精确除法运算的divide方法
     * @return
     */
    public static BigDecimal divide(BigDecimal b1, BigDecimal b2){
        if(b1==null || b2==null){
            return new BigDecimal(0);
        }
        if(b2.compareTo(new BigDecimal(0))==0){
            return b1;
        }
        return b1.divide(b2, 5, RoundingMode.HALF_UP);
    }

    /**
     * 提供精确除法运算的divide方法
     * @param b1
     * @param b2
     * @param scale
     *  除之后小数点位数
     * @return
     */
    public static BigDecimal divide(BigDecimal b1, BigDecimal b2, int scale){
        if(b1==null || b2==null){
            return new BigDecimal(0);
        }
        if(b2.compareTo(new BigDecimal(0))==0){
            return b1;
        }
        return b1.divide(b2, scale, RoundingMode.HALF_UP);
    }

    /**
     * 过滤小数点后面位数
     * @param target
     * @param maxdecimals
     *  小数点位数
     * @return
     */
    public static BigDecimal filter(BigDecimal target, int maxdecimals){
        if( target == null ){
            return null;
        }
        if(target.scale() > maxdecimals){
            BigDecimal newValue = target.setScale(maxdecimals, BigDecimal.ROUND_DOWN);
            return newValue.stripTrailingZeros();
        }
        return target;
    }

    /**
     * 取反，既用当前值乘以-1
     * @return
     */
    public static BigDecimal negate(BigDecimal target){
        if(target == null ){
            return target;
        }
        return target.multiply(new BigDecimal("-1"));
    }

    /**
     * 是否为0， 当target为null时也认为是0
     * @param target
     * @return
     */
    public static boolean isZero(BigDecimal target){
        if(target == null ){
            return true ;
        }
        return BigDecimal.ZERO.compareTo(target) == 0;
    }

    /**
     * 判断两值是否相同
     * @param l
     * @param r
     * @return
     */
    public static boolean eq(BigDecimal l, BigDecimal r){
        if( l == null && r == null ){
            return true ;
        }else if(l != null && r != null ){
            return l.compareTo(r) == 0 ;
        }else {
            return false;
        }
    }

    /**
     * 检测target是否为null，如果为null， 返回0
     * @param target
     * @return
     */
    public static BigDecimal filter(BigDecimal target){
        if( target == null ){
            return BigDecimal.ZERO;
        }
        return target;
    }

    public static boolean overZero(BigDecimal b){
        if(b==null){
            return false;
        }
        return b.compareTo(new BigDecimal(0))==1;
    }

    public static boolean overAndEqualZero(BigDecimal b){
        if(b==null){
            return false;
        }
        return b.compareTo(new BigDecimal(0))>=0;
    }

    /**
     * 字符串转成BigDecimal类型
     * @param et
     * @return
     */
    public static BigDecimal parse2BigDecimal(EditText et){
        return parse2BigDecimal(ST.getString(et));
    }

    public static BigDecimal parse2BigDecimal(String str){
        if(ST.isEmpty(str)){
            return new BigDecimal(0);
        }
        try{
            return new BigDecimal(str);
        }catch (Exception e){
            return new BigDecimal(0);
        }

    }

    /**
     * 判断target是否在min和max之间
     * @param target
     * @param min
     * @param max
     * @return
     */
    public static boolean isBetween(BigDecimal target, BigDecimal min, BigDecimal max){
        if(target == null ){
            target = BigDecimal.ZERO;
        }
        if(min == null ){
            min = BigDecimal.ZERO;
        }
        if(max == null ){
            max = BigDecimal.ZERO;
        }
        int resultMin=target.compareTo(min);
        int resultMax=target.compareTo(max);
        if(resultMin>=0 && resultMax<=0){
            return true;
        }
        return false ;
    }

}
