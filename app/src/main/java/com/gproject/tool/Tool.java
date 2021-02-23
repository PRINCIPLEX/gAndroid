package com.gproject.tool;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import java.io.*;
import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName: Tool
 * @Description:杂项工具类
 */
public class Tool {


    public static byte[] FileToByte(File file) {
        byte[] buffer = null;
        FileInputStream fis = null;
        ByteArrayOutputStream bos = null;
        try {
            fis = new FileInputStream(file);

            bos = new ByteArrayOutputStream();

            byte[] b = new byte[1024];

            int n;

            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }

            buffer = bos.toByteArray();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return buffer;
        }
    }


    /**
     * 退出程序
     */
    public static void exitApp() {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    /**
     * 判断字符串是否仅为数字
     *
     * @param str 要进行判断的字符串
     * @return true：纯数字 false：否
     */
    public static boolean isNumeric(String str) {
        for (int i = str.length(); --i >= 0; ) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 文本提取淘口令或链接，如果什么都没有，返回空
     *
     * @param tk 淘宝口令或者链接
     * @return 淘宝口令或者链接
     */
    public static String matObject(String tk) {
        //先判断是否有链接
        Pattern a = Pattern.compile("https?://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]");
        Matcher aa = a.matcher(tk);
        if (aa.find()) {
            System.out.println(aa.group());
            return aa.group();
        }
        //如果没有链接，则判断淘口令
        String pattern = "([\\p{Sc}])\\w{8,12}([\\p{Sc}])";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(tk);
        if (m.find()) {
            System.out.println("match: " + m.group());
            return m.group();
        }
        return null;
    }

    public static <T> boolean notEmpty(List<T> list) {
        return !isEmpty(list);
    }

    public static <T> boolean isEmpty(List<T> list) {
        if (list == null || list.size() == 0) {
            return true;
        }
        return false;
    }

    // 将px值转换为dip或dp值
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    // 将dip或dp值转换为px值
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    // 将px值转换为sp值
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    // 将sp值转换为px值
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    // 屏幕宽度（像素）
    public static int getWindowWidth(Activity context) {
        DisplayMetrics metric = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric.widthPixels;
    }

    // 屏幕高度（像素）
    public static int getWindowHeight(Activity context) {
        DisplayMetrics metric = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric.heightPixels;
    }

    public static void setStatusBarHeight(Activity activity, View view) {
        //获取到高度，给View设置，在浅色背景下，这段代码加上，状态栏会设置没问题，但是有细微差距
        int bar_height = com.gproject.tool.StatusBarUtil.getStatusBarHeight(activity);
        LinearLayout.LayoutParams layoutParams;
        layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
        layoutParams.height = bar_height - 12;
        view.setLayoutParams(layoutParams);
    }

    public static void setStatusBarHeightNull(Activity activity, View view) {
        //获取到高度，给View设置，在浅色背景下，这段代码加上，状态栏会设置没问题，但是有细微差距
        LinearLayout.LayoutParams layoutParams;
        layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
        layoutParams.height = 0;
        view.setLayoutParams(layoutParams);
    }

    // 根据Unicode编码判断中文汉字和符号
    private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            return true;
        }
        return false;
    }

    // 判断中文汉字和符号
    public static boolean isChinese(String strName) {
        char[] ch = strName.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (isChinese(c)) {
                return true;
            }
        }
        return false;
    }

//
//    //        SharedPreferences存储用户对象
//    public static void saveUser(Context context, UserTokenInfoEntity userEntity) {
//        //创建sp对象,如果有key为"XY_USER"的sp就取出，否则就创建一个此key的sp对象
//        SharedPreferences sp = context.getSharedPreferences("APP2B_USER", Activity.MODE_PRIVATE);
//        //将对象转换成Json
//        String jsonStr = JSON.toJSONString(userEntity);
//        SharedPreferences.Editor editor;//获取编辑器
//        editor = sp.edit();
//        editor.putString("KEY_USER_DATA", jsonStr); //存入json串
//        editor.commit(); //提交
//    }
//
//    //SharedPreferences获取用户对象
//    public static UserTokenInfoEntity getUser(Context context) {
//        UserTokenInfoEntity userEntity = null;
//        //创建sp对象,如果有key为"XY_USER"的sp就取出
//        SharedPreferences sp = context.getSharedPreferences("APP2B_USER", Activity.MODE_PRIVATE);
//        //取出key为"KEY_USER_DATA"的值，如果值为空，则将第二个参数作为默认值赋值
//        String peopleJson = sp.getString("KEY_USER_DATA", "");
//        if (peopleJson != "")  //防空判断
//        {
//            //将json字符串转换成 people对象
//            userEntity = JSON.parseObject(peopleJson, UserTokenInfoEntity.class);
//        }
//        return userEntity;
//    }


    private static final char[] DIGITS_LOWER =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /*
     * KEY+ streamName + txTime
     */
    public static String getSafeUrl(String key, String streamName, String txTime) {

        String input = new StringBuilder().
                append(key).
                append(streamName).
                append(txTime).toString();

        String txSecret = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            txSecret = byteArrayToHexString(
                    messageDigest.digest(input.getBytes("UTF-8")));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return txSecret == null ? "" :
                new StringBuilder().
                        append("txSecret=").
                        append(txSecret).
                        append("&").
                        append("txTime=").
                        append(txTime).
                        toString();
    }

    private static String byteArrayToHexString(byte[] data) {
        char[] out = new char[data.length << 1];

        for (int i = 0, j = 0; i < data.length; i++) {
            out[j++] = DIGITS_LOWER[(0xF0 & data[i]) >>> 4];
            out[j++] = DIGITS_LOWER[0x0F & data[i]];
        }
        return new String(out);
    }


    // 状态栏高度
    private static int statusBarHeight = 0;
    // 屏幕像素点
    private static final Point screenSize = new Point();

    // 获取屏幕像素点
    public static Point getScreenSize(Activity context) {
        if (context == null) {
            return screenSize;
        }
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        if (wm != null) {
            DisplayMetrics mDisplayMetrics = new DisplayMetrics();
            Display diplay = wm.getDefaultDisplay();
            if (diplay != null) {
                diplay.getMetrics(mDisplayMetrics);
                int W = mDisplayMetrics.widthPixels;
                int H = mDisplayMetrics.heightPixels;
                if (W * H > 0 && (W > screenSize.x || H > screenSize.y)) {
                    screenSize.set(W, H);
                }
            }
        }
        return screenSize;
    }

    // 获取状态栏高度
    public static int getStatusBarHeight(Context context) {
        if (statusBarHeight <= 0) {
            Rect frame = new Rect();
            ((Activity) context).getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
            statusBarHeight = frame.top;
        }
        if (statusBarHeight <= 0) {
            try {
                Class<?> c = Class.forName("com.android.internal.R$dimen");
                Object obj = c.newInstance();
                Field field = c.getField("status_bar_height");
                int x = Integer.parseInt(field.get(obj).toString());
                statusBarHeight = context.getResources().getDimensionPixelSize(x);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        return statusBarHeight;
    }

    /**
     * 获取网络类型
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }


}
