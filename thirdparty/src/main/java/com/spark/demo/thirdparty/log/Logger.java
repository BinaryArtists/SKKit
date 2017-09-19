package com.spark.demo.thirdparty.log;

import android.util.Log;

import com.spark.demo.thirdparty.utils.UtilsPackage;


/**
 * Created by Wangxiaxin on 2015/9/21.
 *
 * Logger 输出类
 *
 * 级别分为：VERBOSE, DEBUG, INFO, WARN, ERROR, ASSERT
 *
 * 所有情况下：
 *
 * 1，Logger.o Logger.w Logger.e Logger.wtf 在输出logcat的同时，还会输出到log文件，所以请慎重使用
 *
 * 2，对于正常的运营日志，请使用Logger.o 来输出
 *
 *
 *
 *
 * 
 * debug情况下：所有log都会输出
 *
 * release情况下：Logger.v Logger.d Logger.i 不会输出
 */
public class Logger {
    
    private static final boolean sIsDebugable;
    private static final String STACK_INFO_FORMAT = "%s: %s.%s(L:%d) ";
    private static final int sReleaseLogLevel = Log.WARN;
    private static final int STACK_INDEX = 4;
    private static final String DEFAULT_TAG = "spark=+=";
    
    static {
        sIsDebugable = UtilsPackage.isApkDebugable();
    }
    
    public static String getExceptionMessage(Exception e) {
        return (e == null ? "empty" : e.getMessage());
    }

    public static int v(String msg) {
        if (couldLog(Log.VERBOSE)) {
            String stackInfo = getLineInfo(Log.VERBOSE);
            return Log.v(DEFAULT_TAG, wrapMsgWithStackInfo(msg, stackInfo));
        }
        else
            return 0;
    }

    public static int v(String tag, String msg) {
        if (couldLog(Log.VERBOSE)) {
            String stackInfo = getLineInfo(Log.VERBOSE);
            return Log.v(tag, wrapMsgWithStackInfo(msg, stackInfo));
        }
        else
            return 0;
    }

    public static int v(String msg, Throwable tr) {
        if (couldLog(Log.VERBOSE)) {
            String stackInfo = getLineInfo(Log.VERBOSE);
            return Log.v(DEFAULT_TAG, wrapMsgWithStackInfo(msg, stackInfo), tr);
        }
        else
            return 0;
    }

    public static int v(String tag, String msg, Throwable tr) {
        if (couldLog(Log.VERBOSE)) {
            String stackInfo = getLineInfo(Log.VERBOSE);
            return Log.v(tag, wrapMsgWithStackInfo(msg, stackInfo), tr);
        }
        else
            return 0;
    }

    public static int d(String msg) {
        if (couldLog(Log.DEBUG)) {
            String stackInfo = getLineInfo(Log.DEBUG);
            return Log.d(DEFAULT_TAG, wrapMsgWithStackInfo(msg, stackInfo));
        }
        else
            return 0;
    }

    public static int d(String tag, String msg) {
        if (couldLog(Log.DEBUG)) {
            String stackInfo = getLineInfo(Log.DEBUG);
            return Log.d(tag, wrapMsgWithStackInfo(msg, stackInfo));
        }
        else
            return 0;
    }

    public static int d(String msg, Throwable tr) {
        if (couldLog(Log.DEBUG)) {
            String stackInfo = getLineInfo(Log.DEBUG);
            return Log.d(DEFAULT_TAG, wrapMsgWithStackInfo(msg, stackInfo), tr);
        }
        else
            return 0;
    }

    public static int d(String tag, String msg, Throwable tr) {
        if (couldLog(Log.DEBUG)) {
            String stackInfo = getLineInfo(Log.DEBUG);
            return Log.d(tag, wrapMsgWithStackInfo(msg, stackInfo), tr);
        }
        else
            return 0;
    }

    public static int i(String msg) {
        if (couldLog(Log.INFO)) {
            String stackInfo = getLineInfo(Log.INFO);


            return Log.i(DEFAULT_TAG, wrapMsgWithStackInfo(msg, stackInfo));
        }
        else
            return 0;
    }
    
    public static int i(String tag, String msg) {
        if (couldLog(Log.INFO)) {
            String stackInfo = getLineInfo(Log.INFO);
            return Log.i(tag, wrapMsgWithStackInfo(msg, stackInfo));
        }
        else
            return 0;
    }

    public static int i(String msg, Throwable tr) {
        if (couldLog(Log.INFO)) {
            String stackInfo = getLineInfo(Log.INFO);
            return Log.i(DEFAULT_TAG, wrapMsgWithStackInfo(msg, stackInfo), tr);
        }
        else
            return 0;
    }

    public static int i(String tag, String msg, Throwable tr) {
        if (couldLog(Log.INFO)) {
            String stackInfo = getLineInfo(Log.INFO);
            return Log.i(tag, wrapMsgWithStackInfo(msg, stackInfo), tr);
        }
        else
            return 0;
    }

    public static int o(String msg) {
        String stackInfo = getLineInfo(Log.INFO);
        int len = Log.i(DEFAULT_TAG, wrapMsgWithStackInfo(msg, stackInfo));
//        UtilsLog.getInstance().saveLogFile(DEFAULT_TAG,
//                wrapMsgWithStackInfo(msg, stackInfo)); // todo by spark
        return len;
    }

    /** 单独输出运营日志，归类于INFO */
    public static int o(String tag, String msg) {
        String stackInfo = getLineInfo(Log.INFO);
        int len = Log.i(tag, wrapMsgWithStackInfo(msg, stackInfo));
//        UtilsLog.getInstance().saveLogFile(tag, wrapMsgWithStackInfo(msg, stackInfo)); // todo by spark
        return len;
    }

    public static int w(String msg) {
        if (couldLog(Log.WARN)) {
            String stackInfo = getLineInfo(Log.WARN);
            int len = Log.w(DEFAULT_TAG, wrapMsgWithStackInfo(msg, stackInfo));
//            UtilsLog.getInstance().saveLogFile(DEFAULT_TAG,
//                    wrapMsgWithStackInfo(msg, stackInfo)); // todo by spark
            return len;
        }
        else
            return 0;
    }

    public static int w(String tag, String msg) {
        if (couldLog(Log.WARN)) {
            String stackInfo = getLineInfo(Log.WARN);
            int len = Log.w(tag, wrapMsgWithStackInfo(msg, stackInfo));
//            UtilsLog.getInstance().saveLogFile(tag, wrapMsgWithStackInfo(msg, stackInfo)); // todo by spark
            return len;
        }
        else
            return 0;
    }

    public static int w(String tag, String msg, Throwable tr) {
        if (couldLog(Log.WARN)) {
            String stackInfo = getLineInfo(Log.WARN);
            int len = Log.w(tag, wrapMsgWithStackInfo(msg, stackInfo));
//            UtilsLog.getInstance().saveLogFile(tag,
//                    wrapMsgWithStackInfo(msg, stackInfo) + getStackTraceInfo(tr)); // todo by spark
            return len;
        }
        else
            return 0;
    }

    public static int w(Throwable tr) {
        if (couldLog(Log.WARN)) {
            String stackInfo = getLineInfo(Log.WARN);
            int len = Log.w(DEFAULT_TAG, wrapMsgWithStackInfo("", stackInfo), tr);
//            UtilsLog.getInstance().saveLogFile(DEFAULT_TAG,
//                    wrapMsgWithStackInfo("", stackInfo), getStackTraceInfo(tr));   // todo by spark
            return len;
        }
        else
            return 0;
    }

    public static int w(String tag, Throwable tr) {
        if (couldLog(Log.WARN)) {
            String stackInfo = getLineInfo(Log.WARN);
            int len = Log.w(tag, wrapMsgWithStackInfo("", stackInfo), tr);
//            UtilsLog.getInstance().saveLogFile(tag, wrapMsgWithStackInfo("", stackInfo),
//                    getStackTraceInfo(tr));    // todo by spark
            return len;
        }
        else
            return 0;
    }

    public static int e(String msg) {
        if (couldLog(Log.ERROR)) {
            String stackInfo = getLineInfo(Log.ERROR);
            int len = Log.e(DEFAULT_TAG, wrapMsgWithStackInfo(msg, stackInfo));
//            UtilsLog.getInstance().saveLogFile(DEFAULT_TAG,
//                    wrapMsgWithStackInfo(msg, stackInfo));   // todo by spark
            return len;
        }
        else
            return 0;
    }

    public static int e(String tag, String msg) {
        if (couldLog(Log.ERROR)) {
            String stackInfo = getLineInfo(Log.ERROR);
            int len = Log.e(tag, wrapMsgWithStackInfo(msg, stackInfo));
//            UtilsLog.getInstance().saveLogFile(tag, wrapMsgWithStackInfo(msg, stackInfo));   // todo by spark
            return len;
        }
        else
            return 0;
    }

    public static int e(String msg, Throwable tr) {
        if (couldLog(Log.ERROR)) {
            String stackInfo = getLineInfo(Log.ERROR);
            int len = Log.e(DEFAULT_TAG, wrapMsgWithStackInfo(msg, stackInfo), tr);
//            UtilsLog.getInstance().saveLogFile(DEFAULT_TAG,
//                    wrapMsgWithStackInfo(msg, stackInfo), getStackTraceInfo(tr));   // todo by spark
            return len;
        }
        else
            return 0;
    }

    public static int e(String tag, String msg, Throwable tr) {
        if (couldLog(Log.ERROR)) {
            String stackInfo = getLineInfo(Log.ERROR);
            int len = Log.e(tag, wrapMsgWithStackInfo(msg, stackInfo), tr);
//            UtilsLog.getInstance().saveLogFile(tag, wrapMsgWithStackInfo(msg, stackInfo),
//                    getStackTraceInfo(tr));   // todo by spark
            return len;
        }
        else
            return 0;
    }

    public static int wtf(String msg) {
        if (couldLog(Log.ASSERT)) {
            String stackInfo = getLineInfo(Log.ASSERT);
            int len = Log.wtf(DEFAULT_TAG, wrapMsgWithStackInfo(msg, stackInfo));
//            UtilsLog.getInstance().saveLogFile(DEFAULT_TAG,
//                    wrapMsgWithStackInfo(msg, stackInfo));    // todo by spark
            return len;
        }
        else
            return 0;
    }

    public static int wtf(String tag, String msg) {
        if (couldLog(Log.ASSERT)) {
            String stackInfo = getLineInfo(Log.ASSERT);
            int len = Log.wtf(tag, wrapMsgWithStackInfo(msg, stackInfo));
//            UtilsLog.getInstance().saveLogFile(tag, wrapMsgWithStackInfo(msg, stackInfo)); // todo by spark
            return len;
        }
        else
            return 0;
    }

    public static int wtf(String msg, Throwable tr) {
        if (couldLog(Log.ASSERT)) {
            String stackInfo = getLineInfo(Log.ASSERT);
            int len = Log.wtf(wrapMsgWithStackInfo(DEFAULT_TAG, stackInfo), msg, tr);
//            UtilsLog.getInstance().saveLogFile(
//                    wrapMsgWithStackInfo(DEFAULT_TAG, stackInfo),
//                    msg + getStackTraceInfo(tr));     // todo by spark
            return len;
        }
        else
            return 0;
    }

    public static int wtf(String tag, String msg, Throwable tr) {
        if (couldLog(Log.ASSERT)) {
            String stackInfo = getLineInfo(Log.ASSERT);
            int len = Log.wtf(wrapMsgWithStackInfo(tag, stackInfo), msg, tr);
//            UtilsLog.getInstance().saveLogFile(wrapMsgWithStackInfo(tag, stackInfo),
//                    msg + getStackTraceInfo(tr));   // todo by spark
            return len;
        }
        else
            return 0;
    }

    private static String wrapMsgWithStackInfo(String msg, String stackinfo) {
        return String.format("[%s] %s", stackinfo, msg);
    }

    private static boolean couldLog(int level) {
        //Log.i("spark", "couldLog_level:"+level+", sIsDebugable:"+sIsDebugable);
        return sIsDebugable || level >= sReleaseLogLevel;
    }
    
    private static String getStackTraceInfo(Throwable tr) {
        
        StringBuilder sb = new StringBuilder();
        sb.append(tr.getMessage() + "\n");
        StackTraceElement[] elements = tr.getStackTrace();
        for (StackTraceElement el : elements) {
            sb.append(el.getClassName() + "--" + el.getMethodName() + "  L:"
                    + el.getLineNumber() + "\n");
        }
        return sb.toString();
    }
    
    private static String getLogLevelString(int level) {
        switch (level) {
            case Log.VERBOSE:
                return "V";
            case Log.DEBUG:
                return "D";
            case Log.INFO:
            default:
                return "I";
            case Log.WARN:
                return "W";
            case Log.ERROR:
                return "E";
            case Log.ASSERT:
                return "A";
        }
    }
    
    private static String getLineInfo(int level) {
        StackTraceElement ele = Thread.currentThread().getStackTrace()[STACK_INDEX];
        if (ele != null) {
            String fullClassName = ele.getClassName();
            String className = fullClassName
                    .substring(fullClassName.lastIndexOf(".") + 1);
            return String.format(STACK_INFO_FORMAT, getLogLevelString(level), className,
                    ele.getMethodName(), ele.getLineNumber());
        }
        return "";
    }
}
