package com.cnetsample.log;

import com.cnetsample.LogUtil;
import com.cutil.log.ILog;

/**
 * Log 具体实现类
 */

public class LogImpl implements ILog{
    @Override
    public boolean isOn() {
        return true;
    }

    @Override
    public boolean isShowMethod() {
        return true;
    }

    @Override
    public void e(String tag, String msg) {
        LogUtil.e(tag,msg);
    }

    @Override
    public void w(String tag, String msg) {
        LogUtil.w(tag,msg);
    }

    @Override
    public void i(String tag, String msg) {
        LogUtil.i(tag,msg);
    }

    @Override
    public void d(String tag, String msg) {
        LogUtil.d(tag,msg);
    }
}
