package com.cj.mybrowser.utils;

import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.widget.Toast;

public class ToolUtils {
    public static void hideBottomUIMenu(View v) {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            v.setSystemUiVisibility(uiOptions);
        }

    }

    // 最后一次点击时间
    private static long lastClickTime;

    /**
     * 按两次Back在退出程序
     *
     * @param context 句柄
     */
    public static void exitSys(Activity context) {
        if ((System.currentTimeMillis() - lastClickTime) > 2000) {
            Toast.makeText(context, "再按一次退出", Toast.LENGTH_LONG).show();
            lastClickTime = System.currentTimeMillis();
        } else {
            context.finish();
//            /*当前是退出APP后结束进程。如果不这样做，那么在APP结束后需求手动将EventBus中所注册的监听全部清除以免APP在次启动后重复注册监听*/
//            Process.killProcess(Process.myPid());
//            返回到桌面
//            Intent intent = new Intent(Intent.ACTION_MAIN);
//            intent.addCategory(Intent.CATEGORY_HOME);
//            context.startActivity(intent);
        }
    }
}
