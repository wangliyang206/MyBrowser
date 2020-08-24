package com.cj.mybrowser;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cj.mybrowser.utils.AppPreferencesHelper;
import com.cj.mybrowser.utils.ToolUtils;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainActivity extends AppCompatActivity {

    private EditText mInput;
    private AppPreferencesHelper mAppPreferencesHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //获取权限
        MainActivityPermissionsDispatcher.runAppWithPermissionCheck(this);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    /**
     * 申请权限成功后的逻辑
     */
    @NeedsPermission({
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.RECEIVE_BOOT_COMPLETED,
    })
    public void runApp() {
        mAppPreferencesHelper = new AppPreferencesHelper(getApplicationContext(), "sharedBrowserTest", 1);
        String val = mAppPreferencesHelper.getPref("url", "");

        // 在调用TBS初始化、创建WebView之前进行如下配置
//        HashMap map = new HashMap();
//        map.put(TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER, true);
//        map.put(TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE, true);
//        QbSdk.initTbsSettings(map);

        mInput = findViewById(R.id.edit_input);
        Button mOpen = findViewById(R.id.btn_open);
        Button mConfig = findViewById(R.id.btn_config);

        if (TextUtils.isEmpty(val)) {
            // 首次打开
            mInput.setText("https://xw.qq.com");
        } else {
            // N次打开
            jump(false, val, true);
        }

        mOpen.setOnClickListener(view -> {
            String input = mInput.getText().toString();
            if (TextUtils.isEmpty(input)) {
                Toast.makeText(MainActivity.this, "请输入网址", Toast.LENGTH_LONG).show();
                return;
            }
            jump(false, input, false);
        });

        mConfig.setOnClickListener(view -> {
            jump(true, "http://debugtbs.qq.com/", false);
        });
    }

    private void jump(boolean isDug, String url, boolean isVal) {
        if (!isDug)
            mAppPreferencesHelper.put("url", url);
        Intent intent = new Intent(this, NewWindowX5Activity.class);
        Bundle bundle = new Bundle();
        bundle.putString("URL", url);
        bundle.putBoolean("isVal", isVal);
        intent.putExtras(bundle);
        startActivity(intent);
        if (isVal) {
            // 关闭界面
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        ToolUtils.exitSys(this);
    }

}
