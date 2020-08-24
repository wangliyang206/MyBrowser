package com.cj.mybrowser;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;

import androidx.appcompat.app.AppCompatActivity;

import com.cj.mybrowser.utils.ProgressWebView;
import com.cj.mybrowser.utils.ToolUtils;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.io.File;

import static android.view.KeyEvent.KEYCODE_BACK;

/**
 * 新窗口 X5
 */

public class NewWindowX5Activity extends AppCompatActivity {

    private final String TAG = NewWindowX5Activity.class.getName();
    /*----------------控件信息----------------*/
    ProgressWebView mWebView;

    /*----------------业务信息----------------*/
    // 业务区(操作数据库、请求网络)
    private static final String APP_CACAHE_DIRNAME = "/webcache";
    //  当前访问地址
    private String URL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置全屏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.newwindow_x5_activity);
        Log.i(TAG, "onCreate");

        mWebView = findViewById(R.id.pwv_nwx5_webView);
        getBundleValues();
        initViewData();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
        if (mWebView != null)
            mWebView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
        if (mWebView != null)
            mWebView.onPause();
    }

    @Override
    protected void onDestroy() {
        if (mWebView != null) {
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();
        Log.i(TAG, "onDestroy");
        clearAllCache();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            ToolUtils.hideBottomUIMenu(getWindow().getDecorView());
        }
    }

    protected void getBundleValues() {
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            URL = bundle.getString("URL");
        }
    }

    protected void initViewData() {
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView webView, int i, String s, String s1) {
                super.onReceivedError(webView, i, s, s1);
                Log.i(TAG, "onReceivedError");
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.i(TAG, "shouldOverrideUrlLoading");
                // 打开新的
//                Intent intent = new Intent(NewWindowX5Activity.this, NewWindowX5Activity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("URL", url);
//                intent.putExtras(bundle);
//                startActivity(intent);
                return false;
            }

            @Override
            public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
                super.onPageStarted(webView, s, bitmap);
                Log.i(TAG, "onPageStarted");
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.i(TAG, "onPageFinished");
                mWebView.getSettings().setBlockNetworkImage(false);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                // 解决加载https报错问题
                handler.proceed();
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                mWebView.onProgressChangedX5(view, newProgress);
                Log.i(TAG, "onProgressChanged");
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public void onCloseWindow(WebView window) {
                super.onCloseWindow(window);
                Log.i(TAG, "onCloseWindow");
                onBackPressed();
            }
        });

        mWebView.setOnLongClickListener(v -> true);

        mWebView.setScrollbarFadingEnabled(true);
        mWebView.addJavascriptInterface(this, "Android");
        WebSettings webSetting = mWebView.getSettings();
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(false);                                                   // 设置内置的缩放控件。若为false，则该WebView不可缩放
        webSetting.setUseWideViewPort(true);                                                        // 将图片调整到适合webview的大小
        webSetting.setSupportMultipleWindows(false);                                                // 设置允许开启多窗口
        webSetting.setLoadWithOverviewMode(true);                                                   // 缩放至屏幕的大小
        webSetting.setAppCacheEnabled(true);                                                        // 开启 Application Caches 功能
        webSetting.setDatabaseEnabled(true);                                                        // 开启 database storage API 功能
        webSetting.setDomStorageEnabled(true);                                                      // 开启 DOM storage API 功能
        webSetting.setJavaScriptEnabled(true);                                                      // 如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        webSetting.setCacheMode(WebSettings.LOAD_DEFAULT);                                          // 缓存模式(不使用缓存 LOAD_CACHE_ELSE_NETWORK)

        String cacheDirPath = getFilesDir().getAbsolutePath() + APP_CACAHE_DIRNAME;
        webSetting.setAppCachePath(cacheDirPath);                                                   // 设置 Application Caches 缓存目录
        webSetting.setDatabasePath(cacheDirPath);                                                   // 设置数据库缓存路径
//        webSetting.setGeolocationDatabasePath(cacheDirPath);
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
//        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        // webSetting.setPreFectch(true);
        webSetting.setBlockNetworkImage(true);

        // 测试数据
//        URL = "http://debugtbs.qq.com/";
        mWebView.loadUrl(URL);
    }

    /**
     * 关闭当前页
     */
    @JavascriptInterface
    public void onClose() {
        runOnUiThread(() -> onBackPressed());

    }


    /**
     * 清除WebView缓存
     */
    public void clearWebViewCache() {
        // 清理Webview缓存数据库
        try {
            deleteDatabase("webview.db");
            deleteDatabase("webviewCache.db");
        } catch (Exception e) {
        }

        // WebView 缓存文件
        File appCacheDir = new File(getFilesDir().getAbsolutePath() + APP_CACAHE_DIRNAME);

        File webviewCacheDir = new File(getCacheDir().getAbsolutePath() + "/webviewCache");

        // 删除webview 缓存目录
        if (webviewCacheDir.exists()) {
            deleteFile(webviewCacheDir);
        }
        // 删除webview 缓存 缓存目录
        if (appCacheDir.exists()) {
            deleteFile(appCacheDir);
        }
    }

    /**
     * 递归删除 文件/文件夹
     *
     * @param file
     */
    public void deleteFile(File file) {

        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteFile(files[i]);
                }
            }
            file.delete();
        } else {
        }
    }

    /**
     * Back监听
     */
    @Override
    public void onBackPressed() {
        Log.i(TAG, "onBackPressed");
        this.setResult(RESULT_OK);
        this.finish();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void clearAllCache() {
        clearWebViewCache();
        clearCacheFolder(this.getCacheDir(), System.currentTimeMillis());
    }


    // clear the cache before time numDays
    private int clearCacheFolder(File dir, long numDays) {
        int deletedFiles = 0;
        if (dir != null && dir.isDirectory()) {
            try {
                for (File child : dir.listFiles()) {
                    if (child.isDirectory()) {
                        deletedFiles += clearCacheFolder(child, numDays);
                    }
                    if (child.lastModified() < numDays) {
                        if (child.delete()) {
                            deletedFiles++;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return deletedFiles;
    }
}
